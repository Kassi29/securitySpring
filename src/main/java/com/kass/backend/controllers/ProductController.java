package com.kass.backend.controllers;


import com.kass.backend.models.AlmacenModel;
import com.kass.backend.models.CategoryModel;
import com.kass.backend.models.UserModel;
import com.kass.backend.repositories.IAlmacen;
import com.kass.backend.repositories.ICategory;
import com.kass.backend.repositories.IUser;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.kass.backend.models.ProductModel;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final ProductValidation productValidation; // Asegúrate de tener un validador
    private final ICategory iCategory;
    private final IUser iUser;
    private final IAlmacen iAlmacen;
    private final String imageDirectory = "src/main/resources/static/images/";

    public ProductController(final ProductService productService, ProductValidation productValidation,
                             ICategory iCategory,IUser iUser, IAlmacen iAlmacen) {
        this.productService = productService;
        this.productValidation = productValidation;
        this.iCategory=iCategory;
        this.iUser=iUser;
        this.iAlmacen=iAlmacen;
    }

    @GetMapping
    public List<ProductModel> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/artesano/{sellerId}")
    public ResponseEntity<List<ProductModel>> getProductsBySellerId(@PathVariable int sellerId) {
        List<ProductModel> products = productService.getProductsBySellerId(sellerId);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductModel> getProductById(@PathVariable int id) {
        Optional<ProductModel> productModelOptional = productService.getProductById(id);
        return productModelOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /*
    @PostMapping
    public ResponseEntity<?> addProduct(@Valid @RequestBody ProductModel productModel, BindingResult bindingResult) {
        productValidation.validate(productModel, bindingResult);
        if (bindingResult.hasErrors()) {
            return validation(bindingResult);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.save(productModel));
    }
  */
    private ResponseEntity<?> validation(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();

        bindingResult.getFieldErrors().forEach(fieldError -> {
            errors.put("El campo " + fieldError.getField(), " ERROR: " + fieldError.getDefaultMessage());
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

/*

    @PostMapping
    public ResponseEntity<?> addProduct(
            @RequestParam("file") MultipartFile file,
            @RequestParam("name") String name,
            @RequestParam("description") String descripcion,
            @RequestParam("stock") int stock,
            @RequestParam("price") float precio,
            @RequestParam("categories") List<CategoryModel> categoria,  // Cambiado a List<CategoryModel>
            @RequestParam("seller") UserModel usuario,
            @RequestParam("almacen") AlmacenModel almacen) {

        // Crear el ProductModel desde los parámetros
        ProductModel productModel = new ProductModel();
        productModel.setName(name);
        productModel.setDescription(descripcion);
        productModel.setStock(stock);
        productModel.setPrice(precio);
        productModel.setAlmacen(almacen);

        // Validar el modelo de producto
        BindingResult bindingResult = new BeanPropertyBindingResult(productModel, "productModel");
        productValidation.validate(productModel, bindingResult);
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        // Lógica para guardar la imagen
        if (!file.isEmpty()) {
            String imageUrl = saveImage(file);
            productModel.setImageUrl(imageUrl);
        }

        // Aquí se espera que 'categoria' ya contenga los objetos CategoryModel
        if (categoria == null || categoria.isEmpty()) {
            return ResponseEntity.badRequest().body("Al menos una categoría es obligatoria.");
        }
        productModel.setCategories(categoria);

        // Obtener el vendedor
        Optional<UserModel> sellerOptional = iUser.findById(usuario.getId());
        if (sellerOptional.isPresent()) {
            UserModel seller = sellerOptional.get();
            productModel.setSeller(seller);
        } else {
            return ResponseEntity.badRequest().body("El vendedor no existe.");
        }

        // Guardar el producto
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.save(productModel));
    }

 */
@PostMapping
public ResponseEntity<?> addProduct(
        @RequestParam("file") MultipartFile file,
        @RequestParam("name") String name,
        @RequestParam("description") String descripcion,
        @RequestParam("stock") int stock,
        @RequestParam("price") float precio,
        @RequestParam("categories") List<Integer> categoryIds, // Cambia a List<Long>
        @RequestParam("seller") int sellerId, // Solo el ID
        @RequestParam("almacen") int almacenId) { // Solo el ID

    // Obtener las categorías a partir de los IDs
    List<CategoryModel> categoryModels = iCategory.findByIdIn(categoryIds);
    if (categoryModels.isEmpty()) {
        return ResponseEntity.badRequest().body("Al menos una categoría es obligatoria.");
    }

    // Crear el ProductModel
    ProductModel productModel = new ProductModel();
    productModel.setName(name);
    productModel.setDescription(descripcion);
    productModel.setStock(stock);
    productModel.setPrice(precio);
    productModel.setCategories(categoryModels);

    // Obtener el vendedor
    Optional<UserModel> sellerOptional = iUser.findById(sellerId);
    if (sellerOptional.isPresent()) {
        productModel.setSeller(sellerOptional.get());
    } else {
        return ResponseEntity.badRequest().body("El vendedor no existe.");
    }

    // Obtener el almacén
    Optional<AlmacenModel> almacenOptional = iAlmacen.findById(almacenId);
    if (almacenOptional.isPresent()) {
        productModel.setAlmacen(almacenOptional.get());
    } else {
        return ResponseEntity.badRequest().body("El almacén no existe.");
    }

    // Lógica para guardar la imagen
    if (!file.isEmpty()) {
        String imageUrl = saveImage(file);
        productModel.setImageUrl(imageUrl);
    }

    // Guardar el producto
    return ResponseEntity.status(HttpStatus.CREATED).body(productService.save(productModel));
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

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable int id, @Valid @RequestBody ProductModel productModel, BindingResult bindingResult) {
        productValidation.validate(productModel, bindingResult);
        if (bindingResult.hasErrors()) {
            return validation(bindingResult);
        }

        try {
            ProductModel updatedProduct = productService.update(id, productModel);
            return ResponseEntity.ok(updatedProduct);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable int id) {
        Optional<ProductModel> productModelOptional = productService.getProductById(id);
        if (productModelOptional.isPresent()) {
            productService.delete(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
