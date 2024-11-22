package com.synarion.rrr.Repository;

import com.synarion.rrr.Model.GroupModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<GroupModel,Integer> {

}
