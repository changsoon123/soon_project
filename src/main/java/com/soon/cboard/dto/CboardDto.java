package com.soon.cboard.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class CboardDto {

    private Long id;
    private String title;
    private String content;
    private Date createdAt;

}
