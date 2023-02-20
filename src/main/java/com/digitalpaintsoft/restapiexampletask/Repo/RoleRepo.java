package com.digitalpaintsoft.restapiexampletask.Repo;

import java.util.Optional;

import com.digitalpaintsoft.restapiexampletask.Entity.Role;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role, Long> {
	    Optional<Role> findByName(String name);
	}

