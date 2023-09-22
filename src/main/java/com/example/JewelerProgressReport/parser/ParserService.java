package com.example.JewelerProgressReport.parser;


import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ParserService {

    private final EplClient eplClient;

    @Value("${client.epl.host}")
    private String BASE_URL;
    @Value("${parser.epl.first-selector-for-image}")
    private String FIRST_SELECTOR;
    @Value("${parser.epl.second-selector-for-image}")
    private String SECOND_SELECTOR;
    @Value("${parser.epl.image-not-found-url}")
    private String NOT_FOUND_IMAGE_URL;
    @Value("${parser.epl.link-product}")
    private String LINK_PRODUCT;
    @Value("${parser.epl.count}")
    private String COUNT;

    public byte[] getImage(String article) throws IOException {
        Document document = getPage(article);

        Integer countResult = getCountResult(document);

        if (countResult != null && countResult > 0) {
            String image = document.select(FIRST_SELECTOR).attr("src");


            if (image.equals(NOT_FOUND_IMAGE_URL)) {
                String linkProduct = document.select(LINK_PRODUCT).attr("href");
                Document document1 = Jsoup.connect("http:" + linkProduct).get();
                image = document1.select(SECOND_SELECTOR).attr("data-src");
            }

            return eplClient.getImage(image);
        }
        return null;
    }

    public String getImageLink(String article){
        Document document = getPage(article);
        Integer countResult = getCountResult(document);

        if(countResult != null && countResult > 0){
            return BASE_URL + document.select(FIRST_SELECTOR).attr("src");
        }
        return BASE_URL + "${client.epl.search}" + "/?q=%s".formatted(article);
    }

    private Integer getCountResult(Document document) {
        String[] result = document.select(COUNT).text().split(":");

        if (result.length > 1 && StringUtils.isNumeric(result[1].replace(" ", ""))) {
            return Integer.valueOf(result[1].replace(" ", ""));
        }
        return null;
    }

    private Document getPage(String article){
        String html = eplClient.getPage(article);
        return Jsoup.parse(html, BASE_URL);
    }
}
