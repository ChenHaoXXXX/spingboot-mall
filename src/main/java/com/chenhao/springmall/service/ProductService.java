package com.chenhao.springmall.service;

import com.chenhao.springmall.dto.ProductQueryParams;
import com.chenhao.springmall.dto.ProductRequest;
import com.chenhao.springmall.model.Product;

import java.util.List;

public interface ProductService {
    Integer countProduct(ProductQueryParams productQueryParams);

    List<Product> getProducts(ProductQueryParams productQueryParams);

    Product getProductById(Integer productId);

    Integer createProduct(ProductRequest productRequest);

    Product updateProduct(Integer productId, ProductRequest productRequest);

    void deleteProductById(Integer productId);
}
