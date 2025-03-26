package com.social_luvina.social_dev8.modules.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.social_luvina.social_dev8.modules.models.entities.Otp;
import com.social_luvina.social_dev8.modules.models.entities.User;

import jakarta.transaction.Transactional;

@Repository
public interface OtpRepository extends JpaRepository<Otp, Long>{
  Optional<Otp> findByOtpCodeAndIsUsedFalse(String otpCode);

  @Transactional
  @Modifying
  @Query("DELETE FROM Otp o WHERE o.user = :user")
  void deleteAllOtpsByUser(@Param("user") User user);
}
