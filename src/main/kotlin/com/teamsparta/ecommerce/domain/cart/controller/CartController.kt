package com.teamsparta.ecommerce.domain.cart.controller

import com.teamsparta.ecommerce.domain.cart.dto.AddItemToCartRequestDto
import com.teamsparta.ecommerce.domain.cart.dto.CartResponseDto
import com.teamsparta.ecommerce.domain.cart.dto.DeleteItemFromCartRequestDto
import com.teamsparta.ecommerce.domain.cart.dto.UpdateProductQuantityRequestDto
import com.teamsparta.ecommerce.domain.cart.service.CartService
import com.teamsparta.ecommerce.security.userdetails.UserDetailsImpl
import com.teamsparta.ecommerce.util.web.response.ListResponse
import com.teamsparta.ecommerce.util.web.response.SingleResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/cart")
class CartController(private val cartService: CartService) {

    @PostMapping("/add")
    fun addProductToCart(@RequestBody addItemToCartRequestDto: AddItemToCartRequestDto,
                      @AuthenticationPrincipal member : UserDetailsImpl
    ):ResponseEntity<SingleResponse<String>>{
        cartService.addProductToCart(member.getMemberId(), addItemToCartRequestDto)
        return ResponseEntity(SingleResponse.success(), HttpStatus.OK)
    }

    @PostMapping("/delete")
    fun deleteProductFromCart(@RequestBody deleteItemFromCartRequestDto: DeleteItemFromCartRequestDto,
                           @AuthenticationPrincipal member: UserDetailsImpl
    ):ResponseEntity<SingleResponse<String>>{
        cartService.deleteProductFromCart(member.getMemberId(), deleteItemFromCartRequestDto)
        return ResponseEntity(SingleResponse.success(), HttpStatus.OK)
    }

    @PostMapping("/update")
    fun updateProductQuantity(@RequestBody updateProductQuantityRequestDto: UpdateProductQuantityRequestDto,
                              @AuthenticationPrincipal member: UserDetailsImpl
    ):ResponseEntity<SingleResponse<String>>{
        cartService.updateProductQuantity(member.getMemberId(), updateProductQuantityRequestDto)
        return ResponseEntity(SingleResponse.success(), HttpStatus.OK)
    }

    @GetMapping("/list")
    fun getProductListFromCart(@AuthenticationPrincipal member: UserDetailsImpl
    ):ResponseEntity<ListResponse<CartResponseDto>>{
        val response = ListResponse(
            message = "Successfully retrieved the cart contents",
            data = cartService.getProductListFromCart(member.getMemberId())
        )

        return ResponseEntity(response, HttpStatus.OK)
    }

}