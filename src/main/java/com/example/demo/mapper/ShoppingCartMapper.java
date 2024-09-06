package com.example.demo.mapper;

import com.example.demo.config.MapperConfig;
import com.example.demo.dto.shoppingcart.ShoppingCartDto;
import com.example.demo.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = CartItemMapper.class)
public interface ShoppingCartMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "cartItemSet", source = "cartItems")
    ShoppingCartDto toDto(ShoppingCart shoppingCart);

}
