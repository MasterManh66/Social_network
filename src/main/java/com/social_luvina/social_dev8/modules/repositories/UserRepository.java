package com.social_luvina.social_dev8.modules.repositories;

import org.springframework.stereotype.Repository;

import com.social_luvina.social_dev8.modules.models.entities.User;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmail(String email);

  @Query("SELECT u FROM User u WHERE " + "LOWER(CONCAT(u.firstName, ' ', u.lastName)) LIKE LOWER(CONCAT('%', :keyword, '%'))")
  List<User> searchByKeyword(@Param("keyword") String keyword);
}
