package com.synarion.rrr.Controller;

import com.synarion.rrr.Model.GroupModel;
import com.synarion.rrr.Repository.GroupRepository;
import com.synarion.rrr.Service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/rrr/admin")
public class GroupController {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupService groupService;

    @GetMapping("/group-list")
    public String showGroupList(Model model){
        List<GroupModel> groupList = groupService.getAllGroups();

        for(GroupModel group : groupList){
            LocalDateTime localDateTime = group.getCreatedAt();
            String formattedDate = localDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss a"));
            group.setFormatedDateTime(formattedDate);
        }

        model.addAttribute("groups",groupList);
        return "groupList";
    }

}
