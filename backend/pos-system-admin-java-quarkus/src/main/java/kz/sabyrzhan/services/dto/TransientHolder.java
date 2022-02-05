package kz.sabyrzhan.services.dto;

import kz.sabyrzhan.entities.OrderEntity;
import kz.sabyrzhan.entities.ProductEntity;
import kz.sabyrzhan.entities.StoreConfigEntity;
import kz.sabyrzhan.model.ConfigKey;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
public class TransientHolder {
    private List<ProductEntity> productsEntities = new ArrayList<>();
    private Map<ConfigKey, StoreConfigEntity> configs = new ConcurrentHashMap<>();
    private OrderEntity orderEntity;

    public void putConfig(ConfigKey key, StoreConfigEntity entity) {
        configs.put(key, entity);
    }

    public StoreConfigEntity getConfig(ConfigKey key) {
        return configs.get(key);
    }

    public Map<Integer, ProductEntity> getProductsAsMap() {
        return productsEntities.stream().collect(Collectors.toMap(ProductEntity::getId, Function.identity()));
    }
}
