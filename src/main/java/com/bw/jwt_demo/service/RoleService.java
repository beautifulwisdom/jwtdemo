package com.bw.jwt_demo.service;

import com.bw.jwt_demo.model.RoleModel;

import java.util.List;

public interface RoleService {
    public RoleModel createRole(RoleModel roleEntity);
    public List<RoleModel> getAllRoles();
    public RoleModel getRoleById(Long roleId);

    public void deleteRoleById(Long roleId);

    public void deleteRoles();

}
