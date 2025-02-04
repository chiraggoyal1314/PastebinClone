package com.fastbin.app.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fastbin.app.Models.RegisterDto;
import com.fastbin.app.entity.Role;
import com.fastbin.app.entity.User;
import com.fastbin.app.repository.RoleRepository;
import com.fastbin.app.repository.UserRepository;
import com.fastbin.app.security.JwtHelper;

import java.util.Collections;
import java.util.List;

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
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtHelper jwtHelper;

    // @Autowired
    // public UserController(AuthenticationManager authenticationManager, UserRepository userRepository){
    //     this.authenticationManager = authenticationManager;
    //     this.userRepository = userRepository;
    //     this.roleRepository = roleRepository;
    //     this.passwordEncoder = passwordEncoder;
    // }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody RegisterDto model) {
        // if(userRepository.existsByUsername(model.getUsername()) == false){
        //     return new ResponseEntity<>("User not found in database.",HttpStatus.BAD_REQUEST);
        // }
        // User user = userRepository.findByUsername(model.getUsername()).orElse(null);
        // Boolean isSame = passwordEncoder.matches(model.getPassword(),user.getPassword());
        // if(user != null && isSame){
        //     return new ResponseEntity<>("User found in database.",HttpStatus.OK);
        // }
        // return new ResponseEntity<>("Passsword not correct",HttpStatus.BAD_REQUEST);
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(model.getUsername(), model.getPassword()));
        System.out.println(authentication.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtHelper.generateToken(authentication);
        return new ResponseEntity<>("User signed success : " + token,HttpStatus.OK);
    }
    

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterDto model){
        if(userRepository.existsByUsername(model.getUsername())){
            return new ResponseEntity<>("User already registered",HttpStatus.BAD_REQUEST);
        }
        User user = new User();
        user.setUsername(model.getUsername());
        user.setPassword(passwordEncoder.encode(model.getPassword()));

        Role roles = roleRepository.findByName("USER").get();
        user.setRoles(Collections.singletonList(roles));

        userRepository.save(user);

        return new ResponseEntity<>("User regitered successfully",HttpStatus.OK);
    }

    @GetMapping("/allUsers")
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users;
    }
    
    
}
