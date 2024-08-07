package com.bw.jwt_demo.service;

import com.bw.jwt_demo.entity.RoleEntity;
import com.bw.jwt_demo.model.RoleModel;
import com.bw.jwt_demo.repository.RoleRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    /**
     * We cannot return RoleEntity directory as it cannot be Serialized to JSON
     * So we create corresponding Model class for the Entity and use BeanUtils to copy
     * the properties between Entity to Model or vice-versa
     */
    @Autowired
    private RoleRepository roleRepository;
    @Override
    public RoleModel createRole(RoleModel roleModel) {
        RoleEntity roleEntity = new RoleEntity();
        BeanUtils.copyProperties(roleModel, roleEntity);
        RoleEntity roleEntity1 = roleRepository.save(roleEntity);
        BeanUtils.copyProperties(roleEntity1, roleModel);
        return roleModel;
    }

    @Override
    public List<RoleModel> getAllRoles() {
        List<RoleEntity> roleEntities =  roleRepository.findAll();
        List<RoleModel> roleModels = new ArrayList<>();
        RoleModel rm = null;
        for (RoleEntity re : roleEntities) {
            rm = new RoleModel();
            BeanUtils.copyProperties(re, rm);
            roleModels.add(rm);
        }
        //BeanUtils.copyProperties(roleEntities, roleModels);
        return roleModels;
    }

    @Override
    public RoleModel getRoleById(Long roleId) {
        RoleModel roleModel = new RoleModel();
        RoleEntity roleEntity =  roleRepository.findById(roleId).get();
        BeanUtils.copyProperties(roleEntity, roleModel);
        return roleModel;
    }

    @Override
    public void deleteRoleById(Long roleId) {
        roleRepository.deleteById(roleId);
    }

    @Override
    public void deleteRoles() {
        roleRepository.deleteAll();
    }
}
