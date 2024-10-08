package com.kass.backend.repositories;


import com.kass.backend.models.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProduct extends JpaRepository<ProductModel, Integer> {
    List<ProductModel> findBySellerId(int sellerId);
}
