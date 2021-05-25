package com.example.servingwebcontent.domain;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private LocalDateTime regdate;

    private int post_count;
    private int public_post_count;


    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY) //board를 부를때 사
    private List<Board> boards = new ArrayList<>();

    @Override
    public String toString() {
        return this.name;
    }


    public void addTotal(){
        this.post_count +=1;

    }

    public void deleteTotal(){
        this.post_count -=1;
    }

}
