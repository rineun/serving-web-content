package com.example.servingwebcontent.controller;

import com.example.servingwebcontent.domain.Category;
import com.example.servingwebcontent.domain.Role;
import com.example.servingwebcontent.domain.User;
import com.example.servingwebcontent.repository.CategoryRepository;
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

import java.util.List;

@Controller
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;


    @GetMapping("/list")
    public String list(Model model, @PageableDefault(size = 2) Pageable pageable,
                       @RequestParam(required = false, defaultValue = "") String searchText){


        Page<Category> categories = categoryRepository.findByNameContaining(searchText, pageable);


        int startPage = Math.max(1,categories.getPageable().getPageNumber() -4);
        int endPage = Math.min(categories.getTotalPages(), categories.getPageable().getPageNumber() +4);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("categories", categories);

        return "category/list";
    }

    @GetMapping("/form")
    public String form(Model model, @RequestParam(required = false) Long id){
        if(id ==null){
            model.addAttribute("category", new Category());
        }else {
            Category category = categoryRepository.findById(id).orElse(null);
            model.addAttribute("category", category);

        }

        return "category/form";
    }


    @PostMapping("/form")
    public String formPost(Category category){
        categoryRepository.save(category);
        return "redirect:/category/list";
    }



}
