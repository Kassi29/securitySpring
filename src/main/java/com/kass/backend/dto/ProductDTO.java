package com.kass.backend.dto;

import com.kass.backend.models.CategoryModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProductDTO {
    private int id;
      private String name;
    private String description;
    private List<CategoryModel> categories;
    private int stock;
    private double price;
    private String sellerEmail;
    private String imageUrl;


}
