package com.demo.rss.parser.integration;

import com.demo.rss.parser.model.Item;
import com.demo.rss.parser.service.ItemService;
import com.demo.rss.parser.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

@Component
@Slf4j
public class RssFeedIntegration {

  private final String rssFeedUrl;

  private final ItemService itemService;

  private final RestTemplate restTemplate;

  // TODO can be externalize
  private final long pollingAfterMs = 5 * 60 * 1000;

  @Autowired
  public RssFeedIntegration(final ItemService itemService, final RestTemplate restTemplate,
      @Value("${rss.feed.url}") final String rssFeedUrl) {
    this.itemService = itemService;
    this.restTemplate = restTemplate;
    this.rssFeedUrl = rssFeedUrl;
  }

  /**
   * TODO: Spring feed integration module can be used instead of custom pooling
   */
  @Scheduled(fixedDelay = pollingAfterMs)
  public void execute() {
    log.info("Start executing rss feed polling");
    String feedXml = restTemplate.getForObject(rssFeedUrl, String.class);
    transformAndProcess(feedXml);
  }

  private void transformAndProcess(final String feedXml) {
    List<Item> itemList = new ArrayList<>();
    Document doc = getXmlDocument(feedXml);
    NodeList list = doc.getElementsByTagName("item");
    for (int temp = 0; temp < list.getLength(); temp++) {
      Node node = list.item(temp);
      if (node.getNodeType() == Node.ELEMENT_NODE) {
        Element element = (Element) node;
        Item item = new Item();
        item.setId(element.getElementsByTagName("guid").item(0).getTextContent());
        item.setTitle(element.getElementsByTagName("title").item(0).getTextContent());
        item.setDescription(element.getElementsByTagName("description").item(0).getTextContent());
        item.setAuthor(element.getElementsByTagName("itunes:author").item(0).getTextContent());
        item.setPublicationDate(
            DateUtil.parseDate(element.getElementsByTagName("pubDate").item(0).getTextContent()));
        itemList.add(item);
      }
    }
    if (!CollectionUtils.isEmpty(itemList)) {
      // TODO:Can be used as predefined batch limit insert
      itemService.insertOrUpdate(itemList);
    }
  }

  private Document getXmlDocument(final String feedXml) {
    // Instantiate the Factory
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    try {
      // process XML securely, avoid attacks like XML External Entities (XXE)
      dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
      // parse XML file
      DocumentBuilder db = dbf.newDocumentBuilder();
      Document doc = db.parse(new InputSource(new StringReader(feedXml)));
      doc.getDocumentElement().normalize();
      return doc;
    } catch (Exception e) {
      log.error("RssFeedIntegration failed", e);
      // TODO: can be uses a custom exception
      throw new RuntimeException("RssFeedIntegration failed, Unable to parse the xml", e);
    }
  }
}
