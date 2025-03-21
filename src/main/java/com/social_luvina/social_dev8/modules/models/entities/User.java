package com.social_luvina.social_dev8.modules.models.entities;

import java.util.List;
import java.sql.Date;

import com.social_luvina.social_dev8.modules.models.enums.GenderEnum;

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
@Table(name = "user")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private long id;

  @Column(name = "first_name",nullable = false)
  private String firstName;

  @Column(name = "last_name",nullable = false)
  private String lastName;

  @Column(name = "address")
  private String address;

  @Column(name = "date_of_birth")
  private Date dateOfBirth;

  @Column(name = "job")
  private String job;

  @Column(name = "gender",nullable = false)
  @Enumerated(EnumType.STRING)
  private GenderEnum gender;

  @Lob
  @Column(name = "avatar",columnDefinition = "TEXT")
  private String avatar;
  
  @Column(name = "email",unique = true,nullable = false)
  private String email;

  @Column(name = "password",nullable = false)
  private String password;

  @Column(name = "isActive",nullable = false)
  private boolean isActive;

  @ManyToMany(fetch = FetchType.EAGER,cascade = {CascadeType.REFRESH,CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST})
  @JoinTable(name = "user_role",joinColumns = @JoinColumn(name = "userId"),inverseJoinColumns = @JoinColumn(name = "roleId"))
  private List<Role> roles;

  @OneToMany(cascade = CascadeType.ALL,mappedBy = "user")
  private List<Post> posts;
}
