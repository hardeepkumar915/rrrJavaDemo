package com.synarion.rrr.Service;

import com.synarion.rrr.Model.GroupModel;
import com.synarion.rrr.Model.PermissionModel;
import com.synarion.rrr.Model.SubAdminModel;
import com.synarion.rrr.Repository.GroupRepository;
import com.synarion.rrr.Repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    public List<GroupModel> getAllGroups(){
        return groupRepository.findAll();
    }

    public Map<String, List<Map<String, Object>>> getPermissionsGroupedByGroup() {
        List<PermissionModel> permissions = permissionRepository.findAllByOrderByPermissionGroupAsc();

        // Group permissions by permission_group
        return permissions.stream()
                .collect(Collectors.groupingBy(
                        PermissionModel::getPermissionGroup,
                        LinkedHashMap::new,
                        Collectors.mapping(
                                permission -> {
                                    Map<String, Object> permissionDetails = new HashMap<>();
                                    permissionDetails.put("permissionId", permission.getPermissionId());
                                    permissionDetails.put("permissionName", permission.getPermissionName());
                                    return permissionDetails;
                                },
                                Collectors.toList())
                ));
    }

    public GroupModel findById(int id) {
        return groupRepository.findById(id).orElse(null);
    }

    public void save(GroupModel groupModel) {
        groupRepository.save(groupModel);
    }
}
