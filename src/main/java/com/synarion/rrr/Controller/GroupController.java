package com.synarion.rrr.Controller;

import com.synarion.rrr.Model.AssignPermissionsModel;
import com.synarion.rrr.Model.GroupModel;
import com.synarion.rrr.Model.PermissionModel;
import com.synarion.rrr.Model.SubAdminModel;
import com.synarion.rrr.Repository.AssignPermissionsRepository;
import com.synarion.rrr.Repository.GroupRepository;
import com.synarion.rrr.Repository.PermissionRepository;
import com.synarion.rrr.Service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/rrr/admin")
public class GroupController {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupService groupService;

    @Autowired
    private AssignPermissionsRepository assignPermissionsRepository;

//    @Autowired
//    private AssignPermissionsModel assignPermissionsModel;

    @Autowired
    private PermissionRepository permissionRepository;

    @GetMapping("/group-list")
    public String showGroupList(Model model){
        List<GroupModel> groupList = groupService.getAllGroups();

        for(GroupModel group : groupList){
            LocalDateTime localDateTime = group.getCreatedAt();
            String formattedDate = localDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss a"));
            group.setFormatedDateTime(formattedDate);
        }

        model.addAttribute("groups",groupList);
        return "groupList";
    }

    @PostMapping("/group-list/status/{id}")
    @ResponseBody // Indicates that the return value should be used as the response body
    public ResponseEntity<GroupModel> toggleUserStatus(@PathVariable int id) throws Exception {
        GroupModel admin = groupService.findById(id); // Fetch the user

        if (admin != null) {
            if(admin.getStatus()==1){
                // Toggle the active status
                admin.setStatus(0);

            }else{
                admin.setStatus(1);
            }
            groupService.save(admin); // Save the user with updated status

            return ResponseEntity.ok(admin); // Return the updated user as JSON
        }
        return ResponseEntity.notFound().build(); // Return 404 if user not found
    }

    @GetMapping("/add-group")
    public String showPermissions(Model model) {
        Map<String, List<Map<String, Object>>> groupedPermissions = groupService.getPermissionsGroupedByGroup();
        model.addAttribute("groupedPermissions", groupedPermissions);
        model.addAttribute("newGroup", new GroupModel());
        model.addAttribute("selectedPermissions", Collections.emptyList());
        return "addGroup";
    }

    @PostMapping("/add-group")
    public String assignPermissions(@ModelAttribute("newGroup") GroupModel group,
        @RequestParam(value = "selectedPermissions", required = false) List<Integer> permissionIds ,RedirectAttributes redirectAttributes) {

        final GroupModel savedGroup = groupRepository.save(group);

        assignPermissionsRepository.deleteAll(assignPermissionsRepository.findByGroup(group));

        if(permissionIds != null){
            List<AssignPermissionsModel> assignPermissionsModels = permissionIds.stream()
                    .map(permissionId->{
                        AssignPermissionsModel assignPermissionsModel1 = new AssignPermissionsModel();
                        assignPermissionsModel1.setGroup(savedGroup);
                        assignPermissionsModel1.setPermission(permissionRepository.findById(permissionId).orElseThrow());
                        return assignPermissionsModel1;
                    }).collect(Collectors.toList());
            assignPermissionsRepository.saveAll(assignPermissionsModels);
        }
        redirectAttributes.addFlashAttribute("success","Group saved successfully");
        return "redirect:/rrr/admin/group-list";
    }

    @GetMapping("/update-group/{groupId}")
    public String showUpdateGroupPage(@PathVariable int groupId ,Model model){
        GroupModel group = groupRepository.findById(groupId).orElseThrow();
        List<AssignPermissionsModel> assignedPermissions =  assignPermissionsRepository.findByGroup(group);

        List<Integer> selectedPermissions = assignedPermissions.stream()
                .map(assignPermission -> assignPermission.getPermission().getPermissionId())
                .collect(Collectors.toList());

        // Prepare data for the form
        model.addAttribute("newGroup", group);
        model.addAttribute("groupedPermissions", permissionRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(PermissionModel::getPermissionGroup)));
        model.addAttribute("selectedPermissions", selectedPermissions);

        return "groupUpdate";
    }

    @PostMapping("/update-group/{groupId}")
    public String updateGroup(@PathVariable int groupId,
                              @RequestParam("groupName") String groupName,
                              @RequestParam(value = "selectedPermissions", required = false) List<Integer> selectedPermissionIds,
                              RedirectAttributes redirectAttributes) {
        // Retrieve the group to update
        GroupModel group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found with ID: " + groupId));

        // Update group name
        group.setGroupName(groupName);
        groupRepository.save(group);

        // Remove existing permissions assigned to the group
        assignPermissionsRepository.deleteAll(assignPermissionsRepository.findByGroup(group));

        // Assign new permissions to the group
        if (selectedPermissionIds != null) {
            List<AssignPermissionsModel> newAssignments = selectedPermissionIds.stream()
                    .map(permissionId -> {
                        AssignPermissionsModel assignPermission = new AssignPermissionsModel();
                        assignPermission.setGroup(group);
                        assignPermission.setPermission(permissionRepository.findById(permissionId)
                                .orElseThrow(() -> new RuntimeException("Permission not found with ID: " + permissionId)));
                        return assignPermission;
                    })
                    .collect(Collectors.toList());
            assignPermissionsRepository.saveAll(newAssignments);
        }
        redirectAttributes.addFlashAttribute("success","Group update successfully");
        return "redirect:/rrr/admin/group-list";
    }

    @GetMapping("/delete-group/{groupId}")
    public String deleteGroup(@PathVariable int groupId,RedirectAttributes redirectAttributes) {
        // Retrieve the group by ID
        GroupModel group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found with ID: " + groupId));

        // Delete the group
        groupRepository.delete(group);

        // Remove all associated permissions for the group
        assignPermissionsRepository.deleteAll(assignPermissionsRepository.findByGroup(group));


        redirectAttributes.addFlashAttribute("success","Group deleted successfully");
        return "redirect:/rrr/admin/group-list";
    }
}
