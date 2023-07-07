package com.example.JewelerProgressReport.model.request;

import com.example.JewelerProgressReport.model.response.ReportResponse;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
public class CreatePersonRequest {

    @NotNull
    private String username;

    @Size(min = 3, max = 10, message = "min 3 char and max 10 char for firstname" )
    private String firstname;

    @NotNull
    private String telegramId;

    @NotNull
    private String address;

    @NotNull(message = "the phone field cannot be empty")
    @Pattern(regexp = "^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$", message = "phone not correct")
    private String phoneNumber;

}
