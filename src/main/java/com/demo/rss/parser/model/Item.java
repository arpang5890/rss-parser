package com.demo.rss.parser.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "rss_feed")
@Data
public class Item {

  @Id
  private String id;

  @Column(name = "title")
  private String title;

  @Column(name = "description", columnDefinition = "clob")
  private String description;

  @Column(name = "author")
  private String author;

  @Column(name = "publication_date")
  private LocalDate publicationDate;

  @CreationTimestamp
  @Column(name = "created_date")
  private LocalDate createdDate;

  @UpdateTimestamp
  @Column(name = "updated_date")
  private LocalDate updatedDate;

}