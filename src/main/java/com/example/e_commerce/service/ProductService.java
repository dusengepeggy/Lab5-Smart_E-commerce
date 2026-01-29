package com.example.e_commerce.service;

import com.example.e_commerce.dao.ProductDao;
import com.example.e_commerce.dto.RequestDto.AddProductRequest;
import com.example.e_commerce.dto.RequestDto.ProductQueryParams;
import com.example.e_commerce.dto.ResponseDto.PagedResponse;
import com.example.e_commerce.dto.ResponseDto.ProductWithCategory;
import com.example.e_commerce.dto.ResponseDto.ProductWithCategoryAndStock;
import com.example.e_commerce.model.Product;
import com.example.e_commerce.utils.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductDao productDao;

    public void createProduct(AddProductRequest request) {
        Product product = new Product();
        product.setCategory_id(request.getCategory_id());
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        productDao.createProduct(product);
    }

    public Product getProductById(int productId) {
        return productDao.getProductById(productId)
                .orElseThrow(() -> new NotFoundException("Product with ID " + productId + " not found"));
    }

    public List<Product> getAllProducts() {
        return productDao.getAllProducts();
    }

    public List<Product> getProductsByCategory(int categoryId) {
        return productDao.getProductsByCategory(categoryId);
    }

    public List<Product> searchProductsByName(String searchTerm) {
        return productDao.searchProductsByName(searchTerm);
    }

    public List<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return productDao.getProductsByPriceRange(minPrice, maxPrice);
    }

    public Product updateProduct(int productId, Product product) {
        int rows = productDao.updateProduct(productId, product);
        if (rows == 0) {
            throw new NotFoundException("Product not found");
        }
        return getProductById(productId);
    }

    public void deleteProduct(int productId) {
        int rows = productDao.deleteProduct(productId);
        if (rows == 0) {
            throw new NotFoundException("Product not found");
        }
    }

    public PagedResponse<ProductWithCategory> getAllProductsWithCategory(ProductQueryParams params) {
        int safePage = Math.max(params.getPage(), 0);
        int safeSize = params.getSize() <= 0 ? 20 : Math.min(params.getSize(), 100);
        int offset = safePage * safeSize;
        return productDao.getAllProductsWithCategory(params, safeSize, offset);
    }

    public List<ProductWithCategory> getProductsByCategoryWithCategory(int categoryId) {
        return productDao.getProductsByCategoryWithCategory(categoryId);
    }

    public ProductWithCategoryAndStock getProductByIdWithCategoryAndStock(int productId) {
        return productDao.getProductByIdWithCategoryAndStock(productId)
                .orElseThrow(() -> new NotFoundException("Product with ID " + productId + " not found"));
    }
}
