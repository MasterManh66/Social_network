package com.social_luvina.social_dev8.modules.models.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "role")
public class Role {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "role_id")
  private Long id;

  @Column(name = "role_name",nullable = false)
  private String roleName;

  @JsonIgnore
  @ManyToMany(fetch = FetchType.LAZY,cascade = {CascadeType.REFRESH,CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST})
  @JoinTable(name = "user_role",joinColumns = @JoinColumn(name = "role_id"),inverseJoinColumns = @JoinColumn(name = "user_id"))
  private List<User> users;

}
