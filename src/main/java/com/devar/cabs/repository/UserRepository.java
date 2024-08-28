package com.devar.cabs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devar.cabs.entity.UserEntity;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long>{

	Optional<UserEntity> findByUsername(String username);
}
