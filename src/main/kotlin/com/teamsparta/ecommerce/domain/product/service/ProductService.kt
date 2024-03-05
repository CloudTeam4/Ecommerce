package com.teamsparta.ecommerce.domain.product.service

import com.teamsparta.ecommerce.domain.member.repository.MemberRepository
import com.teamsparta.ecommerce.domain.product.dto.ProductDto
import com.teamsparta.ecommerce.domain.product.model.Product
import com.teamsparta.ecommerce.domain.product.repository.ProductRepository
import com.teamsparta.ecommerce.exception.BadRequestException
import com.teamsparta.ecommerce.exception.NotFoundException
import com.teamsparta.ecommerce.util.enum.Role
import com.teamsparta.ecommerce.util.web.request.ProductCreateRequest
import com.teamsparta.ecommerce.util.web.request.ProductUpdateRequest
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class ProductService(
    private val productRepository: ProductRepository,
    private val memberRepository: MemberRepository
) {

    /**
     * 상품 등록
     * */
    @Transactional
    fun addItem(memberId: Long, request: ProductCreateRequest, itemId: Long): Product {
        val member = memberRepository.findById(memberId).orElseThrow { NotFoundException("판매자 정보를 찾을 수 없습니다.") }
        if(member.role != Role.SELLER) {
            throw BadRequestException("판매자만 상품을 등록할 수 있습니다.")
        }
        val product = Product(
            category = request.category,
            name = request.name,
            explanation = request.explanation,
            price = request.price,
        )
        return productRepository.save(product)
    }

    /**
     * 상품 전체 조회
     * */
    fun getAllItems(): List<ProductDto> {
        val items = productRepository.findAll()
        return ProductDto.fromEntities(items)
    }

    /**
     * 특정 삼품 조회
     * */
    fun getItemById(itemId: Long): ProductDto {
        val item = productRepository.findById(itemId).orElseThrow { NotFoundException("해당 상품을 찾을 수 없습니다.") }
        return ProductDto.fromEntity(item)
    }

    /**
     * 상품 수정
     * */
    @Transactional
    fun updateItem(memberId: Long, request: ProductUpdateRequest, itemId: Long): Product {
        val member = memberRepository.findById(memberId).orElseThrow { NotFoundException("판매자 정보를 찾을 수 없습니다.") }
        val item = productRepository.findById(itemId).orElseThrow { NotFoundException("해당 상품을 찾을 수 없습니다.") }

        if(member.role != Role.SELLER) {
            throw BadRequestException("판매자만 상품 정보를 수정할 수 있습니다.")
        }

        item.update(
            request.category,
            request.name,
            request.explanation,
            request.price
        )

        return item
    }

    /**
     * 상품 삭제
     * */
    @Transactional
    fun deleteItem(memberId: Long, itemId: Long) {
        val member = memberRepository.findById(memberId).orElseThrow { NotFoundException("판매자 정보를 찾을 수 없습니다.") }
        val item = productRepository.findById(itemId).orElseThrow { NotFoundException("해당 상품을 찾을 수 없습니다.") }

        if(member.role != Role.SELLER) {
            throw BadRequestException("판매자만 상품을 삭제할 수 있습니다.")
        }
        
        productRepository.delete(item)
    }



}