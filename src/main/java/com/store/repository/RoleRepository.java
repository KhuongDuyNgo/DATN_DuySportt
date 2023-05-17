package com.store.repository;

import org.springframework.data.repository.CrudRepository;

import com.store.domain.security.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {
	
	Role findByName(String name);

}
