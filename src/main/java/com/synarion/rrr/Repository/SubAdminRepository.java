package com.synarion.rrr.Repository;

import com.synarion.rrr.Model.SubAdminModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubAdminRepository extends JpaRepository<SubAdminModel, Integer> {

    Optional<SubAdminModel> findByUserName(String userName);

    Optional<Object> findByMobile(String mobile);

    Optional<Object> findByEmail(String email);

    @Query("SELECT u FROM SubAdminModel u WHERE " +
            "(:keyword IS NULL OR LOWER(u.firstName) LIKE %:keyword% OR " +
            "LOWER(u.userName) LIKE %:keyword% OR LOWER(u.email) LIKE %:keyword%) AND " +
            "(:active IS NULL OR u.status = :active) AND " +
            "(:startDate IS NULL OR :endDate IS NULL OR u.createdAt BETWEEN :startDate AND :endDate)")
    List<SubAdminModel> searchAdmin(@Param("keyword") String keyword,
                           @Param("active") String active,
                           @Param("startDate") Long startDate,
                           @Param("endDate") Long endDate);

}
