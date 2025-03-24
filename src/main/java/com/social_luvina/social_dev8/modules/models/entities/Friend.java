package com.social_luvina.social_dev8.modules.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import com.social_luvina.social_dev8.modules.models.enums.FriendStatus;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "friend")
public class Friend {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "friend_id")
  private long id;

  @Column(name = "status",nullable = false)
  @Enumerated(EnumType.STRING)
  private FriendStatus friendStatus;

  @Column(name = "created_at",nullable = false,updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @ManyToOne
  @JoinColumn(name = "requester_id", referencedColumnName = "user_id")
  private User requester;

  @ManyToOne
  @JoinColumn(name = "receiver_id",referencedColumnName = "user_id")
  private User receiver;

  @PrePersist
  protected void onCreate() {
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate(){
    this.updatedAt = LocalDateTime.now();
  }
}
