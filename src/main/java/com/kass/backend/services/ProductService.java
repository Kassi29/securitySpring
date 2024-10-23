package com.kass.backend.services;


import com.kass.backend.dto.ProductDTO;
import com.kass.backend.models.ProductModel;
import com.kass.backend.models.SellerRole;
import com.kass.backend.models.UserModel;
import com.kass.backend.repositories.IProduct;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.kass.backend.models.CategoryModel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final String imageDirectory = "src/main/resources/static/images/";
    private final IProduct iProduct;

    public ProductService(IProduct iProduct){
        this.iProduct=iProduct;
    }

    // Listar todos los productos
/*
    @Transactional
    public List<ProductModel> getAllProducts() {
        return iProduct.findAll();
    }


 */

    public List<ProductDTO> getAllProducts() {
        List<ProductModel> products = iProduct.findAll();
        List<ProductDTO> productDTOs = new ArrayList<>();

        for (ProductModel product : products) {
            SellerRole sellerRole = product.getSeller();
            UserModel user = sellerRole.getUser();

            ProductDTO dto = new ProductDTO();
            dto.setId(product.getId());
            dto.setName(product.getName());
            dto.setDescription(product.getDescription());

            dto.setStock(product.getStock());
            dto.setPrice(product.getPrice());
            dto.setSellerEmail(user.getEmail());
            dto.setImageUrl(product.getImageUrl());
            dto.setCategories(product.getCategories());
            productDTOs.add(dto);
        }

        return productDTOs;
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
    public ProductModel update(int id, ProductModel productModel, MultipartFile file) {
        Optional<ProductModel> existingProductOptional = getProductById(id);
        if (existingProductOptional.isPresent()) {
            ProductModel existingProduct = existingProductOptional.get();

            // Actualiza los campos del producto
            existingProduct.setName(productModel.getName());
            existingProduct.setDescription(productModel.getDescription());
            existingProduct.setStock(productModel.getStock());
            existingProduct.setPrice(productModel.getPrice());
            existingProduct.setCategories(productModel.getCategories());
            existingProduct.setAlmacen(productModel.getAlmacen());
            existingProduct.setSeller(productModel.getSeller());

            // Maneja la imagen si se proporciona
            if (file != null && !file.isEmpty()) {
                String imagePath = saveImage(file); // Asegúrate de que este método funcione correctamente
                existingProduct.setImageUrl(imagePath); // Asegúrate de que tengas un campo imageUrl
            }

            return iProduct.save(existingProduct);
        }
        throw new EntityNotFoundException("Producto con ID: " + id + " no encontrado");
    }

    public String saveImage(MultipartFile file) {
        try {
            // Crea un nombre único para la imagen
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            // Crea la ruta del archivo
            Path path = Paths.get(imageDirectory + fileName);
            // Guarda el archivo
            Files.write(path, file.getBytes());

            // Retorna la URL de acceso a la imagen
            //  return "C:\\workspace\\281\\backend\\src\\main\\resources\\static\\images\\" + fileName;
            return "/images/" + fileName; // Esto debería ser la ruta accesible desde el navegador

// Cambia según tu configuración
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al guardar la imagen");
        }
    }

            // Eliminar producto
    @Transactional
    public Optional<ProductModel> delete(int id) {
        Optional<ProductModel> productModelOptional = iProduct.findById(id);
        productModelOptional.ifPresent(iProduct::delete);
        return productModelOptional;
    }
}

//para o
