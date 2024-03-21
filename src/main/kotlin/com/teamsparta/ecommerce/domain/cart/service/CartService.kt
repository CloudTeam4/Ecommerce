package com.teamsparta.ecommerce.domain.cart.service

import com.teamsparta.ecommerce.domain.cart.dto.AddItemToCartRequestDto
import com.teamsparta.ecommerce.domain.cart.dto.CartResponseDto
import com.teamsparta.ecommerce.domain.cart.dto.DeleteItemFromCartRequestDto
import com.teamsparta.ecommerce.domain.cart.dto.UpdateProductQuantityRequestDto
import com.teamsparta.ecommerce.domain.cart.model.Cart
import com.teamsparta.ecommerce.domain.cart.model.CartItem
import com.teamsparta.ecommerce.domain.cart.repository.CartItemRepository
import com.teamsparta.ecommerce.domain.cart.repository.CartRepository
import com.teamsparta.ecommerce.domain.member.repository.MemberRepository
import com.teamsparta.ecommerce.domain.product.repository.ProductRepository
import com.teamsparta.ecommerce.exception.NotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CartService(
    private val cartRepository: CartRepository,
    private val cartItemRepository: CartItemRepository,
    private val productRepository: ProductRepository,
    private val memberRepository: MemberRepository
) {
    @Transactional
    fun addProductToCart(memberId: Long, addItemToCartRequestDto: AddItemToCartRequestDto) {

        val cart = findCartByMemberId(memberId)

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
        val cart = findCartByMemberId(memberId)

        val deleteRequestIdList = deleteItemToCartRequestDto.productIdList.toSet()

        cart.cartItemList.removeIf { cartItem ->
            cartItem.product.itemId in deleteRequestIdList
        }
        cartRepository.save(cart)
    }

    @Transactional
    fun updateProductQuantity(memberId: Long, updateProductQuantityRequestDto: UpdateProductQuantityRequestDto){

        val cart = findCartByMemberId(memberId)
        val cartItemList = cart.cartItemList

        val targetItem = cartItemList.find { it.product.itemId == updateProductQuantityRequestDto.productId }
            ?: throw NotFoundException("Product not found in the cart")

        targetItem.quantity = updateProductQuantityRequestDto.quantity

        cartItemRepository.save(targetItem)

    }

    @Transactional(readOnly = true)
    fun getProductListFromCart(memberId: Long): List<CartResponseDto> {
        val cart = findCartByMemberId(memberId)
        return cart.cartItemList.map {
            CartResponseDto(productId = it.id!!, quantity = it.quantity)
        }.toList()
    }

    private fun findCartByMemberId(memberId: Long) : Cart{
        val member = memberRepository.findById(memberId).orElseThrow {
            throw NotFoundException("The Member was not found for provided id")
        }
        return member.cart?:throw NotFoundException("The Cart was not found for provided Member")

    }

}