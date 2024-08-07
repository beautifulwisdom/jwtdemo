package com.bw.jwt_demo.service;

import com.bw.jwt_demo.entity.UserEntity;
import com.bw.jwt_demo.model.UserModel;
import com.bw.jwt_demo.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Lazy
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserModel register(UserModel userModel) {
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userModel, userEntity);
        userEntity.setPassword(this.bCryptPasswordEncoder.encode(userEntity.getPassword()));
        userEntity = userRepository.save(userEntity);
        BeanUtils.copyProperties(userEntity, userModel);
        return userModel;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username);
        if (userEntity == null) {
            throw new UsernameNotFoundException("User does not exist.");
        } else {
            UserModel userModel = new UserModel();
            BeanUtils.copyProperties(userEntity, userModel);
            return userModel;
        }
        /*if (username.equals("Gnana")) {
            return new User("Gnana", "ragasiyam", new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("User does not exist.");
        }*/
    }
}
