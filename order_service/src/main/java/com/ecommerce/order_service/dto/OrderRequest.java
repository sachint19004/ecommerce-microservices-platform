package com.ecommerce.order_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    // We replaced the single fields with this list:
    private List<OrderLineItemsDto> orderLineItemsDtoList;
    private Long userId; // We still keep the user ID
}