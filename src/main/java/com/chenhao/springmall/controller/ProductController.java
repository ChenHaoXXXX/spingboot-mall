package com.chenhao.springmall.controller;

import com.chenhao.springmall.constant.ProductCategory;
import com.chenhao.springmall.dto.ProductQueryParams;
import com.chenhao.springmall.dto.ProductRequest;
import com.chenhao.springmall.model.Product;
import com.chenhao.springmall.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class ProductController {
//    @GetMapping("/products")
//    public String welcome(Authentication authentication) {
//        // 取得使用者的帳號
//        String username = authentication.getName();
//
//        // 取得使用者的權限
//        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//
//        return "Hello " + username + "! 你的權限為: " + authorities;
//    }

    @Autowired
    ProductService productService;


    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProducts(
            //查詢條件 Filtering
            @RequestParam(required = false) ProductCategory category,
            @RequestParam(required = false) String search
    ) {
        //TODO: 尚未實作條件查詢

        ProductQueryParams productQueryParams = new ProductQueryParams();
        productQueryParams.setCategory(category);
        productQueryParams.setSearch(search);

        // 取得 product list
        List<Product> products = productService.getProducts(productQueryParams);

        // 取得 product 總數
        Integer count = productService.countProduct(productQueryParams);

        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable Integer productId) {
        Product product =  productService.getProductById(productId);
        if(product != null) {
            return ResponseEntity.status(HttpStatus.OK).body(product);
        } else  {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/products")
    public ResponseEntity<Product> creatProduct(@RequestBody @Valid ProductRequest productRequest) {
        Integer id = productService.createProduct(productRequest);
        Product product = productService.getProductById(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer productId,
                                                 @RequestBody @Valid ProductRequest productRequest) {
        try {
            Product updatedProduct = productService.updateProduct(productId, productRequest);
            return ResponseEntity.status(HttpStatus.OK).body(updatedProduct);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Integer productId) {
        productService.deleteProductById(productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("刪除成功");
    }
}
