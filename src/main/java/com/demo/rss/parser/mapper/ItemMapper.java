package com.demo.rss.parser.mapper;

import com.demo.rss.parser.dto.ItemDto;
import com.demo.rss.parser.model.Item;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ItemMapper {

  public ItemDto toDto(final Item item) {
    return new ItemDto(item.getId(), item.getTitle(), item.getDescription(), item.getAuthor());
  }

  public List<ItemDto> toDtos(final List<Item> items) {
    if (!CollectionUtils.isEmpty(items)) {
      return items.stream().map(item -> toDto(item)).collect(Collectors.toList());
    }
    return new ArrayList<>();
  }

}
