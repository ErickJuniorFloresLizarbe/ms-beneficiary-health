package pe.edu.vallegrande.health.serviceTest;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import pe.edu.vallegrande.health.model.Health;
import pe.edu.vallegrande.health.repository.HealthRepository;
import pe.edu.vallegrande.health.service.HealthService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class HealthServiceTest {

    @Mock
    private HealthRepository repository;

    @InjectMocks
    private HealthService service;

    private Health testHealth;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        testHealth = new Health();
        testHealth.setIdHealth(1);
        testHealth.setVaccine("Vacuna A");
        testHealth.setPersonId(1001);
        // Agrega otros atributos necesarios según tu modelo
    }

    // LISTA TODOS LOS DATOS DE HEALTH
    @Test
    void testFindAll() {
        when(repository.findAll()).thenReturn(Flux.just(testHealth));

        StepVerifier.create(service.findAll())
                .expectNext(testHealth)
                .verifyComplete();
    }

    // LISTA POR ID LA HEALTH
    @Test
    void testFindById() {
        when(repository.findById(1)).thenReturn(Mono.just(testHealth));

        StepVerifier.create(service.findById(1))
                .expectNext(testHealth)
                .verifyComplete();
    }

    // VERIFICA SI EXISTE EL ID DE LA HEALTH
    @Test
    void testExistsById() {
        when(repository.existsById(1)).thenReturn(Mono.just(true));

        StepVerifier.create(service.existsById(1))
                .expectNext(true)
                .verifyComplete();
    }

    // LISTA (UNO O MUCHOS DATOS) DE HEALTH DE UNA PERSONA
    @Test
    void testGetByPersonId() {
        when(repository.findByPersonId(1001)).thenReturn(Flux.just(testHealth));

        StepVerifier.create(service.getByPersonId(1001))
                .expectNextMatches(h -> h.getPersonId().equals(1001))
                .verifyComplete();
    }

    // ELIMINA UN REGISTRO DE HEALTH POR ID
    @Test
    void testDeleteById() {
        when(repository.deleteById(1)).thenReturn(Mono.empty());

        StepVerifier.create(service.deleteById(1))
                .verifyComplete();

        verify(repository, times(1)).deleteById(1);
    }
}
