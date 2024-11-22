package com.synarion.rrr.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name ="sub_admin")
@Data
public class SubAdminModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="sub_admin_id")
    private int subAdminId;

    @NotEmpty(message = "Username is required")
    @Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
    @Column(name = "user_name")
    private String userName;

    @NotEmpty(message = "First Name is required")
    @Column(name="first_name")
    private String firstName;

    @NotEmpty(message = "Last Name is required")
    @Column(name="last_name")
    private String lastName;

    @NotEmpty(message = "Email is required")
    @Email(message = "Please provide a valid email")
    private String email;

    @NotEmpty(message = "Mobile number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must be 10 digits")
    private String mobile;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private GroupModel groupId;

    private int status = 1;

    private String password;

    @Transient
    private String confirmPassword;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss a")
    private LocalDateTime createdAt;

    @Transient
    private String formatedDateTime;

    private String otp;

}
