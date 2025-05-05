package com.example.demo.mapper;

import com.example.demo.config.MapperConfig;
import com.example.demo.dto.cartitem.CartItemDto;
import com.example.demo.dto.cartitem.CartItemRequestDto;
import com.example.demo.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = BookMapper.class)
public interface CartItemMapper {
    @Mapping(target = "book", source = "bookId", qualifiedByName = "bookFromId")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "shoppingCart", ignore = true)
    CartItem toModel(CartItemRequestDto cartItemRequestDto);

    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "bookTitle", source = "book.title")
    CartItemDto toDto(CartItem cartItem);

    void updateCartItems(@MappingTarget CartItem cartItem, CartItemRequestDto cartItemDto);
}
