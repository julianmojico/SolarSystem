package SolarSystem.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Request could not be processed due to an Internal Server Error")
public class ServerErrorException extends Exception {

    private static final long serialVersionUID = 100L;

}
