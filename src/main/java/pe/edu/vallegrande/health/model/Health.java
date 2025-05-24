package pe.edu.vallegrande.health.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@Table("health")
public class Health {
    @Id
    @Column("id_health")
    private Integer idHealth;

    @Column("vaccine_schemes")
    private String vaccine;

    @Column("vph")
    private String vph;

    @Column("influenza")
    private String influenza;

    @Column("deworming")
    private String deworming;

    @Column("hemoglobin")
    private String hemoglobin;

    @Column("application_date")
    private LocalDate applicationDate;

    
    @Column("condicion")
    private String condicionBeneficiary;

    @Column("person_id_person")
    private Integer personId;
}
