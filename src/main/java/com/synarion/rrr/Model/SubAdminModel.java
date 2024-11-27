package com.synarion.rrr.Model;

import com.synarion.rrr.Service.Utils;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.annotations.CurrentTimestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.time.LocalDateTime;
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

    private String status = "Y";

    private String password;

    @Transient
    private String confirmPassword;


    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss a")
    @Column(name="created_at")
    private Long createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = Utils.getCurrentTime();
    }


    @Transient
    private String formatedDateTime;

    private String otp;

}
