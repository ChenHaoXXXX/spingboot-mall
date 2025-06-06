package com.chenhao.springmall.repository;

import com.chenhao.springmall.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;



public interface ProductRepository extends JpaRepository<Product,Integer> {
}
