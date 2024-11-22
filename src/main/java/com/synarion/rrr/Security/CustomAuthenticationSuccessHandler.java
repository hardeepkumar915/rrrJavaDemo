package com.synarion.rrr.Security;

import com.synarion.rrr.Model.SubAdminModel;
import com.synarion.rrr.Repository.SubAdminRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final SubAdminRepository subAdminRepository;

    CustomAuthenticationSuccessHandler(SubAdminRepository subAdminRepository){
        this.subAdminRepository=subAdminRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        HttpSession session = request.getSession();
        // Get authenticated user details
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Optional<SubAdminModel> adminOptional = subAdminRepository.findByUserName(userDetails.getUsername());

        if(adminOptional.isPresent()){
            SubAdminModel adminModel = adminOptional.get();
            // Store custom session attributes
            session.setAttribute("adminId",adminModel.getSubAdminId());
            session.setAttribute("firstName", adminModel.getFirstName());
            session.setAttribute("lastName", adminModel.getLastName());
            session.setAttribute("fullName",adminModel.getFirstName()+" "+adminModel.getLastName());
//            session.setAttribute("role",adminModel.getRole());
            session.setAttribute("mobile",adminModel.getMobile());
            session.setAttribute("email",adminModel.getEmail());


        }
        response.sendRedirect("/rrr/admin/verifyAdmin");
    }
}
