package com.demo.rss.parser.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ItemDto {

  private String id;

  private String title;

  private String description;

  private String author;

}
