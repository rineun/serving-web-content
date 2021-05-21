package com.example.servingwebcontent.controller;

import com.example.servingwebcontent.domain.Board;
import com.example.servingwebcontent.domain.User;
import com.example.servingwebcontent.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
class UserApiController {

    @Autowired
    private  UserRepository repository;


    @GetMapping("/users")
    List<User> all() {
        
        //return repository.findAll();
        List<User> users = repository.findAll();

        log.debug("size 호출 전");
        log.debug("size 호출 () :{}", users.get(0).getBoards().size());

        log.debug("size 호출 후");
        return users;
    }


    @PostMapping("/users")
    User newUser(@RequestBody User newUser) {
        return repository.save(newUser);
    }

    // Single item

    @GetMapping("/users/{id}")
    User one(@PathVariable Long id) {

        return repository.findById(id).orElse(null);
         //       .orElseThrow(() -> new UserNotFoundException(id));
    }


    /**
     *
     {
     "boards":[
     {
     "title" : "hi33",
     "content" : "hi333"
     }

     ]
     }
     */
    @PutMapping("/users/{id}")
    User replaceUser(@RequestBody User newUser, @PathVariable Long id) {

        return repository.findById(id)
                .map(user -> {
                  //  user.setTitle(newUser.getTitle());
                  //  user.setContent(newUser.getContent());
                    // user.setBoards(newUser.getBoards());
                    user.getBoards().clear();
                    user.getBoards().addAll(newUser.getBoards());
                    for(Board board: user.getBoards()){
                        board.setUser(user);
                    }
                    return repository.save(user);
                })
                .orElseGet(() -> {
                    newUser.setId(id);
                    return repository.save(newUser);
                });
    }

    @DeleteMapping("/users/{id}")
    void deleteUser(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
