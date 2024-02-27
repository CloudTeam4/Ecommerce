package com.teamsparta.ecommerce.domain.cart.service

import com.teamsparta.ecommerce.domain.cart.dto.AddItemToCartRequestDto
import com.teamsparta.ecommerce.domain.cart.dto.CartResponseDto
import com.teamsparta.ecommerce.domain.cart.dto.DeleteItemFromCartRequestDto
import com.teamsparta.ecommerce.domain.cart.model.CartItem
import com.teamsparta.ecommerce.domain.cart.repository.CartItemRepository
import com.teamsparta.ecommerce.domain.cart.repository.CartRepository
import com.teamsparta.ecommerce.domain.product.repository.ProductRepository
import com.teamsparta.ecommerce.exception.NotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CartService(
    private val cartRepository: CartRepository,
    private val cartItemRepository: CartItemRepository,
    private val productRepository: ProductRepository
) {
    @Transactional
    fun addProductToCart(memberId: Long, addItemToCartRequestDto: AddItemToCartRequestDto) {
        val cart = cartRepository.findByMemberId(memberId).orElseThrow {
            NotFoundException("The Cart was not found for provided Member")
        }
        addItemToCartRequestDto.items.forEach {
            cartItemRepository.save(
                CartItem(
                    product = productRepository
                        .findById(it.productId)
                        .orElseThrow {
                            throw NotFoundException("The Product was not found for provided id")
                        },
                    quantity = it.quantity,
                    cart = cart
                )
            )
        }

    }

    @Transactional
    fun deleteProductFromCart(memberId: Long, deleteItemToCartRequestDto: DeleteItemFromCartRequestDto) {
        val cart = cartRepository.findByMemberId(memberId).orElseThrow {
            NotFoundException("The Cart was not found for provided id")
        }
        val deleteRequestIdList = deleteItemToCartRequestDto.productIdList.toSet()

        cart.cartItemList.removeIf { cartItem ->
            cartItem.product.itemId in deleteRequestIdList
        }
        cartRepository.save(cart)
    }

    @Transactional(readOnly = true)
    fun getProductListFromCart(memberId: Long): List<CartResponseDto> {
        val cart = cartRepository.findByMemberId(memberId).orElseThrow {
            NotFoundException("The Cart was not found for provided id")
        }

        return cart.cartItemList.map {
            CartResponseDto(productId = it.id!!, quantity = it.quantity)
        }.toList()
    }

}