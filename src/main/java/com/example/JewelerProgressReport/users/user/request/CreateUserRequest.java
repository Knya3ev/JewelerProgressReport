package com.example.JewelerProgressReport.users.user.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateUserRequest {

    @NotNull
    private String username;

//    @Size(min = 3, max = 10, message = "min 3 char and max 10 char for firstname" )
    private String firstname;

    @NotNull
    private String telegramId;

//    @NotNull(message = "the phone field cannot be empty")
//    @Pattern(regexp = "^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$", message = "phone not correct")
    private String phoneNumber;

}
