package hospital.ManagementSystem.Controller;

import hospital.ManagementSystem.Dto.PatientDto;
import hospital.ManagementSystem.Service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PatientControllerTest {
    private final PatientService service = mock(PatientService.class);
    private MockMvc mvc;

    private static final String PATIENT_JSON = """
        {"name":"Rahul","age":27,"gender":"Male","disease":"Fever",
         "address":"Chennai","phoneNumber":"9876543210"}
        """;

    private PatientDto patient() {
        return new PatientDto(1L, "Rahul", 27, "Male", "Fever", "Chennai", "9876543210");
    }

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(new PatientController(service)).build();
    }

    @Test
    void addPatient_returnsCreated() throws Exception {
        when(service.createPatient(any(PatientDto.class))).thenReturn(patient());
        mvc.perform(post("/api/patients").contentType(MediaType.APPLICATION_JSON).content(PATIENT_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Rahul"));
    }

    @Test
    void getAllPatients_returnsPatients() throws Exception {
        when(service.getAllPatients()).thenReturn(List.of(patient()));
        mvc.perform(get("/api/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Rahul"));
    }

    @Test
    void getPatientById_returnsPatient() throws Exception {
        when(service.getPatientById(1L)).thenReturn(patient());
        mvc.perform(get("/api/patients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void updatePatient_returnsUpdatedPatient() throws Exception {
        when(service.updatePatient(eq(1L), any(PatientDto.class))).thenReturn(patient());
        mvc.perform(put("/api/patients/1").contentType(MediaType.APPLICATION_JSON).content(PATIENT_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Rahul"));
    }

    @Test
    void deletePatient_returnsNoContent() throws Exception {
        mvc.perform(delete("/api/patients/1"))
                .andExpect(status().isNoContent());
        verify(service).deletePatient(1L);
    }
}
