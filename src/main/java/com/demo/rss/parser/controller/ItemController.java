package com.demo.rss.parser.controller;

import java.util.List;
import java.util.Optional;

import com.demo.rss.parser.dto.ItemDto;
import com.demo.rss.parser.enums.SortDirection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.rss.parser.service.ItemService;

@RestController
@RequestMapping("/items")
public class ItemController {

  private final ItemService itemService;

  @Autowired
  public ItemController(final ItemService itemService) {
    this.itemService = itemService;
  }

  @GetMapping
  public List<ItemDto> geItems(
      @RequestParam(name = "page", defaultValue = "0") int page,
      @RequestParam(name = "size", defaultValue = "10") int size,
      @RequestParam(name = "sort") Optional<String> sortBy,
      @RequestParam(name = "direction") Optional<SortDirection> sortDirection) {
    return itemService.getItems(page, size, sortBy, sortDirection);
  }

}