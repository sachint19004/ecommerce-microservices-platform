package com.ecommerce.order_service.service;

import com.ecommerce.order_service.client.ProductClient;
import com.ecommerce.order_service.dto.OrderLineItemsDto;
import com.ecommerce.order_service.dto.OrderRequest;
import com.ecommerce.order_service.dto.ProductDto;
import com.ecommerce.order_service.model.Order;
import com.ecommerce.order_service.model.OrderLineItems;
import com.ecommerce.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;

    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setUserId(orderRequest.getUserId());

        List<OrderLineItems> orderLineItems = new ArrayList<>();
        
        // 1. Initialize Total Counter as a simple double
        double totalAmount = 0.0; 

        for (OrderLineItemsDto itemDto : orderRequest.getOrderLineItemsDtoList()) {
            
            ProductDto productDto = productClient.getProductById(itemDto.getProductId());
            productClient.reduceQuantity(itemDto.getProductId(), itemDto.getQuantity());

            OrderLineItems lineItem = new OrderLineItems();
            lineItem.setProductId(itemDto.getProductId());
            lineItem.setQuantity(itemDto.getQuantity());
            
            // 2. Set Price directly (Double to Double)
            double price = productDto.getPrice();
            lineItem.setPrice(price); 
            
            orderLineItems.add(lineItem);

            // 3. Simple Math Calculation
            double itemCost = price * itemDto.getQuantity();
            totalAmount += itemCost;
        }

        // 4. Set the Total (Double to Double)
        order.setTotalPrice(totalAmount);

        order.setOrderLineItemsList(orderLineItems);
        orderRepository.save(order);
    }
} 