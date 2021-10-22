package com.demo.rss.parser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demo.rss.parser.model.Item;
 
@Repository
public interface ItemRepository
        extends JpaRepository<Item, Long> {
 
}
