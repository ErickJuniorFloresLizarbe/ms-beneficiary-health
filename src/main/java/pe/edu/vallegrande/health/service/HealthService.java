package pe.edu.vallegrande.health.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.health.model.Health;
import pe.edu.vallegrande.health.repository.HealthRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class HealthService {

    @Autowired
    private HealthRepository repository;

    public Flux<Health> findAll() {
        return repository.findAll()
                .onErrorResume(e -> {
                    System.err.println("Error al obtener todos los registros de salud: " + e.getMessage());
                    return Flux.empty();
                });
    }

    public Mono<Health> findById(Integer id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("No se encontró salud con ID: " + id)))
                .onErrorResume(e -> {
                    System.err.println("Error al buscar salud por ID: " + e.getMessage());
                    return Mono.empty();
                });
    }

    public Mono<Health> save(Health health) {
        return repository.save(health)
                .onErrorResume(e -> {
                    System.err.println("Error al guardar salud: " + e.getMessage());
                    return Mono.empty();
                });
    }

    public Mono<Void> deleteById(Integer id) {
        return repository.deleteById(id)
                .onErrorResume(e -> {
                    System.err.println("Error al eliminar salud por ID: " + e.getMessage());
                    return Mono.empty();
                });
    }

    public Mono<Boolean> existsById(Integer id) {
        return repository.existsById(id)
                .onErrorResume(e -> {
                    System.err.println("Error al verificar existencia por ID: " + e.getMessage());
                    return Mono.just(false);
                });
    }

    public Flux<Health> getByPersonId(Integer personId) {
        return repository.findByPersonId(personId)
                .map(this::convertToDTO)
                .onErrorResume(e -> {
                    System.err.println("Error al obtener salud por personId: " + e.getMessage());
                    return Flux.empty();
                });
    }

    private Health convertToDTO(Health health) {
        Health dto = new Health();
        dto.setIdHealth(health.getIdHealth());
        dto.setVaccine(health.getVaccine());
        dto.setVph(health.getVph());
        dto.setInfluenza(health.getInfluenza());
        dto.setDeworming(health.getDeworming());
        dto.setHemoglobin(health.getHemoglobin());
        dto.setApplicationDate(health.getApplicationDate());
        dto.setCondicionBeneficiary(health.getCondicionBeneficiary());
        dto.setPersonId(health.getPersonId());
        return dto;
    }

    public Mono<Health> saveHealthHistory(Health health) {
        Health history = new Health();
        history.setVaccine(health.getVaccine());
        history.setVph(health.getVph());
        history.setInfluenza(health.getInfluenza());
        history.setDeworming(health.getDeworming());
        history.setHemoglobin(health.getHemoglobin());        
        history.setApplicationDate(health.getApplicationDate());
        history.setCondicionBeneficiary(health.getCondicionBeneficiary());
        history.setPersonId(health.getPersonId());

        return repository.save(history)
                .onErrorResume(e -> {
                    System.err.println("Error al guardar historial de salud: " + e.getMessage());
                    return Mono.empty();
                });
    }

    public Mono<Health> updateHealthWithHistory(Integer id, Health health) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("No se encontró el registro con ID: " + id)))
                .flatMap(existingHealth ->
                        saveHealthHistory(existingHealth)
                                .onErrorResume(e -> {
                                    System.err.println("Error al guardar historial de salud: " + e.getMessage());
                                    return Mono.empty();
                                })
                                .then(updateExistingHealth(existingHealth, health))
                                .onErrorResume(e -> {
                                    System.err.println("Error al actualizar salud: " + e.getMessage());
                                    return Mono.error(new RuntimeException("Fallo al actualizar salud"));
                                })
                );
    }

    public Mono<Health> updateHealthWithoutHistory(Integer id, Health health) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("No se encontró salud con ID: " + id)))
                .flatMap(existingHealth ->
                        updateExistingHealth(existingHealth, health)
                                .onErrorResume(e -> {
                                    System.err.println("Error al actualizar salud sin historial: " + e.getMessage());
                                    return Mono.error(new RuntimeException("Fallo al actualizar sin historial"));
                                })
                );
    }

    private Mono<Health> updateExistingHealth(Health existingHealth, Health health) {
        existingHealth.setVaccine(health.getVaccine());
        existingHealth.setVph(health.getVph());
        existingHealth.setInfluenza(health.getInfluenza());
        existingHealth.setDeworming(health.getDeworming());
        existingHealth.setApplicationDate(health.getApplicationDate());
        existingHealth.setCondicionBeneficiary(health.getCondicionBeneficiary());
        existingHealth.setHemoglobin(health.getHemoglobin());

        return repository.save(existingHealth);
    }
}
