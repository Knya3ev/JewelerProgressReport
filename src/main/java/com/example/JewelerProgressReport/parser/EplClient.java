package com.example.JewelerProgressReport.parser;


import com.example.JewelerProgressReport.parser.config.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "epl-client", url = "${client.epl.host}", configuration = FeignConfiguration.class)
public interface EplClient {
    @GetMapping(value = "${client.epl.search}")
    String getPage(@RequestParam("q") String article);

    @GetMapping("{url}")
    byte[] getImage(@PathVariable("url") String urlImage);
}
