package com.social_luvina.social_dev8.modules.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.social_luvina.social_dev8.modules.models.enums.PostStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "post")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "post_id")
  private long id;

  @Column(name = "title")
  private String title;

  @Column(name = "content")
  private String content;

  @Column(name = "create_at")
  private LocalDateTime createdAt;

  @Column(name = "update_at")
  private LocalDateTime updatedAt;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private PostStatus postStatus;

  @ElementCollection
  @CollectionTable(name = "post_images", joinColumns = @JoinColumn(name = "post_id"))
  @Column(name = "image_path")
  private List<String> images;

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.REFRESH,CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST})
  @JoinColumn(name = "user_id")
  private User user;

  @OneToMany(cascade = CascadeType.ALL,mappedBy = "post")
  private List<Comment> comments;
}
