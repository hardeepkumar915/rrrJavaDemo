package com.synarion.rrr.Repository;

import com.synarion.rrr.Model.PermissionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionRepository extends JpaRepository<PermissionModel,Integer> {
    List<PermissionModel> findAllByOrderByPermissionGroupAsc();
}
