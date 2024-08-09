package com.bw.jwt_demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @PreAuthorize("hasRole('ADMIN')") //Spring boot automatically prefixes ROLE_
    @GetMapping("/hello")
    public String sayHello() {
        return "Hello from HomeController";
    }
}
