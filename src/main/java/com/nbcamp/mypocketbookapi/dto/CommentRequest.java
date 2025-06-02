package com.nbcamp.mypocketbookapi.dto;

import lombok.Getter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.plaf.PanelUI;

@Getter
public class CommentRequest {

    public Long memberId;
    public Long reviewId;
    public String text;

}


