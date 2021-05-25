package com.example.servingwebcontent.service;

import com.example.servingwebcontent.domain.Board;
import com.example.servingwebcontent.domain.User;
import com.example.servingwebcontent.repository.BoardRepository;
import com.example.servingwebcontent.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardService {
    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    public Board save(String username, Board board){
        User user = userRepository.findByUsername(username);
        board.setUser(user);
        board.addCatTotal();

        return boardRepository.save(board);

    }
}
