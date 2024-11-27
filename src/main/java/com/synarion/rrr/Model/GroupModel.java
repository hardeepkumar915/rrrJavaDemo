package com.synarion.rrr.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name="group_table")
@Data
public class GroupModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "group_id")
    private int groupId;

    @Column(name = "group_name")
    private String groupName;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    private int status = 1;

    @Transient
    private String formatedDateTime;
}
