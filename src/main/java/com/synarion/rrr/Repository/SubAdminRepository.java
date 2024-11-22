package com.synarion.rrr.Repository;

import com.synarion.rrr.Model.SubAdminModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubAdminRepository extends JpaRepository<SubAdminModel, Integer> {

    Optional<SubAdminModel> findByUserName(String userName);

    Optional<Object> findByMobile(String mobile);

    Optional<Object> findByEmail(String email);

}
