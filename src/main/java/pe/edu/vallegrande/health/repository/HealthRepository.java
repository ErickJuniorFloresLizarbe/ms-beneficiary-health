package pe.edu.vallegrande.health.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import pe.edu.vallegrande.health.model.Health;
import reactor.core.publisher.Flux;

public interface HealthRepository extends ReactiveCrudRepository<Health, Integer> {
    Flux<Health> findByPersonId(Integer personId);
}
