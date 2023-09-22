package com.example.JewelerProgressReport.users.person.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonResponse {
    private Long id;
    private String username;
    private String firstname;
    private String address;
    private String phoneNumber;
}
