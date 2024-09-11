package com.kass.backend.repositories;

import com.kass.backend.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Optional;
@CrossOrigin(origins = "http://localhost:4200")
@Repository
public interface IUser extends JpaRepository<UserModel, Integer> {


    boolean existsByEmail(String email);

    Optional<UserModel> findByEmail(String email);
}
