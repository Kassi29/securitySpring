package com.kass.backend.controllers;


import com.kass.backend.dto.ProductDTO;
import com.kass.backend.models.*;
import com.kass.backend.repositories.IAlmacen;
import com.kass.backend.repositories.ICategory;
import com.kass.backend.repositories.ISeller;
import com.kass.backend.repositories.IUser;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.kass.backend.services.ProductService;
import com.kass.backend.validation.ProductValidation; // Asegúrate de tener la validación necesaria
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final ProductValidation productValidation; // Asegúrate de tener un validador
    private final ICategory iCategory;
    private final IUser iUser;
    private final IAlmacen iAlmacen;
    private final String imageDirectory = "src/main/resources/static/images/";
    private final ISeller iSeller;

    public ProductController(final ProductService productService, ProductValidation productValidation,
                             ICategory iCategory, IUser iUser, IAlmacen iAlmacen, ISeller iSeller) {
        this.productService = productService;
        this.productValidation = productValidation;
        this.iCategory = iCategory;
        this.iUser = iUser;
        this.iAlmacen = iAlmacen;
        this.iSeller = iSeller;
    }

/*
    @GetMapping
    public List<ProductModel> getAllProducts() {
        return productService.getAllProducts();
    }



 */
@GetMapping
public List<ProductDTO> getAllProducts() {
    return productService.getAllProducts();
}









    @GetMapping("/artesano/{sellerId}")
    public ResponseEntity<List<ProductModel>> getProductsBySellerId(@PathVariable int sellerId) {
        List<ProductModel> products = productService.getProductsBySellerId(sellerId);

        // Imprimir cada producto y su URL de imagen
        for (ProductModel product : products) {
            System.out.println("Producto ID: " + product.getId());
            System.out.println("Nombre: " + product.getName());
            System.out.println("URL de imagen: " + product.getImageUrl());
        }

        return ResponseEntity.ok(products);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProductModel> getProductById(@PathVariable int id) {
        Optional<ProductModel> productModelOptional = productService.getProductById(id);
        return productModelOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    private ResponseEntity<?> validation(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();

        bindingResult.getFieldErrors().forEach(fieldError -> {
            errors.put("El campo " + fieldError.getField(), " ERROR: " + fieldError.getDefaultMessage());
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @PostMapping
    public ResponseEntity<?> addProduct(
            @RequestParam("file") MultipartFile file,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("stock") int stock,
            @RequestParam("price") double price,
            @RequestParam("categories") List<Integer> categoryIds,
            @RequestParam("seller") int sellerId,
            @RequestParam("almacen") int almacenId) {

        // Verificar si el usuario existe
        Optional<UserModel> userOptional = iUser.findById(sellerId);
        if (!userOptional.isPresent()) {
            return ResponseEntity.badRequest().body("El vendedor no existe.");
        }
        UserModel user = userOptional.get();
        System.out.println("Usuario encontrado: " + user.getName());

        // Crear el SellerRole
        SellerRole sellerRole = new SellerRole(user);

        // Guardar el SellerRole
        sellerRole = iSeller.save(sellerRole);
        if (sellerRole == null || sellerRole.getId() == 0) {
            System.out.println("Error: No se pudo crear el SellerRole.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No se pudo crear el SellerRole.");
        }
        System.out.println("SellerRole guardado con ID: " + sellerRole.getId());

        // Obtener las categorías
        List<CategoryModel> categoryModels = iCategory.findByIdIn(categoryIds);
        if (categoryModels.isEmpty()) {
            return ResponseEntity.badRequest().body("Al menos una categoría es obligatoria.");
        }

        // Crear el ProductModel
        ProductModel productModel = new ProductModel();
        productModel.setName(name);
        productModel.setDescription(description);
        productModel.setStock(stock);
        productModel.setPrice(price);
        productModel.setCategories(categoryModels);
        productModel.setSeller(sellerRole); // Establecer el SellerRole en el producto

        // Obtener el almacén
        Optional<AlmacenModel> almacenOptional = iAlmacen.findById(almacenId);
        if (!almacenOptional.isPresent()) {
            return ResponseEntity.badRequest().body("El almacén no existe.");
        }
        productModel.setAlmacen(almacenOptional.get());

        // Guardar la imagen
        if (!file.isEmpty()) {
            String imageUrl = saveImage(file);
            productModel.setImageUrl("http://localhost:8080" + imageUrl);
        }

        // Guardar el producto
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.save(productModel));
    }


    public String saveImage(MultipartFile file) {
        try {

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

            Path path = Paths.get(imageDirectory + fileName);

            Files.write(path, file.getBytes());

            return "/images/" + fileName; // Esto debería ser la ruta accesible desde el navegador


        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al guardar la imagen");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable int id,
            @Valid @RequestPart ProductModel productModel,
            @RequestParam(value = "file", required = false) MultipartFile file,
            BindingResult bindingResult) {

        productValidation.validate(productModel, bindingResult);
        if (bindingResult.hasErrors()) {
            return validation(bindingResult);
        }

        try {
            // Actualizar el producto y pasar la imagen al servicio
            ProductModel updatedProduct = productService.update(id, productModel, file);
            return ResponseEntity.ok(updatedProduct);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/seller/{userId}")
    public ResponseEntity<?> getProductsBySeller(@PathVariable int userId) {
        System.out.println("Recibido userId: " + userId);

        // Obtener el usuario por ID
        Optional<UserModel> userOptional = iUser.findById(userId);
        if (userOptional.isEmpty()) {
            System.out.println("Usuario no encontrado para el ID: " + userId);
            return ResponseEntity.badRequest().body("Usuario no encontrado.");
        }

        UserModel user = userOptional.get();
        System.out.println("Usuario encontrado: " + user.getName());

        // Obtener el SellerRole asociado al usuario
        List<SellerRole> sellerRoles = iSeller.findByUserId(user.getId());
        if (sellerRoles.isEmpty()) {
            System.out.println("El vendedor no existe para el usuario con ID: " + user.getId());
            return ResponseEntity.badRequest().body("El vendedor no existe.");
        }


        List<ProductModel> products = new ArrayList<>();
        for (SellerRole sellerRole : sellerRoles) {
            List<ProductModel> roleProducts = productService.getProductsBySellerId(sellerRole.getId());
            products.addAll(roleProducts);
        }

        System.out.println("Cantidad de productos encontrados para SellerRole ID: " + products.size());
        if (products.isEmpty()) {
            System.out.println("No hay productos para mostrar para SellerRole ID: " + sellerRoles.get(0).getId());
            return ResponseEntity.ok("No hay productos para mostrar.");
        }

        System.out.println("Productos encontrados: ");
        products.forEach(product ->
                System.out.println(" - " + product.getName() + " (ID: " + product.getId() + ") " + product.getImageUrl())
        );

        return ResponseEntity.ok(products);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable int id) {
        // Lógica para eliminar el producto
        productService.delete(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }


}
