package SolarSystem.Tests;


import SolarSystem.Application;
import SolarSystem.Implementations.SolarSystemManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;


@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class SolarSystemManagerTest {

    @Autowired
    private SolarSystemManager ssm;

    @Test
    public void instance() {

        int days = 10;

        try {
            this.ssm.initialize("Astral", 0, 0);
            ssm.setupSampleSystem();
            ssm.timePassSequence(days);
        } catch (Exception e) {
            fail(e.getMessage());
        }
        assertEquals("Astral", ssm.getSolarSystem().getName());
        assertEquals(ssm.getSolarSystem().getSolarCenter().getX(),0,0);
        assertEquals(ssm.getSolarSystem().getSolarCenter().getY(),0,0);
        assertNotNull(ssm.getSolarSystem().getPlanets());
        assertNotNull(ssm.weatherRecords);
        assertNotNull(ssm.maxRainyDays);
        assertEquals(days, ssm.weatherRecords.size() - 1);
    }

}