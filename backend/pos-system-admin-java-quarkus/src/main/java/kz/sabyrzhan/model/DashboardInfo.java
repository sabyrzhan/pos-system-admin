package kz.sabyrzhan.model;

import lombok.Data;

import java.util.Map;

@Data
public class DashboardInfo {
    private long totalNewOrders;
    private long totalInProgressOrders;
    private long totalProcessedOrders;
    private long totalCancelledOrders;
    private long totalReturnedOrders;
    private long totalProducts;
    private Map<String, Long> topProducts;
}
