package com.example.demo.service.order;

import com.example.demo.dto.order.OrderRequestDto;
import com.example.demo.dto.order.OrderResponseDto;
import com.example.demo.dto.order.OrderUpdateDto;
import com.example.demo.dto.orderitem.OrderItemDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.mapper.OrderItemMapper;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.model.CartItem;
import com.example.demo.model.Order;
import com.example.demo.model.OrderItem;
import com.example.demo.model.ShoppingCart;
import com.example.demo.model.User;
import com.example.demo.repository.book.BookRepository;
import com.example.demo.repository.order.OrderRepository;
import com.example.demo.repository.orderitem.OrderItemRepository;
import com.example.demo.repository.shoppingcart.ShoppingCartRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;
    private final BookRepository bookRepository;

    @Override
    @Transactional
    public OrderResponseDto createOrder(User user, OrderRequestDto requestDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(user.getId())
                .orElseThrow(
                        () -> new EntityNotFoundException("Can`t find by this user " + user));
        Order order = orderMapper.toModel(shoppingCart, requestDto);
        order.setUser(user);
        order.setStatus(Order.Status.PENDING);
        order.setOrderDate(LocalDateTime.now());
        Set<OrderItem> orderItemSet = shoppingCart.getCartItems()
                .stream()
                .map(cart -> mapToOrderItem(cart, order))
                .collect(Collectors.toSet());
        order.setOrderItems(orderItemSet);
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (OrderItem orderItem : orderItemSet) {
            totalPrice = totalPrice.add(orderItem.getPrice());
        }
        order.setTotal(totalPrice);
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public List<OrderResponseDto> findByUser(User user) {
        return orderRepository.findAllByUserId(user.getId()).stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public OrderResponseDto updateOrderStatus(Long orderId, OrderUpdateDto updateDto) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new EntityNotFoundException("Can`t find order by this id " + orderId));
        orderMapper.updateOrderStatus(updateDto, order);
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderItemDto getOrderItemFromOrderById(Long orderId, Long orderItemsId) {
        OrderItem orderItem = orderItemRepository.findByIdAndOrderId(orderId, orderItemsId)
                .orElseThrow(
                        () -> new EntityNotFoundException("can`t find orderItem by order id "
                        + orderId)
                );
        return orderItemMapper.toDto(orderItem);
    }

    private OrderItem mapToOrderItem(CartItem cartItem, Order order) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setBook(bookRepository.findById(cartItem.getBook().getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can`t find book by id: " + cartItem.getId())));
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setPrice(orderItem.getBook().getPrice()
                .multiply(new BigDecimal(orderItem.getQuantity())));
        return orderItem;
    }
}
