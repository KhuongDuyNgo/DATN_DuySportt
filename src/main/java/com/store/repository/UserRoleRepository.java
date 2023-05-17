package com.store.repository;

import com.store.domain.User;
import com.store.domain.security.UserRole;
import org.springframework.data.repository.CrudRepository;

public interface UserRoleRepository extends CrudRepository<UserRole, Long> {
}
