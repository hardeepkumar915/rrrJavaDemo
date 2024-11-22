package com.synarion.rrr.Controller;

import com.synarion.rrr.Model.GroupModel;
import com.synarion.rrr.Model.SubAdminModel;
import com.synarion.rrr.Repository.SubAdminRepository;
import com.synarion.rrr.Service.GroupService;
import com.synarion.rrr.Service.SubAdminService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/rrr/admin")
public class SubAdminController {

    @Autowired
    private SubAdminService subAdminService;

    @Autowired
    private SubAdminRepository subAdminRepository;

    @Autowired
    private GroupService groupService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String noEndPoint(){
        return "login";
    }

    @GetMapping("/dashboard")
    public String showDashboardPage(){
        return "index";
    }

    @GetMapping("/login")
    public String showLoginPage(){
        System.out.print("show login page -> ");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        System.out.println(auth);

        if(!auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {

            System.out.println("in auth method");

            return "login";
        }
        return "redirect:/rrr/admin/dashboard";
    }

//    @PostMapping("/login")
//    public String loginAdmin(@RequestParam Map<String, String> formData, Model model){
//        Optional<SubAdminModel> subAdminModel = subAdminService.findByUserName(formData.get("userName"));
//
//        if(subAdminModel.isPresent() && formData.get("password").equals(subAdminModel.get().getPassword())){
//            return "redirect:/rrr/admin/dashboard";
//        }else{
//            model.addAttribute("error", "Username or Password is invalid");
//            return "login";
//        }
//    }

    @GetMapping("/verifyAdmin")
    public String showOtpPage(){
        return "otpPage";
    }

    @PostMapping("/verifyAdmin")
    public String verifyAdmin(@RequestParam String otp, HttpSession session,Model model){
        int adminId = (int)session.getAttribute("adminId");
        Optional<SubAdminModel> subAdminModel = subAdminRepository.findById(adminId);

        if(subAdminModel.get().getOtp().equals(otp)){
            return "redirect:/rrr/admin/dashboard";
        }else{
            model.addAttribute("error","Invalid Otp");
            return "otpPage";
        }
    }

    @GetMapping("/subAdminAdd")
    public String showSubAdminAddPage(Model model){
        List<GroupModel> groupList = groupService.getAllGroups();
        model.addAttribute("groupList",groupList);
        model.addAttribute("addAdmin",new SubAdminModel());
        return "subAdminAdd";
    }

    @PostMapping("/subAdminAdd")
    public String addSubAdmin(@Valid @ModelAttribute("addAdmin") SubAdminModel subAdminModel, BindingResult bindingResult, Model model) throws IOException {
        if(bindingResult.hasErrors()){
            List<GroupModel> groupList = groupService.getAllGroups();
            model.addAttribute("groupList",groupList);
            return "subAdminAdd";
        }

        if(!subAdminModel.getPassword().equals(subAdminModel.getConfirmPassword()) || subAdminModel.getPassword().isEmpty()){
            List<GroupModel> groupList = groupService.getAllGroups();
            model.addAttribute("groupList",groupList);
            model.addAttribute("passwordError","password do not match");
            model.addAttribute("passwordRequired","password is required");
            return "subAdminAdd";
        }

        if(subAdminRepository.findByMobile(subAdminModel.getMobile()).isPresent()){
            model.addAttribute("mobileError","mobile number already in use");
            return "subAdminAdd";
        }

        if(subAdminRepository.findByEmail(subAdminModel.getEmail()).isPresent()){
            model.addAttribute("emailError","Email already in use");
            return "subAdminAdd";
        }
        if(subAdminModel.getGroupId()==null){
            model.addAttribute("groupError","please choose admin group ");
            return "subAdminAdd";
        }

        try{
            subAdminService.saveSubAdmin(subAdminModel);
        }catch(Exception e){
            System.out.println("subadmin add error -> "+e);
            List<GroupModel> groupList = groupService.getAllGroups();
            model.addAttribute("groupList",groupList);
            model.addAttribute("error","something went wrong, try again later");
            return "subAdminAdd";
        }
        List<GroupModel> groupList = groupService.getAllGroups();
        model.addAttribute("groupList",groupList);
        model.addAttribute("success", "Sub Admin added successfully ");
        return "subAdminAdd";
    }

    @GetMapping("/reset-admin")
    public String resetForm(Model model) {
        List<GroupModel> groupList = groupService.getAllGroups();
        model.addAttribute("groupList",groupList);
        model.addAttribute("addAdmin", new SubAdminModel()); // Reset to a new User object
        return "subAdminAdd";
    }

    @GetMapping("/subAdmin-list")
    public String showSubAdminList(Model model){
        List<SubAdminModel> subAdmins = subAdminService.getAllAdmins();

        for(SubAdminModel subAdmin : subAdmins){
            LocalDateTime localDateTime = subAdmin.getCreatedAt();
            String formattedDate = localDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss a"));
            subAdmin.setFormatedDateTime(formattedDate);
        }

        model.addAttribute("subAdmins",subAdmins);
        return "subAdminReport";
    }

    @PostMapping("/toggle/{id}")
    @ResponseBody // Indicates that the return value should be used as the response body
    public ResponseEntity<SubAdminModel> toggleUserStatus(@PathVariable int id) throws Exception {
        SubAdminModel admin = subAdminService.findById(id); // Fetch the user

        if (admin != null) {
            if(admin.getStatus()==1){
                // Toggle the active status
                admin.setStatus(0);

            }else{
                admin.setStatus(1);
            }
            subAdminService.save(admin); // Save the user with updated status

            return ResponseEntity.ok(admin); // Return the updated user as JSON
        }
        return ResponseEntity.notFound().build(); // Return 404 if user not found
    }

    @GetMapping("/change-password/{id}")
    public String showChangePassword(@PathVariable int id,Model model){
        model.addAttribute("changePasswordAdmin", new SubAdminModel());
        model.addAttribute("subAdminId",id);
        return "changePassword";
    }

    @PostMapping("/change-password/{id}")
    public String changePassword(@PathVariable int id, @ModelAttribute("changePasswordAdmin") SubAdminModel subAdminModel, BindingResult bindingResult, RedirectAttributes redirectAttributes,Model model){

        SubAdminModel subAdminFind = subAdminService.findById(id);

        if(!subAdminModel.getPassword().equals(subAdminModel.getConfirmPassword())){
            redirectAttributes.addFlashAttribute("passwordError","password do not match !");
            return "redirect:/rrr/admin/change-password/"+id;
        }

        try{
            subAdminFind.setPassword(passwordEncoder.encode(subAdminModel.getPassword()));
            subAdminService.save(subAdminFind);
            redirectAttributes.addFlashAttribute("success", "change Password Successful");
            return "redirect:/rrr/admin/subAdmin-list";
        }catch(Exception e){
            System.out.println("error -> " +e.getMessage());
            redirectAttributes.addFlashAttribute("error","something went wrong, please try again later");
            return "redirect:/rrr/admin/change-password/"+id;
        }
    }

    @GetMapping("edit-admin/{id}")
    public String showEditAdminPage(@PathVariable int id,Model model){
        model.addAttribute("subAdminId",id);

        SubAdminModel subAdminModel = subAdminService.findById(id);

        List<GroupModel> groupList = groupService.getAllGroups();
        model.addAttribute("groupList",groupList);
        model.addAttribute("editAdmin", subAdminModel);
        return "editAdmin";
    }


    @PostMapping("edit-admin/{id}")
    public String editSubAdmin(@PathVariable int id,@Valid @ModelAttribute("editAdmin") SubAdminModel editAdminData,RedirectAttributes redirectAttributes, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            System.out.println("edit error binding -> "+bindingResult.getAllErrors());
            List<GroupModel> groupList = groupService.getAllGroups();
            model.addAttribute("editAdmin",editAdminData);
            redirectAttributes.addFlashAttribute("groupList",groupList);
            return "edit-admin/"+id;
        }


        try{
            SubAdminModel subAdminModel = subAdminService.findById(id);

            subAdminModel.setUserName(editAdminData.getUserName());
            subAdminModel.setFirstName(editAdminData.getFirstName());
            subAdminModel.setLastName(editAdminData.getLastName());
            subAdminModel.setEmail(editAdminData.getEmail());
            subAdminModel.setMobile(editAdminData.getMobile());
            subAdminModel.setGroupId(editAdminData.getGroupId());
            subAdminService.save(subAdminModel);
        }catch(Exception e){
            List<GroupModel> groupList = groupService.getAllGroups();
            redirectAttributes.addFlashAttribute("groupList",groupList);
            redirectAttributes.addFlashAttribute("error","something went wrong, try again later");
            return "redirect:/rrr/admin/edit-admin/"+id;
        }
        List<GroupModel> groupList = groupService.getAllGroups();
        redirectAttributes.addFlashAttribute("groupList",groupList);
        redirectAttributes.addFlashAttribute("success", "Sub Admin edited successfully ");
        return "redirect:/rrr/admin/edit-admin/"+id;
    }

    @GetMapping("/delete-admin/{id}")
    public String deleteAdmin(@PathVariable int id, RedirectAttributes redirectAttributes){
        SubAdminModel subAdminModel = subAdminService.findById(id);

        if(subAdminModel != null){
            subAdminService.deleteAdmin(id);
        }
        redirectAttributes.addFlashAttribute("success","admin delete successful");
        return "redirect:/rrr/admin/subAdmin-list";
    }
}
