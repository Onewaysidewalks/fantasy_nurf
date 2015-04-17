package ninja.onewaysidewalks.riotapi;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import ninja.onewaysidewalks.riotapi.models.Champion;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

/**
 * This is a manual testing class to verify the serialization and usage of the RIOT API
 * Should not be run in automation/CI setups (due to rate limiting)
 */
@Test
public class RiotClientImplTest {

    private RiotClientImpl subject;

    @BeforeMethod
    public void setUp() {
        ObjectMapper om = new ObjectMapper();
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        subject = new RiotClientImpl(om);
    }

    @Test
    public void getUrfIds_validCall_success() {
        subject.getMatchIdsForUrf(1427940000, RiotClientImpl.Region.NA);
    }

    @Test
    public void getChampions_validCall_success() {
        List<Champion> champions = subject.getChampions(RiotClientImpl.Region.NA);

        System.out.println(champions);
    }
}
