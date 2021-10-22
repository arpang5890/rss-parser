package com.demo.rss.parser.service;

import com.demo.rss.parser.dto.ItemDto;
import com.demo.rss.parser.mapper.ItemMapper;
import com.demo.rss.parser.model.Item;
import com.demo.rss.parser.repository.ItemRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

  @Mock
  private ItemRepository itemRepositoryMock;

  private ItemService itemService;

  @BeforeEach
  void setMock() {
    itemService = new ItemService(itemRepositoryMock, new ItemMapper());
  }

  @AfterEach
  void tearDown() {
    itemService = null;
  }

  @Test
  public void getAllItems_WhenRecordNotEmpty() {
    Page<Item> page = new PageImpl<>(getItems());
    Mockito.when(itemRepositoryMock.findAll(ArgumentMatchers.any(Pageable.class))).thenReturn(page);
    List<ItemDto> items = itemService.getItems(0, 10, Optional.empty(), Optional.empty());
    assertEquals(2, items.size());
    Mockito.verify(itemRepositoryMock, Mockito.times(1))
        .findAll(ArgumentMatchers.any(Pageable.class));
  }

  @Test
  public void getAllItems_WhenRecordEmpty() {
    Page<Item> page = new PageImpl<>(new ArrayList<>());
    Mockito.when(itemRepositoryMock.findAll(ArgumentMatchers.any(Pageable.class))).thenReturn(page);
    List<ItemDto> items = itemService.getItems(0, 10, Optional.empty(), Optional.empty());
    assertEquals(0, items.size());
    Mockito.verify(itemRepositoryMock, Mockito.times(1))
        .findAll(ArgumentMatchers.any(Pageable.class));
  }

  @Test
  public void saveItems_WhenRecordNotEmpty() {
    List<Item> items = getItems();
    itemService.insertOrUpdate(items);
    Mockito.verify(itemRepositoryMock, Mockito.times(1)).saveAll(items);
  }

  @Test
  public void saveItems_WhenRecordEmpty() {
    Throwable exception = assertThrows(IllegalArgumentException.class,
        () -> itemService.insertOrUpdate(new ArrayList<>()));
    assertEquals(exception.getMessage(), "No items supplied to insert");
  }


  private List<Item> getItems() {
    List<Item> list = new ArrayList<>();
    Item t1 = new Item();
    t1.setTitle("Test");
    Item t2 = new Item();
    t2.setTitle("Test1");
    list.add(t1);
    list.add(t2);
    return list;
  }
}
