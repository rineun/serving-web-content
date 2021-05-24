package com.example.servingwebcontent.service;

import com.example.servingwebcontent.domain.Role;
import com.example.servingwebcontent.domain.User;
import com.example.servingwebcontent.repository.RoleRepository;
import com.example.servingwebcontent.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User save(User user){

        String encodePassword = passwordEncoder.encode(user.getPassword());

        user.setPassword(encodePassword);
        user.setEnabled(true);

        Role roleOne = repository.findById(1L).orElse(null);

        Role role = new Role();
        role.setId(roleOne.getId());
        role.setName(role.getName());
        user.getRoles().add(role);

        return userRepository.save(user);
    }

    public User saveAmdin(User user, String passwordReset){

        if(!passwordReset.equals("")){
            String encodePassword = passwordEncoder.encode(passwordReset);
            user.setPassword(encodePassword);
            return userRepository.save(user);
        }else{
            User userById = userRepository.findById(user.getId()).orElse(null);
            userById.setUsername(user.getUsername());
            userById.setEnabled(user.getEnabled());
            userById.setRoles(user.getRoles());
            return userRepository.save(userById);
        }
    }
}
