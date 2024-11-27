package com.synarion.rrr.Model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="permission_group")
@Data
public class PermissionModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="permission_id")
    private int permissionId;

    @Column(name="permission_name")
    private String permissionName;

    @Column(name = "permission_group")
    private String permissionGroup;

    @Column(name="created_at")
    private long createdAt;
}
