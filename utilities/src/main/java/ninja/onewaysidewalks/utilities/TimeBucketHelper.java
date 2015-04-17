package ninja.onewaysidewalks.utilities;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.ArrayList;
import java.util.List;

public class TimeBucketHelper {
    public static DateTime getTimeBucket(Long matchTimeMills, TimeBucketInterval timeBucketInterval) {
        //TODO: handle other time intervals. Not needed for competition

        DateTime matchTime = new DateTime(matchTimeMills, DateTimeZone.UTC);

        if (timeBucketInterval.equals(TimeBucketInterval.HOUR)) {
            return new DateTime(matchTime.getYear(), matchTime.getMonthOfYear(), matchTime.getDayOfMonth(), matchTime.getHourOfDay(), 0, DateTimeZone.UTC);
        } else {
            throw new IllegalArgumentException("Non-Hour timebuckets not supported");
        }
    }

    public static List<DateTime> getTimeBuckets(DateTime startTime, DateTime endTime, TimeBucketInterval timeBucketInterval) {
        if (timeBucketInterval.equals(TimeBucketInterval.HOUR)) {
            DateTime startBucket = getTimeBucket(startTime.getMillis(), timeBucketInterval);
            DateTime endBucket = getTimeBucket(endTime.getMillis(), timeBucketInterval);
            DateTime currentBucket = startBucket;

            List<DateTime> buckets = new ArrayList<>();
            buckets.add(startBucket);

            while (currentBucket.isBefore(endBucket)) {
                currentBucket = currentBucket.plusHours(1);
                buckets.add(currentBucket);
            }

            return buckets;
        } else {
            throw new IllegalArgumentException("Non-Hour timebuckets not supported");
        }
    }
}
