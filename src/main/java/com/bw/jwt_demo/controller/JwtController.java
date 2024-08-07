package com.bw.jwt_demo.controller;

import com.bw.jwt_demo.model.JwtRequest;
import com.bw.jwt_demo.model.JwtResponse;
import com.bw.jwt_demo.model.UserModel;
import com.bw.jwt_demo.service.CustomUserDetailsService;
import com.bw.jwt_demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController()
@RequestMapping("/api")
public class JwtController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<UserModel> register(@RequestBody UserModel userModel) {
        UserModel userModel1 = customUserDetailsService.register(userModel);
        ResponseEntity<UserModel> responseEntity = new ResponseEntity<>(userModel1, HttpStatus.CREATED);
        return responseEntity;
    }

    @PostMapping("/login")
    public ResponseEntity<?> generateToken(@RequestBody JwtRequest jwtRequest){

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                jwtRequest.getUserName(), jwtRequest.getPassword()));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(jwtRequest.getUserName());

        String jwtToken = jwtUtil.generateToken(userDetails);

        JwtResponse jwtResponse = new JwtResponse(jwtToken);

        return ResponseEntity.ok(jwtResponse);
    }

    @GetMapping("/currentUser")
    public UserModel getCurrentUser(Principal principal) {
        return ((UserModel) this.customUserDetailsService.loadUserByUsername(
                principal.getName()));
    }
}
