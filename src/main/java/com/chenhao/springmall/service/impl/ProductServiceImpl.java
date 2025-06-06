package com.chenhao.springmall.service.impl;

import com.chenhao.springmall.dto.ProductQueryParams;
import com.chenhao.springmall.dto.ProductRequest;
import com.chenhao.springmall.model.Product;
import com.chenhao.springmall.repository.ProductRepository;
import com.chenhao.springmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Integer countProduct(ProductQueryParams productQueryParams) {
        return  productRepository.findAll().size();
    }

    @Override
    public List<Product> getProducts(ProductQueryParams params) {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Integer productId) {
        return productRepository.findById(productId).orElse(null);
    }

    @Override
    public Integer createProduct(ProductRequest productRequest) {
        Product product = new Product();
        product.setProductName(productRequest.getProductName());
        product.setPrice(productRequest.getPrice());
        product.setCategory(productRequest.getCategory());
        product.setDescription(productRequest.getDescription());
        product.setImageUrl(productRequest.getImageUrl());
        product.setStock(productRequest.getStock());

        Date now = new Date();
        product.setCreatedDate(now);
        product.setLastModifiedDate(now);
        Integer productId = productRepository.save(product).getProductId();
        return productId;
    }

    @Override
        public Product updateProduct(Integer productId, ProductRequest productRequest) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("找不到商品 ID: " + productId));
        product.setProductName(productRequest.getProductName());
        product.setPrice(productRequest.getPrice());
        product.setCategory(productRequest.getCategory());
        product.setDescription(productRequest.getDescription());
        product.setImageUrl(productRequest.getImageUrl());
        product.setStock(productRequest.getStock());
        product.setLastModifiedDate(new Date());
        productRepository.save(product);
        return product;
    }

    @Override
    public void deleteProductById(Integer productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("找不到商品，ID: " + productId));
        productRepository.deleteById(productId);

    }
}
