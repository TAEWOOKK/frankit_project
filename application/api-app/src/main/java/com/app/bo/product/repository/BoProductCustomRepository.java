package com.app.bo.product.repository;

import com.app.bo.product.constant.ProductKeywordType;
import com.app.bo.product.constant.ProductOrderType;
import com.app.bo.product.dto.request.ProductGetRequestDto;
import com.app.bo.product.dto.response.ProductGetResponseDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.app.domain.product.entity.QProduct.product;
import static com.querydsl.core.types.Order.DESC;

@Repository
@RequiredArgsConstructor
public class BoProductCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Page<ProductGetResponseDto> findByFilter(ProductGetRequestDto request){

        // 페이징 처리
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

        // 검색 조건
        BooleanBuilder whereBuilder = new BooleanBuilder();
        setKeyword(request.getProductKeywordType(), request.getKeyword(), whereBuilder);

        // 정렬 조건
        OrderSpecifier[] order = setOrder(request.getProductOrderType(), request.getOrder());

        // 검색 갯수
        Long total = jpaQueryFactory.select(product.count())
                .from(product)
                .where(whereBuilder)
                .fetchOne();

        // 검색 결과
        List<ProductGetResponseDto> response = jpaQueryFactory.select(
            Projections.constructor(
                ProductGetResponseDto.class,
                product.productId,
                product.productName,
                product.productDescription,
                product.productPrice,
                product.deliveryPrice,
                product.createTime
            ))
            .from(product)
            .where(whereBuilder)
            .orderBy(order)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        // 결과를 Page로 변환
        return new PageImpl<>(response, pageable, total);
    }

    private void setKeyword(ProductKeywordType productKeywordType, String keyword, BooleanBuilder whereBuilder) {

        if(productKeywordType == null || keyword == null || keyword.trim().isEmpty()){
            return;
        }
        if (productKeywordType == ProductKeywordType.PRODUCT_DESCRIPTION) {
            whereBuilder.and(product.productDescription.containsIgnoreCase(keyword));
        } else {
            whereBuilder.and(product.productName.containsIgnoreCase(keyword));
        }
    }

    private OrderSpecifier[] setOrder(ProductOrderType productOrderType, Order order) {
        if(order == null) order = DESC;
        switch (productOrderType == null ? ProductOrderType.CREATE_TIME : productOrderType) {
            case PRODUCT_NAME:
                return new OrderSpecifier[]{new OrderSpecifier(order, product.productName)};
            default:
                return new OrderSpecifier[]{new OrderSpecifier(order, product.createTime)};
        }
    }
}
