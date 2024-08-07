package com.bw.jwt_demo.controller;

import com.bw.jwt_demo.entity.RoleEntity;
import com.bw.jwt_demo.model.RoleModel;
import com.bw.jwt_demo.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RoleController {

    @Autowired
    private RoleService roleService;
    @PostMapping("/roles")
    public RoleModel createRole(@RequestBody RoleModel roleModel) {
        return roleService.createRole(roleModel);
    }

    @GetMapping("/roles")
    public List<RoleModel> getAllRoles() {
        return roleService.getAllRoles();
    }

    @DeleteMapping("/roles/{roleId}")
    public void deleteRoleById(@PathVariable  Long roleId) {
         roleService.deleteRoleById(roleId);
    }

    @DeleteMapping("/roles")
    public void deleteRoles() {
        roleService.deleteRoles();
    }
}
