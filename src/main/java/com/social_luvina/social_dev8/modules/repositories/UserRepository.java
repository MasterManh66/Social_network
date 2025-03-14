package com.social_luvina.social_dev8.modules.repositories;

import org.springframework.stereotype.Repository;

import com.social_luvina.social_dev8.modules.models.entities.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmail(String email);
}
