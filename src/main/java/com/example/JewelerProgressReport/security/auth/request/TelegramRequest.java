package com.example.JewelerProgressReport.security.auth.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TelegramRequest {
    private String username;
    private long telegramId;
}
