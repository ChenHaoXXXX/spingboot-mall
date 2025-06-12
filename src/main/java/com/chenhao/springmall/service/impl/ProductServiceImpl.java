package com.chenhao.springmall.service.impl;

import com.chenhao.springmall.constant.ProductCategory;
import com.chenhao.springmall.dto.ProductQueryParams;
import com.chenhao.springmall.dto.ProductRequest;
import com.chenhao.springmall.model.Product;
import com.chenhao.springmall.repository.ProductRepository;
import com.chenhao.springmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Component
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Page<Product> getProducts(ProductQueryParams params, Pageable pageable) {
        Specification<Product> spec = (root, query, cb) -> {
            if (params.getCategory() != null) {
                return cb.equal(root.get("category"), params.getCategory());
            }
            if (params.getSearch() != null && !params.getSearch().isEmpty()) {
                String searchPattern = "%" + params.getSearch().toLowerCase() + "%";
                return cb.or(
                    cb.like(cb.lower(root.get("productName")), searchPattern),
                    cb.like(cb.lower(root.get("description")), searchPattern)
                );
            }
            return null;
        };

        return productRepository.findAll(spec, pageable);
    }

    @Override
    public Product getProductById(Integer productId) {
        return productRepository.findById(productId).orElse(null);
    }

    @Override
    @Transactional
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
        
        return productRepository.save(product).getProductId();
    }

    @Override
    @Transactional
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
        
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public void deleteProductById(Integer productId) {
        if (!productRepository.existsById(productId)) {
            throw new IllegalArgumentException("找不到商品，ID: " + productId);
        }
        productRepository.deleteById(productId);
    }
}
