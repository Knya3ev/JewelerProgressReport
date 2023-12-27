package com.example.JewelerProgressReport.moderation.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CountAllResponse {
    private int jewelry;
    private int shop;
}
