package com.example.servingwebcontent.controller;

import com.example.servingwebcontent.domain.Board;
import com.example.servingwebcontent.domain.Category;
import com.example.servingwebcontent.repository.BoardRepository;
import com.example.servingwebcontent.repository.CategoryRepository;
import com.example.servingwebcontent.service.BoardService;
import com.example.servingwebcontent.validator.BoardValidator;
import com.sun.org.apache.xpath.internal.operations.Mod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardService boardService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BoardValidator boardValidator;

    @GetMapping("/list")
    public String list(Model model,@PageableDefault(size = 2) Pageable pageable,
                       @RequestParam(required = false, defaultValue = "") String searchText){

        //List<Board> boards = boardRepository.findAll();
        //Page<Board> boards = boardRepository.findAll(PageRequest.of(0, 20));

        //http://localhost:8080/board/list?page=0&size=1
       // Page<Board> boards = boardRepository.findAll(pageable);

        Page<Board> boards = boardRepository.findByTitleContainingOrContentContaining(searchText, searchText, pageable);


        int startPage = Math.max(1,boards.getPageable().getPageNumber() -4);
        int endPage = Math.min(boards.getTotalPages(), boards.getPageable().getPageNumber() +4);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("boards", boards);

        return "board/list";
    }

    @GetMapping("/form")
    public String form(Model model, @RequestParam(required = false) Long id){

        if(id ==null){
            model.addAttribute("board", new Board());
        }else {
            Board board = boardRepository.findById(id).orElse(null);
            model.addAttribute("board", board);
        }

        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);

        return "board/form";
    }

    @PostMapping("/form")
    public String greetingSubmit(@Valid Board board, BindingResult bindingResult, Authentication authentication, @RequestParam(required = false) Long  prevCatId) {
        boardValidator.validate(board, bindingResult);
        if (bindingResult.hasErrors()) {
            return "board/form";
        }
        log.debug("prevCatId 호출 () :{}", prevCatId);
        log.debug("board.getCategory().getId() 호출  :{}", board.getCategory().getId());
        String username = authentication.getName();

        if(prevCatId == null ) {
            log.debug("prevCatId null !!!:{}", prevCatId);
            boardService.save(username, board);
        }else {
            if(board.getCategory().getId() != prevCatId){
                log.debug("prevCatId !=== getId");
            }else{
                log.debug("prevCatId== getId");
            }
            // boardService.save(username, board);
        }

        // boardRepository.save(board);
        return "redirect:/board/list";
    }
}
