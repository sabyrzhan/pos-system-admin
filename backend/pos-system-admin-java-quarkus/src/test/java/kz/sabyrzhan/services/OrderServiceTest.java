package kz.sabyrzhan.services;

import kz.sabyrzhan.entities.OrderEntity;
import kz.sabyrzhan.entities.OrderItemEntity;
import kz.sabyrzhan.entities.ProductEntity;
import kz.sabyrzhan.entities.StoreConfigEntity;
import kz.sabyrzhan.model.ConfigKey;
import kz.sabyrzhan.model.PaymentType;
import kz.sabyrzhan.services.dto.TransientHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class OrderServiceTest {
    OrderService orderService = new OrderService();

    ProductEntity product1, product2, product3;
    StoreConfigEntity taxConfig;
    TransientHolder transientHolder;

    @BeforeEach
    void setUp() {
        product1 = new ProductEntity();
        product1.setId(1);
        product1.setCategoryId(1);
        product1.setStock(10);
        product1.setName("Product1");
        product1.setDescription("Product1 desc");
        product1.setSalePrice(100);

        product2 = new ProductEntity();
        product2.setId(2);
        product2.setCategoryId(2);
        product2.setStock(20);
        product2.setName("Product2");
        product2.setDescription("Product2 desc");
        product2.setSalePrice(200);

        product3 = new ProductEntity();
        product3.setId(3);
        product3.setCategoryId(3);
        product3.setStock(30);
        product3.setName("Product3");
        product3.setDescription("Product3 desc");
        product3.setSalePrice(300);

        taxConfig = new StoreConfigEntity();
        taxConfig.setId(1);
        taxConfig.setConfigKey(ConfigKey.TAX_PERCENT);
        taxConfig.setConfigValue("5");

        transientHolder = new TransientHolder();
        transientHolder.putConfig(ConfigKey.TAX_PERCENT, taxConfig);
        transientHolder.setProductsEntities(List.of(product1, product2, product3));
    }

    @Test
    void validateOrderEntity_success() {
        OrderEntity orderEntity = createOrder();
        orderEntity.setPaid(800);
        orderEntity.setTax(30);
        orderEntity.setDiscount(20);
        orderEntity.setDue(190);
        orderEntity.setSubtotal(600);
        orderEntity.setTotal(610);
        orderEntity.setCustomerName("CustomerName");
        orderEntity.setPaymentType(PaymentType.CREDIT);

        orderService.validateOrderEntity(orderEntity, taxConfig, transientHolder);
    }

    @Test
    void validateOrderEntity_noDiscount() {
        OrderEntity orderEntity = createOrder();
        orderEntity.setPaid(800);
        orderEntity.setTax(30);
        orderEntity.setDiscount(0);
        orderEntity.setDue(170);
        orderEntity.setSubtotal(600);
        orderEntity.setTotal(630);
        orderEntity.setCustomerName("CustomerName");
        orderEntity.setPaymentType(PaymentType.CREDIT);

        orderService.validateOrderEntity(orderEntity, taxConfig, transientHolder);
    }

    @Test
    void validateOrderEntity_invalidTax() {
        OrderEntity orderEntity = createOrder();
        orderEntity.setPaid(800);
        orderEntity.setTax(10);
        orderEntity.setDiscount(0);
        orderEntity.setDue(190);
        orderEntity.setSubtotal(600);
        orderEntity.setTotal(610);
        orderEntity.setCustomerName("CustomerName");
        orderEntity.setPaymentType(PaymentType.CREDIT);

        try {
            orderService.validateOrderEntity(orderEntity, taxConfig, transientHolder);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Tax is invalid", e.getMessage());
        }
    }

    @Test
    void validateOrderEntity_invalidSubtotal() {
        OrderEntity orderEntity = createOrder();
        orderEntity.setPaid(800);
        orderEntity.setTax(25);
        orderEntity.setDiscount(20);
        orderEntity.setDue(295);
        orderEntity.setSubtotal(500);
        orderEntity.setTotal(505);
        orderEntity.setCustomerName("CustomerName");
        orderEntity.setPaymentType(PaymentType.CREDIT);

        try {
            orderService.validateOrderEntity(orderEntity, taxConfig, transientHolder);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Subtotal is invalid", e.getMessage());
        }
    }

    @Test
    void validateOrderEntity_invalidTotal() {
        OrderEntity orderEntity = createOrder();
        orderEntity.setPaid(800);
        orderEntity.setTax(30);
        orderEntity.setDiscount(20);
        orderEntity.setDue(100);
        orderEntity.setSubtotal(600);
        orderEntity.setTotal(700);
        orderEntity.setCustomerName("CustomerName");
        orderEntity.setPaymentType(PaymentType.CREDIT);

        try {
            orderService.validateOrderEntity(orderEntity, taxConfig, transientHolder);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Total is invalid", e.getMessage());
        }
    }

    @Test
    void validateOrderEntity_invalidDue() {
        OrderEntity orderEntity = createOrder();
        orderEntity.setPaid(800);
        orderEntity.setTax(30);
        orderEntity.setDiscount(20);
        orderEntity.setDue(100);
        orderEntity.setSubtotal(600);
        orderEntity.setTotal(610);
        orderEntity.setCustomerName("CustomerName");
        orderEntity.setPaymentType(PaymentType.CREDIT);

        try {
            orderService.validateOrderEntity(orderEntity, taxConfig, transientHolder);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Due is invalid", e.getMessage());
        }
    }

    public OrderEntity createOrder() {
        OrderEntity orderEntity = new OrderEntity();

        OrderItemEntity item1 = new OrderItemEntity();
        item1.setProductId(product1.getId());
        item1.setProductName(product1.getName());
        item1.setPrice(product1.getSalePrice());
        item1.setQuantity(1);

        OrderItemEntity item2 = new OrderItemEntity();
        item2.setProductId(product2.getId());
        item2.setProductName(product2.getName());
        item2.setPrice(product2.getSalePrice());
        item2.setQuantity(1);

        OrderItemEntity item3 = new OrderItemEntity();
        item3.setProductId(product3.getId());
        item3.setProductName(product3.getName());
        item3.setPrice(product3.getSalePrice());
        item3.setQuantity(1);

        orderEntity.getItems().add(item1);
        orderEntity.getItems().add(item2);
        orderEntity.getItems().add(item3);

        return orderEntity;
    }
}