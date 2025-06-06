package com.chenhao.springmall.repository;

import com.chenhao.springmall.model.Product;
import org.springframework.data.repository.CrudRepository;

interface ProductRepository extends CrudRepository<Product,Integer> {
}
