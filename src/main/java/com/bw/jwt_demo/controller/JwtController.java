package com.bw.jwt_demo.controller;

import com.bw.jwt_demo.model.JwtRequest;
import com.bw.jwt_demo.model.JwtResponse;
import com.bw.jwt_demo.service.CustomUserDetailsService;
import com.bw.jwt_demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api")
public class JwtController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;



    @PostMapping("/generateToken")
    public ResponseEntity<?> generateToken(@RequestBody JwtRequest jwtRequest){

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                jwtRequest.getUserName(), jwtRequest.getPassword()));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(jwtRequest.getUserName());

        String jwtToken = jwtUtil.generateToken(userDetails);

        JwtResponse jwtResponse = new JwtResponse(jwtToken);

        return ResponseEntity.ok(jwtResponse);
    }
}
