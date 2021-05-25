package com.example.servingwebcontent.repository;

import com.example.servingwebcontent.domain.Category;
import com.example.servingwebcontent.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Page<Category> findByNameContaining(String name, Pageable pageable);
}
