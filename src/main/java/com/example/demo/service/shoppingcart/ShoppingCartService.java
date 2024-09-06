package com.example.demo.service.shoppingcart;

import com.example.demo.dto.cartitem.CartItemRequestDto;
import com.example.demo.dto.shoppingcart.ShoppingCartDto;
import com.example.demo.model.User;

public interface ShoppingCartService {
    ShoppingCartDto getShoppingCart(Long userId);

    ShoppingCartDto save(Long userId, CartItemRequestDto cartItemRequestDto);

    ShoppingCartDto update(Long userId, CartItemRequestDto cartItemRequestDto, Long cartItemId);

    void delete(Long userId, Long cartItemId);

    void createCart(User user);
}
