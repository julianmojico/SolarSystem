package SolarSystem.Implementations;

import SolarSystem.Models.SolarSystem;
import SolarSystem.Services.JobLoggerService;
import SolarSystem.Services.SolarSystemService;
import SolarSystem.Utilities.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;


@Service
//A specific implementation of SolarSystem with custom logic and calculations.
public class SolarSystemImpl implements SolarSystemService {

    //Each service handles each SolarSystem since they have their own properties and logic
    //Time is a variable related to each SolarSystem (see Einsten's Theory of General Relativity)

    @Autowired
    private SolarSystem solarSystem;
    @Autowired
    private Tuple tuple;
    private static final Logger logger = Logger.getLogger(SolarSystemImpl.class.getName());

    //TODO: ESTA INSTANCIA LOGGER ES UNICA SI CREO DOS SOLARSYSTEMIMPL?

    public SolarSystemImpl(){

    }

    public SolarSystemImpl(double x, double y) {

        tuple = new Tuple(x,y);
        //tuple.createTuple(x,y);
        solarSystem = new SolarSystem(tuple);
        solarSystem.setSolarCenter(tuple);
        logger.log(Level.INFO, "SolarSystem created");
    }

    @Override
    public void timePass(int days) {

    }

    @Override
    public boolean planetsAlignedWithSun() {

       // StraightLine2D line;
        return false;
    }

    @Override
    public void getCurrentStatus() {

    }

    public void setLogger(Logger logger){
        logger = logger;
    }

    public boolean planetsAligned() {
        return false;
    }

}
