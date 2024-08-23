package com.bw.jwt_demo.service;

import com.bw.jwt_demo.entity.RoleEntity;
import com.bw.jwt_demo.entity.UserEntity;
import com.bw.jwt_demo.model.RoleModel;
import com.bw.jwt_demo.model.UserModel;
import com.bw.jwt_demo.repository.RoleRepository;
import com.bw.jwt_demo.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Lazy
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserModel register(UserModel userModel) {
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userModel, userEntity); // copyProperties doesn't do deep copy. It doesn't copy collections

        Set<RoleEntity> roleEntities = new HashSet<>();
        //fetch every role from the DB based on role id and than set this role to user entity roles
        for(RoleModel rm : userModel.getRoles()) {
            Optional<RoleEntity> optRe = roleRepository.findById(rm.getId());
            if (optRe.isPresent()) {
                roleEntities.add(optRe.get());
            }
        }
        userEntity.setRoles(roleEntities);

        userEntity.setPassword(this.bCryptPasswordEncoder.encode(userEntity.getPassword()));
        userEntity = userRepository.save(userEntity);
        BeanUtils.copyProperties(userEntity, userModel);

        //convert RoleEntities to RoleModels
        Set<RoleModel> roleModels = new HashSet<>();
        RoleModel roleModel = null;
        for(RoleEntity re : userEntity.getRoles()) {
            roleModel = new RoleModel();
            roleModel.setId(re.getId());
            roleModel.setRoleName(re.getRoleName());
            roleModels.add(roleModel);
        }
        userModel.setRoles(roleModels);

        return userModel;
    }

    //validation for user existence
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username);
        if (userEntity == null) {
            throw new UsernameNotFoundException("User does not exist.");
        } else {
            UserModel userModel = new UserModel();
            BeanUtils.copyProperties(userEntity, userModel);

            //convert RoleEntities to RoleModels
            Set<RoleModel> roleModels = new HashSet<>();
            RoleModel roleModel = null;
            for(RoleEntity re : userEntity.getRoles()) {
                roleModel = new RoleModel();
                roleModel.setId(re.getId());
                roleModel.setRoleName(re.getRoleName());
                roleModels.add(roleModel);
            }
            userModel.setRoles(roleModels);

            return userModel;
        }
        /*if (username.equals("Gnana")) {
            return new User("Gnana", "ragasiyam", new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("User does not exist.");
        }*/
    }
}
