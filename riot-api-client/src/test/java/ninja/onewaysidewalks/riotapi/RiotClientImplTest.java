package ninja.onewaysidewalks.riotapi;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Test
public class RiotClientImplTest {

    private RiotClientImpl subject;

    @BeforeMethod
    public void setUp() {
        subject = new RiotClientImpl();
    }

    @Test
    public void getUrfIds_validCall_success() {
        subject.getMatchIdsForUrf(1427940000, RiotClientImpl.Region.NA);
    }
}
