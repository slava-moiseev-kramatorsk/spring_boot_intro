package com.example.demo.controller;

import com.example.demo.dto.cartitem.CartItemRequestDto;
import com.example.demo.dto.shoppingcart.ShoppingCartDto;
import com.example.demo.model.User;
import com.example.demo.service.shoppingcart.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Cart management",
        description = "Endpoints for management shopping cart")
@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    @Operation(summary = "Get shoppingCart",
            description = "Get shoppingCart for current user")
    @PreAuthorize("hasRole('USER')")
    public ShoppingCartDto getShoppingCart(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.getShoppingCart(user.getId());
    }

    @PostMapping
    @Operation(summary = "Add new cartItems",
            description = "Add new cartItems to shoppingCart")
    @PreAuthorize("hasRole('USER')")
    public ShoppingCartDto addCartItems(
            Authentication authentication,
            @RequestBody CartItemRequestDto cartItemRequestDto
    ) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.save(user.getId(), cartItemRequestDto);
    }

    @PutMapping("/items/{cartItemId}")
    @Operation(summary = "Update cartItems",
            description = "Update cartItems in shoppingCart")
    @PreAuthorize("hasRole('USER')")
    public ShoppingCartDto updateItems(
            Authentication authentication,
            CartItemRequestDto cartItemRequestDto,
            Long cartItemId
    ) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.update(user.getId(), cartItemRequestDto, cartItemId);
    }

    @DeleteMapping("/items/{cartItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete cartItems from shoppingCart",
            description = "Delete cartItems from shoppingCart")
    @PreAuthorize("hasRole('USER')")
    public void deleteCartItem(@PathVariable Long cartItemId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        shoppingCartService.delete(cartItemId, user.getId());
    }
}
