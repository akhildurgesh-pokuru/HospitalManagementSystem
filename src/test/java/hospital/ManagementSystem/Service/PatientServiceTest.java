package hospital.ManagementSystem.Service;

import hospital.ManagementSystem.Dto.PatientDto;
import hospital.ManagementSystem.Entity.Patient;
import hospital.ManagementSystem.Repository.PatientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {
    @Mock private PatientRepository repository;
    @InjectMocks private PatientService service;

    private PatientDto sampleDto() {
        return new PatientDto(null, "Rahul", 27, "Male", "Fever", "Chennai", "9876543210");
    }

    private Patient savedPatient() {
        Patient p = new Patient();
        p.setId(1L);
        p.setName("Rahul");
        p.setAge(27);
        p.setGender("Male");
        p.setDisease("Fever");
        p.setAddress("Chennai");
        p.setPhoneNumber("9876543210");
        return p;
    }

    @Test
    void createPatient_savesAndReturnsPatient() {
        when(repository.save(any(Patient.class))).thenAnswer(invocation -> {
            Patient patient = invocation.getArgument(0);
            patient.setId(1L);
            return patient;
        });

        PatientDto response = service.createPatient(sampleDto());

        assertEquals(1L, response.id());
        assertEquals("Rahul", response.name());
        assertEquals("Fever", response.disease());
        verify(repository).save(any(Patient.class));
    }

    @Test
    void getAllPatients_returnsList() {
        when(repository.findAll()).thenReturn(List.of(savedPatient()));
        List<PatientDto> result = service.getAllPatients();
        assertEquals(1, result.size());
        assertEquals("Rahul", result.get(0).name());
    }

    @Test
    void getPatientById_existingIdReturnsPatient() {
        when(repository.findById(1L)).thenReturn(Optional.of(savedPatient()));
        PatientDto result = service.getPatientById(1L);
        assertEquals(1L, result.id());
        assertEquals(27, result.age());
    }

    @Test
    void getPatientById_missingIdReturnsNotFound() {
        when(repository.findById(999L)).thenReturn(Optional.empty());
        ResponseStatusException error = assertThrows(ResponseStatusException.class,
                () -> service.getPatientById(999L));
        assertEquals(HttpStatus.NOT_FOUND, error.getStatusCode());
    }

    @Test
    void updatePatient_updatesExistingPatient() {
        Patient existing = savedPatient();
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);
        PatientDto updated = new PatientDto(null, "Rahul", 28, "Male", "Viral Fever",
                "Chennai", "9876543210");

        PatientDto result = service.updatePatient(1L, updated);

        assertEquals(28, result.age());
        assertEquals("Viral Fever", result.disease());
        verify(repository).save(existing);
    }

    @Test
    void deletePatient_existingPatientDeletesIt() {
        Patient existing = savedPatient();
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        service.deletePatient(1L);
        verify(repository).delete(existing);
    }
}
