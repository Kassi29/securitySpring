package com.kass.backend.repositories;

import com.kass.backend.models.DeliveryRole;
import com.kass.backend.models.EmpresaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IDelivery extends JpaRepository<DeliveryRole, Integer> {
    @Modifying
    @Query("DELETE FROM DeliveryRole dr WHERE dr.user.id = :userId")
    void deleteByUserId(@Param("userId") int userId);



}
