package SolarSystem.Test;

import SolarSystem.Implementations.SolarSystemManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;


@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest
public class SolarSystemManagerTest {

    @Autowired
    private SolarSystemManager ssm;

    @Test
    public void instance() {

        try {
            this.ssm.initialize("Astral", 0, 0);
            ssm.setupSampleSystem();
            ssm.timePassSequence(10);
        } catch (Exception e) {
            fail(e.getMessage());
        }
        assertEquals("Astral", ssm.getSolarSystem().getName());
        assertEquals(ssm.getSolarSystem().getSolarCenter().getX(),0,0);
        assertEquals(ssm.getSolarSystem().getSolarCenter().getY(),0,0);
        assertNotNull(ssm.getSolarSystem().getPlanets());
        assertNotNull(ssm.planetsAligned);
        assertNotNull(ssm.planetsAlignedWithSun);
        assertNotNull(ssm.maxRainyDays);
    }

}