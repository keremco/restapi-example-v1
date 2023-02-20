package com.digitalpaintsoft.restapiexampletask.Controller;

import com.digitalpaintsoft.restapiexampletask.Entity.User;
import com.digitalpaintsoft.restapiexampletask.Entity.Role;
import com.digitalpaintsoft.restapiexampletask.Repo.RoleRepo;
import com.digitalpaintsoft.restapiexampletask.Repo.UserRepo;
import com.digitalpaintsoft.restapiexampletask.DTO.LoginDTO;
import com.digitalpaintsoft.restapiexampletask.DTO.SignupDTO;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class Controller {

	@Autowired
    private AuthenticationManager authenticationManager;
	
	@Autowired
    private UserRepo userRepo;
	
	@Autowired
    private RoleRepo roleRepository;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@PostMapping("/signin")
    public ResponseEntity<String> authenticateUser(@RequestBody LoginDTO loginDto){
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(), loginDto.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		return new ResponseEntity<>("Giriş Başarılı", HttpStatus.OK);
    }
	
	@PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupDTO signUpDto){

        
        if(userRepo.existsByUsername(signUpDto.getUsername())){
            return new ResponseEntity<>("Bu kullanıcı adı mevcut!", HttpStatus.BAD_REQUEST);
        }

        
        if(userRepo.existsByEmail(signUpDto.getEmail())){
            return new ResponseEntity<>("Bu email mevcut!", HttpStatus.BAD_REQUEST);
        }

        
        User user = new User();
        user.setName(signUpDto.getName());
        user.setUsername(signUpDto.getUsername());
        user.setEmail(signUpDto.getEmail());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));

        Role roles = roleRepository.findByName("ROLE_ADMIN").get();
        user.setRoles(Collections.singleton(roles));
        
        userRepo.save(user);

        return new ResponseEntity<>("Kayıt tamamlandı!", HttpStatus.OK);

    }
	
}
