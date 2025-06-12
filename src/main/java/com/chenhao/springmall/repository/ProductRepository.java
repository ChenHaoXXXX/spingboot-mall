package com.chenhao.springmall.repository;

import com.chenhao.springmall.constant.ProductCategory;
import com.chenhao.springmall.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {
    
    // 根據類別查詢商品
    List<Product> findByCategory(ProductCategory category);
    
    // 根據類別分頁查詢商品
    Page<Product> findByCategory(ProductCategory category, Pageable pageable);
    
    // 根據關鍵字搜尋商品名稱或描述
    @Query("SELECT p FROM Product p WHERE p.productName LIKE %:keyword% OR p.description LIKE %:keyword%")
    List<Product> searchProducts(@Param("keyword") String keyword);
    
    // 根據關鍵字分頁搜尋商品名稱或描述
    @Query("SELECT p FROM Product p WHERE p.productName LIKE %:keyword% OR p.description LIKE %:keyword%")
    Page<Product> searchProducts(@Param("keyword") String keyword, Pageable pageable);
    
    // 根據價格範圍查詢商品
    List<Product> findByPriceBetween(Integer minPrice, Integer maxPrice);
    
    // 根據價格範圍分頁查詢商品
    Page<Product> findByPriceBetween(Integer minPrice, Integer maxPrice, Pageable pageable);
    
    // 根據庫存狀態查詢商品
    List<Product> findByStockGreaterThan(Integer stock);
    
    // 根據庫存狀態分頁查詢商品
    Page<Product> findByStockGreaterThan(Integer stock, Pageable pageable);
    
    // 查詢熱門商品（根據創建時間排序）
    @Query("SELECT p FROM Product p ORDER BY p.createdDate DESC")
    List<Product> findTopProducts(Pageable pageable);
    
    // 檢查商品是否存在
    boolean existsByProductName(String productName);
}
