package com.example.JewelerProgressReport.jewelry.images;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @GetMapping()
    public ResponseEntity<byte[]> getImage(@RequestParam("article") String article) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(imageService.getImage(article));
    }
    @GetMapping("/link")
    public ResponseEntity<String> getIMageLink(@RequestParam("article") String article){
        return ResponseEntity.ok().body(imageService.getImageLink(article));
    }

}
