package kz.sabyrzhan.resources;

import io.quarkus.runtime.annotations.RegisterForReflection;
import kz.sabyrzhan.entities.CategoryEntity;
import kz.sabyrzhan.entities.OrderEntity;
import kz.sabyrzhan.entities.ProductEntity;
import kz.sabyrzhan.entities.StoreConfigEntity;
import kz.sabyrzhan.model.*;

@RegisterForReflection(targets = {
        UserRole.class, CategoryEntity.class, StoreConfigEntity.class, PaymentType.class,
        ErrorResponse.class, OrderEntity.class, InvoiceResult.class, Product.class, ProductEntity.class,
        User.class,
})
public class ReflectionConfiguration {
}
