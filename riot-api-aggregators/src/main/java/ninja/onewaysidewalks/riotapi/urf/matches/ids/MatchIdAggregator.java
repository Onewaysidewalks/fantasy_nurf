package ninja.onewaysidewalks.riotapi.urf.matches.ids;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import ninja.onewaysidewalks.messaging.client.consumers.CompetingConsumerConfig;
import ninja.onewaysidewalks.messaging.client.consumers.MessageHandler;
import ninja.onewaysidewalks.messaging.client.consumers.rabbitmq.CompetingConsumerImpl;
import ninja.onewaysidewalks.messaging.client.producers.Producer;
import ninja.onewaysidewalks.messaging.client.producers.ProducerConfig;
import ninja.onewaysidewalks.messaging.client.producers.rabbitmq.fanout.FanoutProducerImpl;
import ninja.onewaysidewalks.riotapi.RiotClient;
import ninja.onewaysidewalks.riotapi.RiotClientImpl;
import ninja.onewaysidewalks.riotapi.urf.matches.ids.persistence.MatchIdPersistence;
import ninja.onewaysidewalks.riotapi.urf.matches.ids.persistence.MatchIdPersistenceImpl;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * This is a simple tool to grab all 5-minute buckets from the RIOT api, and store their contents (match ids) into
 * cassandra for later use.
 *
 * Ideally: this would become a true service, and simply listen for queue messages about what time bucket query the API for
 *
 * Super Ideally: the platform would be able to listen for "Game Completed" notifications, and simply record the match ids that way
 * (making this tool, and the API that fuels it, obsolete.)
 *
 *
 * The match ids (which are stored in the same 5 minute buckets into cassandra (for history persistence), and enqueue a message for match receivers
 * to listen to and pull the actual match information (stats, etc.) from RIOT and store for later calculation
 *
 */
@Slf4j
public class MatchIdAggregator {
    /**
     *
     * @param args first element should be a long representing a start time, second element being a long representing the end time
     *             Note: all times are UTC (see ISO-8601)
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        new MatchIdAggregator().run(args);
    }

    public void run(String[] args) throws Exception {

        MatchIdPersistence matchIdPersistence = null;
        CompetingConsumerImpl<Long> competingConsumer = null;
        ObjectMapper objectMapper = new ObjectMapper();

        //There needs to be a global cassandra cluster management piece
        //That takes in configuration, and shares the session for all who need it
        Cluster cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
        Session session = cluster.connect();

        try {
            //The default start time
            DateTime beginning = DateTime.now(DateTimeZone.UTC).minusHours(8);

            //Normalize time to have no seconds or minutes
            beginning = beginning.minusMillis(beginning.getMillisOfSecond()).minusSeconds(beginning.getSecondOfMinute());
            DateTime end = DateTime.now();
            if (args != null && args.length >= 1) {
                beginning = new DateTime(new Long(args[0]), DateTimeZone.UTC);

                if (args.length >= 2) {
                    end  = new DateTime(new Long(args[1]), DateTimeZone.UTC);
                }
            }

            //These config objects should come from disk/config service
            ProducerConfig producerConfig = new ProducerConfig();
            producerConfig.setHost("192.168.33.11");

            //in a real service these things would be registered into a container, and resolved out on demand
            final RiotClient riotClient = new RiotClientImpl(objectMapper);
            matchIdPersistence = new MatchIdPersistenceImpl(session);
            final Producer producer = new FanoutProducerImpl(objectMapper, producerConfig);

            Queue<DateTime> bucketsToRead = new ArrayDeque<>(getTimeBuckets(beginning, end));

            while (bucketsToRead.size() > 0) {
                DateTime bucket = bucketsToRead.peek();

                try {
                    long timestamp = bucket.getMillis() / 1000; //Riot API expects 5 minute buckets, with SECOND granularity

                    if (matchIdPersistence.bucketExists(timestamp)) {
                        log.info("bucket already written, skipping. Bucket {}", timestamp);
                        bucketsToRead.remove();
                        continue;
                    }

                    log.info("querying for bucket {}", timestamp);
                    List<Long> matchIds = riotClient.getMatchIdsForUrf(timestamp, RiotClientImpl.Region.NA);
                    matchIdPersistence.writeBucket(timestamp, matchIds);

                    for (Long matchId : matchIds) {
                        producer.sendMessage("match_completed", matchId);
                    }

                    bucketsToRead.remove();
                } catch (Exception ex) {
                    log.error("unable to query for match ids and save to cassandra. Bucket: {}", bucket, ex);
                    Thread.sleep(5000);
                }
            }
        } catch (Exception ex) {
            log.error("Couldnt continue to run aggregator.", ex);
        } finally {
            //should be called on application shutdown
            if (competingConsumer != null) {
                competingConsumer.stop();
            }

            if (!session.isClosed()) {
                session.close();
            }

            if (!cluster.isClosed()) {
                cluster.close();
            }
        }
    }

    /**
     * This method will take a time range and slice it up into 5 minute bucketss
     * @param beginning starting time for buckets
     * @param end ending time for buckets
     * @return a list of 5 minute buckets, in chronological order
     */
    public List<DateTime> getTimeBuckets(DateTime beginning, DateTime end) {
        int minutesAwayFromBucket = beginning.getMinuteOfHour() % 5;
        if (minutesAwayFromBucket != 0) {
            beginning = beginning.minusMinutes(minutesAwayFromBucket);
        }

        DateTime currentPlace = beginning;

        List<DateTime> buckets = new ArrayList<>();
        while (currentPlace.isBefore(end)) {
            buckets.add(currentPlace);
            currentPlace = currentPlace.plusMinutes(5);
        }

        return buckets;
    }
}