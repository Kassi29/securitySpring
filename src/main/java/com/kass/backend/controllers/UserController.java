package com.kass.backend.controllers;


import com.kass.backend.dto.ChangePasswordRequest;
import com.kass.backend.dto.DeliveryDTO;
import com.kass.backend.dto.SellerDTO;
import com.kass.backend.dto.UserDto;
import com.kass.backend.models.*;
import com.kass.backend.repositories.IDelivery;
import com.kass.backend.repositories.IEmpresa;
import com.kass.backend.repositories.ISeller;
import com.kass.backend.repositories.IUser;
import com.kass.backend.services.*;
import com.kass.backend.validation.user.UserValidation;
import com.kass.backend.validation.user.UserValidationEdit;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserValidation userValidation;
    private final UserValidationEdit userValidationEdit;
    private final DeliveryService deliveryService;
    private final IUser iuser;
    private final SellerService sellerService;
    private final ISeller iSeller;
    private final IDelivery iDelivery;

    //para ver los roles
    private final RoleService roleService;

    public UserController(UserService userService, UserValidation userValidation
            , RoleService roleService, UserValidationEdit userValidationEdit,
                          DeliveryService deliveryService, IUser iuser, SellerService sellerService,
                          ISeller iSeller, IDelivery iDelivery) {

        this.userService = userService;
        this.userValidation = userValidation;
        this.roleService = roleService;
        this.userValidationEdit = userValidationEdit;
        this.deliveryService = deliveryService;
        this.iuser = iuser;
        this.sellerService = sellerService;
        this.iSeller = iSeller;
        this.iDelivery = iDelivery;

    }

    @GetMapping
    public List<UserModel> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/findByEmail/{email}")
    public ResponseEntity<UserModel> getUserById(@PathVariable String email) {
        Optional<UserModel> userModelOptional = userService.findByEmail(email);
        return userModelOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    //para crear admins
    @PostMapping
    public ResponseEntity<?> addUser(@Valid @RequestBody UserModel user, BindingResult bindingResult) {
        System.out.println("ESTAS REGISTRANDO UN USARIO ADMIN");
        user.setAdmin(true);
        return registerUser(user, bindingResult);
    }

    //para crear users
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserModel user, BindingResult bindingResult) {
        userValidation.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return validation(bindingResult);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable int id) {
        System.out.println("Intentando eliminar el usuario con ID: " + id);

        // Obtener el usuario por ID
        Optional<UserModel> userModelOptional = iuser.findById(id);
        if (userModelOptional.isPresent()) {
            UserModel user = userModelOptional.get();
            System.out.println("Usuario encontrado: " + user);

            // Verificar si el usuario tiene el rol ROLE_DELIVERY
            boolean isDelivery = user.getRoles().stream()
                    .anyMatch(role -> role.getName().equals("ROLE_DELIVERY"));
            System.out.println("El usuario tiene rol ROLE_DELIVERY: " + isDelivery);

            if (isDelivery) {
                System.out.println("Eliminando deliveries asociados al usuario con ID: " + id);
                // Asegúrate de que este método elimina todos los DeliveryRoles asociados
                deliveryService.deleteDeliveriesByUserId(id);
            }

            // Ahora intenta eliminar el usuario
            try {
                userService.delete(id);
                System.out.println("Usuario con ID: " + id + " eliminado correctamente");
                return ResponseEntity.noContent().build();
            } catch (DataIntegrityViolationException e) {
                System.out.println("Error de integridad al eliminar el usuario con ID: " + id + ": " + e.getMessage());
                return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 409 Conflict
            }
        } else {
            System.out.println("Usuario con ID: " + id + " no encontrado");
            return ResponseEntity.notFound().build();
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable int id, @Valid @RequestBody UserDto userDto, BindingResult bindingResult) {
        userValidationEdit.validate(userDto, bindingResult);
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        try {
            UserModel updatedUser = userService.update(id, userDto);
            return ResponseEntity.ok(updatedUser);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    private ResponseEntity<?> validation(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();

        bindingResult.getFieldErrors().forEach(fieldError -> {
            errors.put("El campo " + fieldError.getField(), " Error: " + fieldError.getDefaultMessage());
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    //para ver los roles
    @GetMapping("/roles")
    public List<RoleModel> getAllRoles() {
        return roleService.getAllRoles();
    }

    //rol delivery
    @PostMapping("/registerDelivery")
    public ResponseEntity<?> registerDelivery(
            @Valid @RequestBody UserModel user,
            @RequestParam int empresaId,
            BindingResult bindingResult) {

        userValidation.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return validation(bindingResult);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveDelivery(user, empresaId));
    }

    @GetMapping("/listaDeliverys")
    public List<DeliveryDTO> getAllDeliveryRoles() {
        return deliveryService.getAllDeliveryRoles();
    }

    @DeleteMapping("/listaDeliverys/{id}")
    public ResponseEntity<Void> deleteDelivery(@PathVariable int id) {
        deliveryService.deleteDeliveryById(id); // Implementa este método en tu DeliveryService
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/listaDeliverys/{sellerRoleId}")
    public ResponseEntity<DeliveryRole> updateDelivery(@PathVariable int sellerRoleId, @RequestBody DeliveryRole updatedData) {
        // Buscar el artesano por ID utilizando Optional
        Optional<DeliveryRole> existingDeliveryOpt = iDelivery.findById(sellerRoleId);
        if (existingDeliveryOpt.isEmpty()) {
            return ResponseEntity.notFound().build(); // Retorna 404 si no se encuentra
        }

        DeliveryRole existingDelivery = existingDeliveryOpt.get();

        existingDelivery.setEmpresa(updatedData.getEmpresa());

        // Guardar el artesano actualizado

        DeliveryRole updatedDelivery = iDelivery.save(existingDelivery);

        return ResponseEntity.ok(updatedDelivery); // Retorna 200 OK con el objeto actualizado
    }


    //rol de artesano creado by ADMIN se agrega comunidad
    @PostMapping("/registerArtesano")
    public ResponseEntity<?> registerArtesano(
            @Valid @RequestBody UserModel user,
            @RequestParam int comunidadId,
            BindingResult bindingResult) {

        userValidation.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return validation(bindingResult);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveArtesano(user, comunidadId));
    }

    @GetMapping("/listaArtesanos")
    public List<SellerDTO> getAllSellers() {
        return sellerService.getAllSellerRoles();
    }

    //edito almacen


    @DeleteMapping("/listaArtesanos/{id}")
    public ResponseEntity<Void> deleteSeller(@PathVariable int id) {
        sellerService.deleteSellerById(id);
        return ResponseEntity.noContent().build(); // Devuelve un 204 No Content
    }

    @PutMapping("/listaArtesanos/{sellerRoleId}")
    public ResponseEntity<SellerRole> updateArtesano(@PathVariable int sellerRoleId, @RequestBody SellerRole updatedData) {
        // Buscar el artesano por ID utilizando Optional
        Optional<SellerRole> existingArtesanoOpt = iSeller.findById(sellerRoleId);
        if (!existingArtesanoOpt.isPresent()) {
            return ResponseEntity.notFound().build(); // Retorna 404 si no se encuentra
        }

        SellerRole existingArtesano = existingArtesanoOpt.get();

        // Actualizar la comunidad y mantener otros campos
        existingArtesano.setComunidad(updatedData.getComunidad());

        // Guardar el artesano actualizado
        SellerRole updatedArtesano = iSeller.save(existingArtesano);

        return ResponseEntity.ok(updatedArtesano); // Retorna 200 OK con el objeto actualizado
    }


    //cambio
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        if (request.getUserId() == null) {
            return ResponseEntity.badRequest().body("El ID del usuario no puede ser nulo");
        }
        if (!userService.checkCurrentPassword(request.getEmail(), request.getCurrentPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Contraseña actual incorrecta");
        }
        if (!request.getNewPassword().equals(request.getRepeatNewPassword())) {
            return ResponseEntity.badRequest().body("Las nuevas contraseñas no coinciden");
        }

        userService.changePassword(request.getUserId(), request.getNewPassword());

        return ResponseEntity.ok(Collections.singletonMap("message", "Contraseña cambiada con éxito"));

    }

    //USUARIO X ROL
    @GetMapping("/filterByRole/{roleName}")
    public ResponseEntity<List<UserModel>> getUsersByRole(@PathVariable String roleName) {
        List<UserModel> users = userService.getUsersByRole(roleName);
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }


}


