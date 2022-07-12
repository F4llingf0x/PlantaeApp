package hu.plantae.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.plantae.controller.PlantController;
import hu.plantae.dto.PlantCreateCommand;
import hu.plantae.dto.PlantInfo;
import hu.plantae.service.PlantService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static hu.plantae.domain.SunRequirement.FULLSUN;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PlantController.class)
public class PlantControllerValidationTest {

    @MockBean
    PlantService service;

    @Autowired
    MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    private PlantCreateCommand plantCreateCommand;
    private List<String> firstPlantName = List.of("Tracheophytes",
            "Angiosperms",
            "Monocots",
            "Commelinids",
            "Arecales",
            "Arecaceae",
            "Phoenix",
            "canariensis");


    @Test
    void init() throws Exception {
        plantCreateCommand = new PlantCreateCommand(firstPlantName, List.of("https://example.jpg"),
                10d,
                20d,
                30d,
                40d,
                50d,
                60d,
                FULLSUN);
    }
//----------------------------TESTING POST /api/addPlant----------------------------------------

    @Test
    void saveValidation_OneInput_Success() throws Exception {
        init();
        when(service.savePlant(any())).thenReturn(new PlantInfo());
        mockMvc.perform(post("/api/plants/addPlant")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(plantCreateCommand)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void saveValidation_MinTemperature_NotValid() throws Exception {
        init();
        plantCreateCommand.setMinimumTemperature(-60d);
        when(service.savePlant(any())).thenReturn(new PlantInfo());
        mockMvc.perform(post("/api/plants/addPlant")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(plantCreateCommand)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field", equalTo("minimumTemperature")))
                .andExpect(jsonPath("$[0].errorMessage", equalTo("must be greater than or equal to -40")));
    }

    @Test
    void saveValidation_MaxTemperature_NotValid() throws Exception {
        init();
        plantCreateCommand.setMaximumTemperature(100d);
        when(service.savePlant(any())).thenReturn(new PlantInfo());
        mockMvc.perform(post("/api/plants/addPlant")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(plantCreateCommand)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field", equalTo("maximumTemperature")))
                .andExpect(jsonPath("$[0].errorMessage", equalTo("must be less than or equal to 60")));
    }

    @Test
    void saveValidation_MinAirHumidity_NotValid() throws Exception {
        init();
        plantCreateCommand.setMinimumAirHumidity(-60d);
        when(service.savePlant(any())).thenReturn(new PlantInfo());
        mockMvc.perform(post("/api/plants/addPlant")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(plantCreateCommand)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field", equalTo("minimumAirHumidity")))
                .andExpect(jsonPath("$[0].errorMessage", equalTo("must be greater than or equal to 0")));
    }

    @Test
    void saveValidation_MaxAirHumidity_NotValid() throws Exception {
        init();
        plantCreateCommand.setMaximumAirHumidity(110d);
        when(service.savePlant(any())).thenReturn(new PlantInfo());
        mockMvc.perform(post("/api/plants/addPlant")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(plantCreateCommand)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field", equalTo("maximumAirHumidity")))
                .andExpect(jsonPath("$[0].errorMessage", equalTo("must be less than or equal to 100")));
    }

    @Test
    void saveValidation_MinSoilMoisture_NotValid() throws Exception {
        init();
        plantCreateCommand.setMinimumSoilHumidity(-60d);
        when(service.savePlant(any())).thenReturn(new PlantInfo());
        mockMvc.perform(post("/api/plants/addPlant")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(plantCreateCommand)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field", equalTo("minimumSoilHumidity")))
                .andExpect(jsonPath("$[0].errorMessage", equalTo("must be greater than or equal to 0")));
    }

    @Test
    void saveValidation_MaxSoilMoisture_NotValid() throws Exception {
        init();
        plantCreateCommand.setMaximumSoilHumidity(110d);
        when(service.savePlant(any())).thenReturn(new PlantInfo());
        mockMvc.perform(post("/api/plants/addPlant")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(plantCreateCommand)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field", equalTo("maximumSoilHumidity")))
                .andExpect(jsonPath("$[0].errorMessage", equalTo("must be less than or equal to 100")));
    }

//-------------------------------TESTING GET /api/{id}-------------------------------------

    @Test
    void getValidation_getPlantById_Valid() throws Exception {
        init();
        when(service.findPlantById(anyInt())).thenReturn(new PlantInfo());
        mockMvc.perform(get("/api/plants/{id}", 1))
                .andDo(print())
                .andExpect(status().isAccepted());
    }

    @Test
    void getValidation_getPlantById_NotValid() throws Exception {
        init();
        when(service.findPlantById(anyInt())).thenReturn(new PlantInfo());
        mockMvc.perform(get("/api/plants/{id}", -1))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field", equalTo("Parameter")))
                .andExpect(jsonPath("$[0].errorMessage", equalTo("must be more than or equal to 1")));
    }

//-------------------------------TESTING PUT /api/{id}-------------------------------------

    @Test
    void modifyValidation_MaxTemperature_Valid() throws Exception {

        init();
        when(service.modifyPlant(anyInt(), any())).thenReturn(new PlantInfo());
        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.put("/api/plants/{id}/", "1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(mapper.writeValueAsString(plantCreateCommand));

        this.mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status()
                        .isAccepted());
    }

    @Test
    void modifylidation_MinAirHumidity_NotValid() throws Exception {
        init();
        plantCreateCommand.setMinimumAirHumidity(-60d);
        when(service.savePlant(any())).thenReturn(new PlantInfo());
        mockMvc.perform(post("/api/plants/addPlant")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(plantCreateCommand)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field", equalTo("minimumAirHumidity")))
                .andExpect(jsonPath("$[0].errorMessage", equalTo("must be greater than or equal to 0")));
    }

    @Test
    void modifyValidation_MaxAirHumidity_NotValid() throws Exception {
        init();
        plantCreateCommand.setMaximumAirHumidity(110d);
        when(service.savePlant(any())).thenReturn(new PlantInfo());
        mockMvc.perform(post("/api/plants/addPlant")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(plantCreateCommand)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field", equalTo("maximumAirHumidity")))
                .andExpect(jsonPath("$[0].errorMessage", equalTo("must be less than or equal to 100")));
    }

    @Test
    void modifyValidation_MinSoilMoisture_NotValid() throws Exception {
        init();
        plantCreateCommand.setMinimumSoilHumidity(-60d);
        when(service.savePlant(any())).thenReturn(new PlantInfo());
        mockMvc.perform(post("/api/plants/addPlant")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(plantCreateCommand)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field", equalTo("minimumSoilHumidity")))
                .andExpect(jsonPath("$[0].errorMessage", equalTo("must be greater than or equal to 0")));
    }

    @Test
    void modifyValidation_MaxSoilMoisture_NotValid() throws Exception {
        init();
        plantCreateCommand.setMaximumSoilHumidity(110d);
        when(service.savePlant(any())).thenReturn(new PlantInfo());
        mockMvc.perform(post("/api/plants/addPlant")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(plantCreateCommand)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field", equalTo("maximumSoilHumidity")))
                .andExpect(jsonPath("$[0].errorMessage", equalTo("must be less than or equal to 100")));
    }

    
}
