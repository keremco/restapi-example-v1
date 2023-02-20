package com.digitalpaintsoft.restapiexampletask.Service;

import com.digitalpaintsoft.restapiexampletask.Entity.User;
import com.digitalpaintsoft.restapiexampletask.Repo.UserRepo;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LoginUserDetailsService implements UserDetailsService {

	private UserRepo userRepo;
	
	public LoginUserDetailsService(UserRepo userRepo) {
		this.userRepo = userRepo;
	}
	
	@Override
	public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
		User user = userRepo.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
							.orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı"));
			
		Set<GrantedAuthority> authorities = user
												.getRoles()
												.stream()
												.map((role) -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toSet());
			
		return new org.springframework.security.core.userdetails.User(user.getEmail(), 
																	  user.getPassword(),
																	  authorities);
	}

}
