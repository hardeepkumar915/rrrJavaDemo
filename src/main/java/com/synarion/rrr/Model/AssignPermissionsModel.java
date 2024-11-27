package com.synarion.rrr.Model;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "assign_permission_table")
@Data
public class AssignPermissionsModel {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long assignPermissionId;

        @ManyToOne
        @JoinColumn(name = "group_id")
        private GroupModel group;

        @ManyToOne
        @JoinColumn(name = "permission_id")
        private PermissionModel permission;


    }
