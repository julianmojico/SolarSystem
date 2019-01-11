package SolarSystem.Tests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest
public class ControllersTests {

    @Autowired
    private MockMvc mvc;


//    @MockBean
//    private SolarSystemController solarSystemController;

    @Test
    public void getMonitor() throws Exception {
                mvc.perform(get("/monitor").accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE))
                        .andExpect(jsonPath("$.response").value("The system is up and running"))
                     .andReturn();
    }

    @Test
    public void compute() throws Exception {
        mvc.perform(get("/astral/weather/compute").accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.response").value("WeatherDays computed succesfully days 3650 for SolarSystem Astral"))
                .andReturn();
    }

    @Test
    public void weatherDay() throws Exception {
        mvc.perform(get("/astral/weather/day/0").accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.day").value("0"))
                .andExpect(jsonPath("$.weatherDay").value("DRY"));
                //.andReturn();
    }
}
