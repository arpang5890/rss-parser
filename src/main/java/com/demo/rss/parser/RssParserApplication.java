package com.demo.rss.parser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableScheduling
public class RssParserApplication {

  public static void main(String[] args) {
    SpringApplication.run(RssParserApplication.class, args);
  }

  @Bean
  public RestTemplate restTemplate() {
    // TODO can add some connection pooling
    return new RestTemplate();
  }
}
