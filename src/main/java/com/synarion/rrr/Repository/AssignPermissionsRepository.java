package com.synarion.rrr.Repository;

import com.synarion.rrr.Model.AssignPermissionsModel;
import com.synarion.rrr.Model.GroupModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface AssignPermissionsRepository extends JpaRepository<AssignPermissionsModel,Integer> {
    List<AssignPermissionsModel> findByGroup(GroupModel group);


}
