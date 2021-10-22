package com.demo.rss.parser.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.demo.rss.parser.dto.ItemDto;
import com.demo.rss.parser.enums.SortDirection;
import com.demo.rss.parser.mapper.ItemMapper;
import com.demo.rss.parser.model.Item;
import com.demo.rss.parser.repository.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@Slf4j
public class ItemService {

  private final ItemRepository itemRepository;

  private final ItemMapper itemMapper;

  private final String DEFAULT_SORT_BY = "updatedDate";

  private final Sort.Direction DEFAULT_SORT_DIRECTION = Sort.Direction.DESC;

  @Autowired
  public ItemService(final ItemRepository itemRepository, final ItemMapper itemMapper) {
    this.itemRepository = itemRepository;
    this.itemMapper = itemMapper;
  }

  public List<ItemDto> getItems(int page, int size,
      Optional<String> sortBy, Optional<SortDirection> sortDirection) {
    Page<Item> pageItems = itemRepository.findAll(getPageable(page, size, sortBy, sortDirection));
    if (pageItems.hasContent()) {
      return itemMapper.toDtos(pageItems.getContent());
    }
    return new ArrayList<>();
  }

  public void insertOrUpdate(final List<Item> itemList) {
    if (CollectionUtils.isEmpty(itemList)) {
      // TODO: can be custom exception
      throw new IllegalArgumentException("No items supplied to insert");
    }
    itemRepository.saveAll(itemList);
  }

  private Pageable getPageable(int page, int size,
      Optional<String> sortBy, Optional<SortDirection> sortDirection) {
    /**
     * TODO:
     * sortBy db field should not directly expose to the caller (FE), internally in service layer
     * (in-memory), we should have mapping
     */
    String sortOn = sortBy.isPresent() ? sortBy.get() : DEFAULT_SORT_BY;
    Sort.Direction direction =
        sortDirection.isPresent() ? Sort.Direction.valueOf(sortDirection.get().toString())
            : DEFAULT_SORT_DIRECTION;
    return PageRequest.of(page, size, Sort.by(direction, sortOn));
  }

}