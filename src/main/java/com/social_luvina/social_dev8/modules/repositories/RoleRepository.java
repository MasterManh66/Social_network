package com.social_luvina.social_dev8.modules.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.social_luvina.social_dev8.modules.models.entities.Role;
import java.util.Optional;

public interface RoleRepository  extends JpaRepository<Role, Long>{

  Optional<Role> findByRoleName(String roleName);
}