package com.kass.backend.services;


import com.kass.backend.models.ProductModel;
import com.kass.backend.repositories.IProduct;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final IProduct iProduct;

    public ProductService(IProduct iProduct){
        this.iProduct=iProduct;
    }

    // Listar todos los productos
    @Transactional
    public List<ProductModel> getAllProducts() {
        return iProduct.findAll();
    }

    public List<ProductModel> getProductsBySellerId(int sellerId) {
        // Retorna la lista de productos asociados al ID del vendedor
        return iProduct.findBySellerId(sellerId);
    }

    @Transactional
    // Guardar un nuevo producto
    public ProductModel save(ProductModel product) {
       // return iProduct.save(product);

        System.out.println("estamos en save");
        ProductModel savedProduct = iProduct.save(product);
        System.out.println("Saved Product ID: " + savedProduct.getId());
        System.out.println("Categories: " + product.getCategories());
        return savedProduct;
    }

    // Ver producto por ID
    public Optional<ProductModel> getProductById(int id) {
        return iProduct.findById(id);
    }

    // Actualizar producto
    @Transactional
    public ProductModel update(int id, ProductModel productModel) {
        Optional<ProductModel> existingProductOptional = getProductById(id);
        if (existingProductOptional.isPresent()) {
            ProductModel existingProduct = existingProductOptional.get();
            existingProduct.setName(productModel.getName());
            existingProduct.setDescription(productModel.getDescription());
            existingProduct.setStock(productModel.getStock());
            existingProduct.setPrice(productModel.getPrice());
            existingProduct.setCategories(productModel.getCategories());
            existingProduct.setAlmacen(productModel.getAlmacen());
            existingProduct.setSeller(productModel.getSeller());
            return iProduct.save(existingProduct);
        }
        throw new EntityNotFoundException("Producto con ID: " + id + " no encontrado");
    }

    // Eliminar producto
    @Transactional
    public Optional<ProductModel> delete(int id) {
        Optional<ProductModel> productModelOptional = iProduct.findById(id);
        productModelOptional.ifPresent(iProduct::delete);
        return productModelOptional;
    }
}
