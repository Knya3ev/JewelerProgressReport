package com.example.JewelerProgressReport.users.user.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GuestResponse {
    private Long id;
    private String username;
    private boolean inStaff;
    private boolean pendingApproval;
}
