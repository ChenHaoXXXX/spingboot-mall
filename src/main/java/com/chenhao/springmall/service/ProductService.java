package com.chenhao.springmall.service;

import com.chenhao.springmall.dto.ProductQueryParams;
import com.chenhao.springmall.dto.ProductRequest;
import com.chenhao.springmall.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    Page<Product> getProducts(ProductQueryParams productQueryParams, Pageable pageable);

    Product getProductById(Integer productId);

    Integer createProduct(ProductRequest productRequest);

    Product updateProduct(Integer productId, ProductRequest productRequest);

    void deleteProductById(Integer productId);
}
