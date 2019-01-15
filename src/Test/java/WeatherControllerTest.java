package SolarSystem.Test;

import SolarSystem.Application;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class WeatherControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void getMonitor() throws Exception {
                mvc.perform(get("/monitor").accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE))
                        .andExpect(jsonPath("$.response").value("The system is up and running"));

    }

    @Test
    public void compute() throws Exception {
        mvc.perform(get("/weather/compute?days=5").accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.response").value("WeatherDays computed succesfully days 5 for SolarSystem Astral"));
    }

//    @Test
//    @DirtiesContext
//    /* Test not working due to Spring bug: https://stackoverflow.com/questions/22712325/multiple-tests-with-autowired-mockhttpservletrequest-not-working*/
//    public void errorNotFound() throws Exception {
//
//        mvc.perform(get("/weather/day/999999").accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.status").value("404"))
//                .andExpect(jsonPath("$.error").value("Not Found"))
//                .andReturn();
//    }

//    @Test
//    @DirtiesContext
//    /* Test not working due to Spring bug: https://stackoverflow.com/questions/22712325/multiple-tests-with-autowired-mockhttpservletrequest-not-working*/
//    public void errorBadRequest() throws Exception {
//        mvc.perform(get("/weather/day/string").accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.status").value("400"))
//                .andExpect(jsonPath("$.error").value("Bad Request"))
//                .andReturn();
//    }

    @After
    public void weatherDay() throws Exception {
        mvc.perform(get("/weather/day/0").accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.day").value("0"))
                .andExpect(jsonPath("$.weatherDay").value("DRY"));
                //.andReturn();
    }
}
