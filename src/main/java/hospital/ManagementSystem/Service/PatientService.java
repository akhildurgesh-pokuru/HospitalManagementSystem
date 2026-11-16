package hospital.ManagementSystem.Service;

import hospital.ManagementSystem.Dto.PatientDto;
import hospital.ManagementSystem.Entity.Patient;
import hospital.ManagementSystem.Repository.PatientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class PatientService {

    private final PatientRepository repository;

    public PatientService(PatientRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public PatientDto createPatient(PatientDto dto) {
        Patient patient = new Patient();
        copyFields(dto, patient);
        return toDto(repository.save(patient));
    }

    @Transactional(readOnly = true)
    public List<PatientDto> getAllPatients() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public PatientDto getPatientById(Long id) {
        return toDto(findPatient(id));
    }

    @Transactional
    public PatientDto updatePatient(Long id, PatientDto dto) {
        Patient patient = findPatient(id);
        copyFields(dto, patient);
        return toDto(repository.save(patient));
    }

    @Transactional
    public void deletePatient(Long id) {
        Patient patient = findPatient(id);
        repository.delete(patient);
    }

    private Patient findPatient(Long id) {
        return repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient not found: " + id));
    }

    private void copyFields(PatientDto dto, Patient patient) {
        patient.setName(dto.name().trim());
        patient.setAge(dto.age());
        patient.setGender(dto.gender().trim());
        patient.setDisease(dto.disease().trim());
        patient.setAddress(dto.address());
        patient.setPhoneNumber(dto.phoneNumber());
    }

    private PatientDto toDto(Patient patient) {
        return new PatientDto(patient.getId(), patient.getName(), patient.getAge(),
                patient.getGender(), patient.getDisease(), patient.getAddress(),
                patient.getPhoneNumber());
    }
}

