package SolarSystem.Models;

public class StringResponse {

    private String response;

    public StringResponse(String s) {
        this.setResponse(s);
    }

    public String getResponse() {
        return response;
    }

    private void setResponse(String response) {
        this.response = response;
    }
}