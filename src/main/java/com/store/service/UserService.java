package com.store.service;


import java.util.List;

import com.store.domain.User;
import com.store.domain.security.UserRole;

public interface UserService {
	
	User findById(Long id);
	
	User findByUsername(String username);
		
	User findByEmail(String email);
		
	void save(User user);
	
	User createUser(String username, String email,  String password, List<String> roles);
//	User createUser(UserRole userRole);
	List<User> getListUser();

	List<UserRole> getListUserRole();

	void deleteUser(Long id_userrole);

}
