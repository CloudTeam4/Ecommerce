package com.teamsparta.ecommerce.domain.product.controller

import com.teamsparta.ecommerce.domain.product.dto.ProductDto
import com.teamsparta.ecommerce.domain.product.service.ProductService
import com.teamsparta.ecommerce.security.userdetails.UserDetailsImpl
import com.teamsparta.ecommerce.util.web.request.ProductCreateRequest
import com.teamsparta.ecommerce.util.web.request.ProductUpdateRequest
import com.teamsparta.ecommerce.util.web.response.ListResponse
import com.teamsparta.ecommerce.util.web.response.SingleResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid
import org.springframework.web.multipart.MultipartFile as MultipartFile1

@RestController
@RequestMapping("/api/items")
class ProductController(
    private val productService: ProductService
) {

    // 상품 등록
    @PostMapping
    fun createMenu(
        @AuthenticationPrincipal user: UserDetailsImpl,
        @Valid @RequestPart("request") request: ProductCreateRequest,
        @RequestPart("image") image: MultipartFile1 // 이미지 파일을 받는 부분
    ): ResponseEntity<SingleResponse<ProductDto>> {
            val item = productService.addItem(user.getMemberId(), request, image)
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(SingleResponse.successOf("상품 등록에 성공했습니다!", ProductDto.fromEntity(item)))
        }
//    }


    // 상품 전체 조회
    @GetMapping
    fun findAllItems(): ResponseEntity<ListResponse<ProductDto>> {
        val menus = productService.getAllItems()
        return ResponseEntity(ListResponse.successOf(menus), HttpStatus.OK)
    }

    // 특정 상품 조회
    @GetMapping("/{itemId}")
    fun findItem(@PathVariable itemId: Long): ResponseEntity<SingleResponse<ProductDto>> {
        val item = productService.getItemById(itemId)
        return ResponseEntity(SingleResponse.successOf(item), HttpStatus.OK)
    }

    // 상품 수정
    @PutMapping("/{itemId}")
    fun updateItem(
        @AuthenticationPrincipal user: User,
        @PathVariable itemId: Long,
        @Valid @RequestBody request: ProductUpdateRequest
    ): ResponseEntity<SingleResponse<ProductDto>> {
        val item = productService.updateItem(user.username.toLong(), request, itemId)
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(SingleResponse.successOf("상품 수정에 성공했습니다.", ProductDto.fromEntity(item)))
    }


    // 상품 삭제
    @DeleteMapping("/{itemId}")
    fun deleteItem(
        @AuthenticationPrincipal user: User,
        @PathVariable itemId: Long,
    ): ResponseEntity<SingleResponse<String>> {
        productService.deleteItem(user.username.toLong(), itemId)
        return ResponseEntity(SingleResponse.success(), HttpStatus.OK)
    }
}