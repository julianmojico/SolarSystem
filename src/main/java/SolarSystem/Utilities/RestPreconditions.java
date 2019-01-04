package SolarSystem.Utilities;

import SolarSystem.Exceptions.ResourceNotFoundException;

public class RestPreconditions {
    public static <T> T checkFound(T resource) {

        if (resource==null){
            throw new ResourceNotFoundException();
        }

        return resource;
    }
}
