package kz.sabyrzhan.entities;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import kz.sabyrzhan.model.ConfigKey;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "pos_store_configs")
@Getter
@Setter
public class StoreConfigEntity extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "config_key")
    @Enumerated(EnumType.STRING)
    private ConfigKey configKey;

    @Column(name = "config_value")
    private String configValue;

    @Column
    private Instant created = Instant.now();

    @Column
    private Instant updated = Instant.now();
}
