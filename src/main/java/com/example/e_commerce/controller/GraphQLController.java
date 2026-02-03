package com.example.e_commerce.controller;

import com.example.e_commerce.dto.RequestDto.ProductQueryParams;
import com.example.e_commerce.dto.ResponseDto.PagedResponse;
import com.example.e_commerce.dto.ResponseDto.ProductWithCategory;
import com.example.e_commerce.dto.ResponseDto.ProductWithCategoryAndStock;
import com.example.e_commerce.model.Order;
import com.example.e_commerce.model.OrderItem;
import com.example.e_commerce.model.Review;
import com.example.e_commerce.service.OrderItemService;
import com.example.e_commerce.service.OrderService;
import com.example.e_commerce.service.ProductService;
import com.example.e_commerce.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class GraphQLController {

    private final ProductService productService;
    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final ReviewService reviewService;

    @QueryMapping
    public PagedResponse<ProductWithCategory> products(
            @Argument Integer page,
            @Argument Integer size,
            @Argument Integer categoryId,
            @Argument String q,
            @Argument Double minPrice,
            @Argument Double maxPrice,
            @Argument String sortBy,
            @Argument String sortDir
    ) {
        ProductQueryParams params = new ProductQueryParams();
        params.setPage(page != null ? page : 0);
        params.setSize(size != null ? size : 20);
        params.setCategoryId(categoryId);
        params.setQ(q);
        params.setMinPrice(minPrice != null ? BigDecimal.valueOf(minPrice) : null);
        params.setMaxPrice(maxPrice != null ? BigDecimal.valueOf(maxPrice) : null);
        params.setSortBy(sortBy != null ? sortBy : "name");
        params.setSortDir(sortDir != null ? sortDir : "asc");
        return productService.getAllProductsWithCategory(params);
    }

    @QueryMapping
    public ProductWithCategoryAndStock product(@Argument int id) {
        return productService.getProductByIdWithCategoryAndStock(id);
    }

    @QueryMapping
    public Order order(@Argument int id) {
        return orderService.getOrderById(id);
    }

    @QueryMapping
    public List<Order> ordersByUser(@Argument int userId) {
        return orderService.getOrdersByUserId(userId);
    }

    @SchemaMapping(typeName = "Order", field = "items")
    public List<OrderItem> orderItems(Order order) {
        return orderItemService.getOrderItemsByOrderId(order.getOrder_id());
    }

    @QueryMapping
    public Review review(@Argument int id) {
        return reviewService.getReviewById(id);
    }

    @QueryMapping
    public List<Review> reviewsByProduct(@Argument int productId) {
        return reviewService.getReviewsByProductId(productId);
    }

    @QueryMapping
    public List<Review> reviewsByUser(@Argument int userId) {
        return reviewService.getReviewsByUserId(userId);
    }

    @QueryMapping
    public List<Review> reviews() {
        return reviewService.getAllReviews();
    }

    @MutationMapping
    public boolean createReview(
            @Argument int userId,
            @Argument int productId,
            @Argument int rating,
            @Argument String comment
    ) {
        reviewService.createReview(userId, productId, rating, comment);
        return true;
    }

    @MutationMapping
    public Review updateReview(
            @Argument int id,
            @Argument Integer rating,
            @Argument String comment
    ) {
        return reviewService.updateReview(id, rating != null ? rating : 0, comment);
    }

    @MutationMapping
    public boolean deleteReview(@Argument int id) {
        reviewService.deleteReview(id);
        return true;
    }
}
