package pe.edu.vallegrande.health.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.health.model.Health;
import pe.edu.vallegrande.health.service.HealthService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/health")
public class HealthController {

    @Autowired
    private HealthService service;

    @GetMapping
    public Flux<Health> getAll() {
        return service.findAll();
    }

    @GetMapping("/person/{personId}")
    public Flux<Health> getHealthByPersonId(@PathVariable Integer personId) {
        return service.getByPersonId(personId);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Health>> getById(@PathVariable Integer id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<Health> create(@RequestBody Health health) {
        return service.save(health);
    }

    @PutMapping("/update-with-history/{id}")
    public Mono<ResponseEntity<Health>> updateWithHistory(@PathVariable Integer id, @RequestBody Health health) {
        return service.existsById(id)
                .flatMap(exists -> {
                    if (exists) {
                        health.setIdHealth(id);
                        return service.updateHealthWithHistory(id, health)
                                .map(ResponseEntity::ok);
                    } else {
                        return Mono.just(ResponseEntity.notFound().build());
                    }
                });
    }

    @PutMapping("/update/{id}")
    public Mono<ResponseEntity<Health>> updateWithoutHistory(@PathVariable Integer id, @RequestBody Health health) {
        return service.existsById(id)
                .flatMap(exists -> {
                    if (exists) {
                        health.setIdHealth(id);
                        return service.updateHealthWithoutHistory(id, health)
                                .map(ResponseEntity::ok);
                    } else {
                        return Mono.just(ResponseEntity.notFound().build());
                    }
                });
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable Integer id) {
        return service.existsById(id)
                .flatMap(exists -> {
                    if (exists) {
                        return service.deleteById(id)
                                .thenReturn(ResponseEntity.noContent().build());
                    } else {
                        return Mono.just(ResponseEntity.notFound().build());
                    }
                });
    }
}
