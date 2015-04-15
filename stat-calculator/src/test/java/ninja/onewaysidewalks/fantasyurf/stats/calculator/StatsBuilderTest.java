package ninja.onewaysidewalks.fantasyurf.stats.calculator;

import ninja.onewaysidewalks.fantasyurf.stats.calculator.shared.ChampionSpecificStatProvider;
import ninja.onewaysidewalks.fantasyurf.stats.calculator.shared.StatsBuilder;
import ninja.onewaysidewalks.fantasyurf.stats.calculator.shared.providers.AssistsByChampionStatProvider;
import ninja.onewaysidewalks.fantasyurf.stats.calculator.shared.providers.DeathsByChampionStatProvider;
import ninja.onewaysidewalks.fantasyurf.stats.calculator.shared.providers.KillsByChampionStatProvider;
import ninja.onewaysidewalks.riotapi.models.Match;
import ninja.onewaysidewalks.riotapi.models.Participant;
import ninja.onewaysidewalks.riotapi.models.ParticipantStats;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@Test
public class StatsBuilderTest {
    @Test
    public void statBuilder_noMatches_succeedsWithEmptyMap() {
        Map<Integer, Map<String, Double>> statistics = StatsBuilder.start(new ChampionSpecificStatProvider[]{
                new KillsByChampionStatProvider(),
                new AssistsByChampionStatProvider(),
                new DeathsByChampionStatProvider()
        }).buildStats();

        assertThat(statistics.size(), is(0));
    }

    @Test
    public void statBuilder_onlyOneMatch_succeedsWithOneMatchOfData() {
        Map<Integer, Map<String, Double>> statistics = StatsBuilder.start(new ChampionSpecificStatProvider[] {
                new KillsByChampionStatProvider(),
                new AssistsByChampionStatProvider(),
                new DeathsByChampionStatProvider()
        })
            .withMatch(provideMatch(1337, 5, 6, 7))
            .buildStats();

        assertThat(statistics.size(), is(1));
        assertThat(statistics.get(1337).size(), is(21)); //7 stats per champion data point

        assertThat(statistics.get(1337).get("kills-mean"), is(5d));
        assertThat(statistics.get(1337).get("kills-min"), is(5d));
        assertThat(statistics.get(1337).get("kills-max"), is(5d));
        assertThat(statistics.get(1337).get("kills-p50"), is(5d));
        assertThat(statistics.get(1337).get("kills-p75"), is(5d));
        assertThat(statistics.get(1337).get("kills-p90"), is(5d));
        assertThat(statistics.get(1337).get("kills-p99"), is(5d));

        assertThat(statistics.get(1337).get("assists-mean"), is(7d));
        assertThat(statistics.get(1337).get("assists-min"), is(7d));
        assertThat(statistics.get(1337).get("assists-max"), is(7d));
        assertThat(statistics.get(1337).get("assists-p50"), is(7d));
        assertThat(statistics.get(1337).get("assists-p75"), is(7d));
        assertThat(statistics.get(1337).get("assists-p90"), is(7d));
        assertThat(statistics.get(1337).get("assists-p99"), is(7d));

        assertThat(statistics.get(1337).get("deaths-mean"), is(6d));
        assertThat(statistics.get(1337).get("deaths-min"), is(6d));
        assertThat(statistics.get(1337).get("deaths-max"), is(6d));
        assertThat(statistics.get(1337).get("deaths-p50"), is(6d));
        assertThat(statistics.get(1337).get("deaths-p75"), is(6d));
        assertThat(statistics.get(1337).get("deaths-p90"), is(6d));
        assertThat(statistics.get(1337).get("deaths-p99"), is(6d));
    }

    @Test
    public void statBuilder_multipleMatches_succeedsWithMultipleMatchData() {
        Map<Integer, Map<String, Double>> statistics = StatsBuilder.start(new ChampionSpecificStatProvider[]{
                new KillsByChampionStatProvider(),
                new AssistsByChampionStatProvider()
        })
                .withMatch(provideMatch(1337, 5, 6, 7))
                .withMatch(provideMatch(1337, 5, 6, 7))
                .withMatch(provideMatch(1337, 5, 6, 7))
                .withMatch(provideMatch(1337, 5, 6, 7))
                .withMatch(provideMatch(1337, 15, 6, 7))
                .withMatch(provideMatch(1337, 5, 6, 7))
                .withMatch(provideMatch(1337, 5, 6, 7))
                .withMatch(provideMatch(1337, 5, 6, 37))
                .withMatch(provideMatch(1337, 2, 16, 7))
                .withMatch(provideMatch(1337, 5, 6, 7))
                .withMatch(provideMatch(1337, 8, 8, 8))
                .withMatch(provideMatch(1337, 9, 9, 9))


                .buildStats();

        assertThat(statistics.size(), is(1));
        assertThat(statistics.get(1337).size(), is(14)); //7 stats per champion data point

        assertThat(statistics.get(1337).get("kills-mean"), is(6.166666666666667d));
        assertThat(statistics.get(1337).get("kills-min"), is(2d));
        assertThat(statistics.get(1337).get("kills-max"), is(15d));
        assertThat(statistics.get(1337).get("kills-p50"), is(5d));
        assertThat(statistics.get(1337).get("kills-p75"), is(7.25d));
        assertThat(statistics.get(1337).get("kills-p90"), is(13.200000000000006d));
        assertThat(statistics.get(1337).get("kills-p99"), is(15d));

        assertThat(statistics.get(1337).get("assists-mean"), is(9.75d));
        assertThat(statistics.get(1337).get("assists-min"), is(7d));
        assertThat(statistics.get(1337).get("assists-max"), is(37d));
        assertThat(statistics.get(1337).get("assists-p50"), is(7d));
        assertThat(statistics.get(1337).get("assists-p75"), is(7.75d));
        assertThat(statistics.get(1337).get("assists-p90"), is(28.60000000000003d));
        assertThat(statistics.get(1337).get("assists-p99"), is(37d));
    }

    private Match provideMatch(int championId, int kills, int deaths, int assists) {
        Match match = new Match();
        Participant participant = new Participant();
        participant.setChampionId(championId);
        match.setParticipants(Collections.singletonList(participant));

        ParticipantStats participantStats = new ParticipantStats();
        participant.setStats(participantStats);

        participantStats.setAssists(assists);
        participantStats.setKills(kills);
        participantStats.setDeaths(deaths);

        return match;
    }

}
