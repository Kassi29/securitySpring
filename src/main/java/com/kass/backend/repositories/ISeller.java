package com.kass.backend.repositories;

import com.kass.backend.models.RoleModel;
import com.kass.backend.models.SellerRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ISeller extends JpaRepository<SellerRole, Integer> {
    //Optional<SellerRole> findByUserId(int userId);

    @Query("SELECT sr FROM SellerRole sr WHERE sr.user.id = :userId")
    List<SellerRole> findByUserId(@Param("userId") int userId);

    @Modifying
    @Query("DELETE FROM SellerRole dr WHERE dr.user.id = :userId")
    void deleteByUserId(@Param("userId") int userId);

}
