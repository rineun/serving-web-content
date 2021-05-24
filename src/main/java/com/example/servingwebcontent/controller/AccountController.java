package com.example.servingwebcontent.controller;

import com.example.servingwebcontent.domain.Board;
import com.example.servingwebcontent.domain.Role;
import com.example.servingwebcontent.domain.User;
import com.example.servingwebcontent.repository.RoleRepository;
import com.example.servingwebcontent.repository.UserRepository;
import com.example.servingwebcontent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;


    @GetMapping("/login")
    public String login(){
        return "account/login";
    }


    @GetMapping("/register")
    public String register(User user){
        return "account/register";
    }

    @PostMapping("/register")
    public String registerPost(User user){
        userService.save(user);
        return "redirect:/";
    }


    @GetMapping("/list")
    public String list(Model model, @PageableDefault(size = 2) Pageable pageable,
                       @RequestParam(required = false, defaultValue = "") String searchText){


        Page<User> users = userRepository.findByUsernameContaining(searchText, pageable);


        int startPage = Math.max(1,users.getPageable().getPageNumber() -4);
        int endPage = Math.min(users.getTotalPages(), users.getPageable().getPageNumber() +4);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("users", users);

        return "account/list";
    }

    @GetMapping("/form")
    public String form(Model model, @RequestParam(required = false) Long id){
        if(id ==null){
            model.addAttribute("user", new User());
        }else {
            User user = userRepository.findById(id).orElse(null);
            model.addAttribute("user", user);

        }
        List<Role> roles  = roleRepository.findAll();
        model.addAttribute("roles", roles);

        return "account/form";
    }


    @PostMapping("/form")
    public String accountPost(User user, @RequestParam(required = false, defaultValue = "") String passwordReset){

        userService.saveAmdin(user, passwordReset);
        return "redirect:/account/list";
    }



}
