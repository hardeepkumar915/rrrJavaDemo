package com.synarion.rrr.Service;

import com.synarion.rrr.Model.SubAdminModel;
import com.synarion.rrr.Repository.SubAdminRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class SubAdminService implements UserDetailsService {

    private final SubAdminRepository subAdminRepository;

    private final PasswordEncoder passwordEncoder;

    public SubAdminService(SubAdminRepository subAdminRepository,PasswordEncoder passwordEncoder){
        this.subAdminRepository = subAdminRepository;
        this.passwordEncoder    = passwordEncoder;
    }

    public Optional<SubAdminModel> findByUserName(String userName){
        return subAdminRepository.findByUserName(userName);
    }

    public void save(SubAdminModel subAdminModel){
        subAdminRepository.save(subAdminModel);
    }

    public SubAdminModel saveSubAdmin(SubAdminModel subAdminModel) throws Exception{

        // Encode password
        subAdminModel.setPassword(passwordEncoder.encode(subAdminModel.getPassword()));
        return subAdminRepository.save(subAdminModel);
    }

    public List<SubAdminModel> getAllAdmins() {
        return subAdminRepository.findAll();
    }

    public void deleteAdmin(int id){
        subAdminRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        SubAdminModel admin = subAdminRepository.findByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User not found : " + userName));

        System.out.println("usename -> "+ admin.getUserName());
        System.out.println("password -> "+ passwordEncoder.encode(admin.getPassword()));

        return User.builder()
                .username(admin.getUserName())
                .password(admin.getPassword())
                .build();
    }

    public SubAdminModel findById(int id) {
        return subAdminRepository.findById(id).orElse(null);
    }


}
