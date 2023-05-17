package com.store.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.store.domain.Address;
import com.store.repository.UserRoleRepository;
import org.hibernate.annotations.Cascade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.store.domain.User;
import com.store.domain.security.Role;
import com.store.domain.security.UserRole;
import com.store.repository.RoleRepository;
import com.store.repository.UserRepository;
import com.store.service.UserService;

import utility.SecurityUtility;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserRoleRepository userRoleRepository;
	
	@Autowired
	private RoleRepository roleRepository;

	@Override
	public User findById(Long id) {
		Optional<User> opt = userRepository.findById(id);
		return opt.get();
	}
	
	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}
	

	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public void save(User user) {
		userRepository.save(user);
	}

	@Override
	@Transactional
	public User createUser(String username, String password, String email, List<String> roles) {
		User user = findByUsername(username);
		if (user != null) {
			return user;
		} else {
			user = new User();
			user.setUsername(username);
			user.setPassword(SecurityUtility.passwordEncoder().encode(password));
			user.setEmail(email);
			Set<UserRole> userRoles = new HashSet<>();
			for (String rolename : roles) {
				Role role = roleRepository.findByName(rolename);
				if (role == null) {
					role = new Role();
					role.setName(rolename);
					roleRepository.save(role);
				}
				userRoles.add(new UserRole(user, role));
			}
			user.setUserRoles(userRoles);
			return userRepository.save(user);
		}
	}

//	@Override
//	@Transactional
//	public User createUser(String username, String password, String email, List<String> roles) {
//		User user = findByUsername(username);
//		if (user != null) {
//			return user;
//		} else {
//			user = new User();
//			user.setUsername(username);
//			user.setPassword(SecurityUtility.passwordEncoder().encode(password));
//			user.setEmail(email);
////			user.setFirstName("");
////			user.setLastName("");
////			user.setAddress(new Address(11L,"", "", "", ""));
//			Set<UserRole> userRoles = new HashSet<>();
//			for (String rolename : roles) {
//				Role role = roleRepository.findByName(rolename);
//				if (role == null) {
//					role = new Role();
//					role.setName(rolename);
//					roleRepository.save(role);
//				}
//				userRoles.add(new UserRole(user, role));
//			}
//			user.setUserRoles(userRoles);
//			return userRepository.save(user);
//		}
//	}

	@Override
	public List<User> getListUser() {
		List<User> list = (List<User>) userRepository.findAll();
		return list;
	}

	@Override
	public List<UserRole> getListUserRole() {
		List<UserRole> userRoleList = (List<UserRole>) userRoleRepository.findAll();
		return userRoleList;

	}

	@Override
	public void deleteUser(Long id_userrole) {
		UserRole userRole = userRoleRepository.findById(Long.valueOf(id_userrole)).get();
		User u = userRepository.findById(userRole.getUser().getId()).get();
		userRoleRepository.deleteById( id_userrole);
		userRepository.deleteById(u.getId());

	}

}
