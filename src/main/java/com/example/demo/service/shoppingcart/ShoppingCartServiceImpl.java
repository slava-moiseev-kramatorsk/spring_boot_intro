package com.example.demo.service.shoppingcart;

import com.example.demo.dto.cartitem.CartItemRequestDto;
import com.example.demo.dto.shoppingcart.ShoppingCartDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.mapper.CartItemMapper;
import com.example.demo.mapper.ShoppingCartMapper;
import com.example.demo.model.CartItem;
import com.example.demo.model.ShoppingCart;
import com.example.demo.model.User;
import com.example.demo.repository.book.BookRepository;
import com.example.demo.repository.cartitem.CartItemRepository;
import com.example.demo.repository.shoppingcart.ShoppingCartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    private final BookRepository bookRepository;
    private final ShoppingCartMapper shoppingCartMapper;

    @Override
    public ShoppingCartDto getShoppingCart(Long userId) {
        ShoppingCart shoppingCart = getCartByUser(userId);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartDto save(Long userId, CartItemRequestDto cartItemRequestDto) {
        ShoppingCart shoppingCart = getCartByUser(userId);
        CartItem cartItem = cartItemMapper.toModel(cartItemRequestDto);
        cartItem.setBook(bookRepository.findById(cartItemRequestDto.bookId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Can`t find book with id "
                        + cartItemRequestDto.bookId())));
        cartItem.setQuantity(cartItem.getQuantity());
        cartItem.setShoppingCart(shoppingCart);
        cartItemRepository.save(cartItem);
        shoppingCart.getCartItems().add(cartItem);
        return shoppingCartMapper.toDto(shoppingCartRepository.save(shoppingCart));
    }

    @Override
    public ShoppingCartDto update(
            Long userId,
            CartItemRequestDto cartItemRequestDto,
            Long cartItemId
    ) {
        isPresentBook(cartItemRequestDto);
        if (!cartItemRepository.existsById(cartItemId)) {
            throw new EntityNotFoundException("Can`t find cartItem with id "
            + cartItemId);
        }
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can`t find by id "
                                + cartItemId)
                );
        cartItemMapper.updateCartItems(cartItem, cartItemRequestDto);
        cartItemRepository.save(cartItem);
        return shoppingCartMapper.toDto(getCartByUser(userId));
    }

    @Override
    public void delete(Long userId, Long cartItemId) {
        ShoppingCart shoppingCart = getCartByUser(userId);
        CartItem cartItem = getCartItemById(cartItemId);
        shoppingCart.getCartItems().remove(cartItem);
        cartItem.setShoppingCart(shoppingCart);
        cartItemRepository.delete(cartItem);
    }

    @Override
    public void createCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);

    }

    private void isPresentBook(CartItemRequestDto cartItem) {
        if (!cartItemRepository.existsById(cartItem.bookId())) {
            throw new EntityNotFoundException("Can`t find book " + cartItem.bookId());
        }
    }

    private ShoppingCart getCartByUser(Long userId) {
        return shoppingCartRepository.findUserById(userId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can`t find cart by user id" + userId));
    }

    private CartItem getCartItemById(Long id) {
        return cartItemRepository.findById(id)
               .orElseThrow(() ->
                       new EntityNotFoundException("can`t find item with id " + id));

    }
}
