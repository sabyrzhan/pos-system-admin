package kz.sabyrzhan.repositories;

import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.smallrye.mutiny.Uni;
import kz.sabyrzhan.entities.StoreConfigEntity;
import kz.sabyrzhan.exceptions.EntityNotFoundException;
import kz.sabyrzhan.model.ConfigKey;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class StoreConfigRepository implements PanacheRepositoryBase<StoreConfigEntity, Integer> {
    public Uni<StoreConfigEntity> findByConfigKey(ConfigKey configKey) {
        return find("config_key = ?1", configKey.name()).singleResult()
                .onItemOrFailure().transformToUni((storeConfigEntity, throwable) -> {
                    if (throwable != null) {
                        return Uni.createFrom().failure(new EntityNotFoundException("Config with key=" + configKey + " not found."));
                    }

                    return Uni.createFrom().item(storeConfigEntity);
                });
    }
}
