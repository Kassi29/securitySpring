package com.kass.backend.controllers;


import com.kass.backend.dto.UserDto;
import com.kass.backend.models.CategoryModel;
import com.kass.backend.models.RoleModel;
import com.kass.backend.models.UserModel;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserValidation userValidation;
    private final UserValidationEdit userValidationEdit;

    //para ver los roles
    private final RoleService roleService;

    public UserController(UserService userService, UserValidation userValidation
            , RoleService roleService, UserValidationEdit userValidationEdit) {

        this.userService = userService;
        this.userValidation = userValidation;
        this.roleService = roleService;
        this.userValidationEdit = userValidationEdit;
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

}
