package com.chenhao.springmall.controller;

import com.chenhao.springmall.constant.ProductCategory;
import com.chenhao.springmall.dto.ProductQueryParams;
import com.chenhao.springmall.dto.ProductRequest;
import com.chenhao.springmall.model.Product;
import com.chenhao.springmall.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Collection;

@Controller
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

    //進入網站首頁
    @GetMapping("/")
    public String redirectToProducts() {
        return "redirect:/products";
    }

    // 顯示商品列表頁面
    @GetMapping("/products")
    public String showProductsPage(Model model, Authentication authentication) {
        // 獲取初始商品數據
        ProductQueryParams params = new ProductQueryParams();
        Pageable pageable;
        
        // 根據用戶角色決定顯示的商品數量
        if (authentication == null || isGuest(authentication)) {
            // 訪客只能看到3個商品
            pageable = PageRequest.of(0, 3);
        } else {
            // 其他角色可以看到12個商品
            pageable = PageRequest.of(0, 12);
        }
        
        Page<Product> productPage = productService.getProducts(params, pageable);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", productPage.getNumber());
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("totalItems", productPage.getTotalElements());
        
        return "product-list";
    }

    // 顯示新增商品頁面
    @GetMapping("/products/new")
    public String showNewProductPage(Model model) {
        model.addAttribute("categories", ProductCategory.values());
        return "product-form";
    }

    // 顯示編輯商品頁面
    @GetMapping("/products/{productId}/edit")
    public String showEditProductPage(@PathVariable Integer productId, Model model) {
        Product product = productService.getProductById(productId);
        if (product == null) {
            return "redirect:/products";
        }
        
        model.addAttribute("product", product);
        model.addAttribute("categories", ProductCategory.values());
        return "product-update";
    }

    // 獲取商品列表 API
    @GetMapping("/api/products")
    @ResponseBody
    public ResponseEntity<?> getProducts(
            @RequestParam(required = false) ProductCategory category,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            Authentication authentication
    ) {
        try {
            ProductQueryParams productQueryParams = new ProductQueryParams();
            productQueryParams.setCategory(category);
            productQueryParams.setSearch(search);

            // 根據用戶角色調整分頁大小
            if (authentication == null || isGuest(authentication)) {
                size = 3; // 訪客只能看到3個商品
            }

            Pageable pageable = PageRequest.of(page, size);
            Page<Product> productPage = productService.getProducts(productQueryParams, pageable);

            Map<String, Object> response = new HashMap<>();
            response.put("products", productPage.getContent());
            response.put("currentPage", productPage.getNumber());
            response.put("totalItems", productPage.getTotalElements());
            response.put("totalPages", productPage.getTotalPages());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 獲取單個商品 API
    @GetMapping("/api/products/{productId}")
    @ResponseBody
    public ResponseEntity<?> getProduct(@PathVariable Integer productId) {
        try {
            Product product = productService.getProductById(productId);
            if (product != null) {
                return ResponseEntity.ok(product);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("商品不存在");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 創建商品 API
    @PostMapping("/api/products")
    @ResponseBody
//    @PreAuthorize("hasAnyRole('ADMIN', 'MERCHANT')")
    public ResponseEntity<?> createProduct(@RequestBody @Valid ProductRequest productRequest) {
        try {
            Integer id = productService.createProduct(productRequest);
            Product product = productService.getProductById(id);
            return ResponseEntity.status(HttpStatus.CREATED).body(product);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 更新商品 API
    @PutMapping("/api/products/{productId}")
    @ResponseBody
    //@PreAuthorize("hasAnyRole('ADMIN', 'MERCHANT')")
    public ResponseEntity<?> updateProduct(
            @PathVariable Integer productId,
            @RequestBody @Valid ProductRequest productRequest
    ) {
        try {
            Product updatedProduct = productService.updateProduct(productId, productRequest);
            return ResponseEntity.ok(updatedProduct);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // 刪除商品 API
    @DeleteMapping("/api/products/{productId}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ADMIN', 'MERCHANT')")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer productId) {
        try {
            productService.deleteProductById(productId);
            return ResponseEntity.ok("商品已刪除");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 輔助方法：檢查是否為訪客
    private boolean isGuest(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .noneMatch(auth -> auth.equals("ROLE_ADMIN") || 
                                 auth.equals("ROLE_NORMAL_MEMBER") || 
                                 auth.equals("ROLE_MERCHANT"));
    }
}
