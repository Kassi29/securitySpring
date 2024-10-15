package com.kass.backend.controllers;


import com.kass.backend.dto.ChangePasswordRequest;
import com.kass.backend.dto.DeliveryDTO;
import com.kass.backend.dto.UserDto;
import com.kass.backend.models.*;
import com.kass.backend.repositories.IEmpresa;
import com.kass.backend.services.DeliveryService;
import com.kass.backend.services.EmpresaService;
import com.kass.backend.services.RoleService;
import com.kass.backend.services.UserService;
import com.kass.backend.validation.user.UserValidation;
import com.kass.backend.validation.user.UserValidationEdit;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
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
    private final EmpresaService empresaService;
    private final DeliveryService deliveryService;


    //para ver los roles
    private final RoleService roleService;

    public UserController(UserService userService, UserValidation userValidation
            , RoleService roleService, UserValidationEdit userValidationEdit,
                          EmpresaService empresaService, DeliveryService deliveryService) {

        this.userService = userService;
        this.userValidation = userValidation;
        this.roleService = roleService;
        this.userValidationEdit = userValidationEdit;
        this.empresaService = empresaService;
        this.deliveryService = deliveryService;

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
        Optional<UserModel> userModelOptional = userService.delete(id);
        if (userModelOptional.isPresent()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
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

    //cambio
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        // Verifica que el ID no sea nulo
        if (request.getUserId() == null) {
            return ResponseEntity.badRequest().body("El ID del usuario no puede ser nulo");
        }

        // Verificar si la contraseña actual es correcta
        if (!userService.checkCurrentPassword(request.getEmail(), request.getCurrentPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Contraseña actual incorrecta");
        }

        // Verificar si las nuevas contraseñas coinciden
        if (!request.getNewPassword().equals(request.getRepeatNewPassword())) {
            return ResponseEntity.badRequest().body("Las nuevas contraseñas no coinciden");
        }

        // Cambiar la contraseña
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


