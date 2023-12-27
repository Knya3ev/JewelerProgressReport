package com.example.JewelerProgressReport.util.map;

import com.example.JewelerProgressReport.users.user.User;
import com.example.JewelerProgressReport.users.user.request.CreateUserRequest;
import com.example.JewelerProgressReport.users.user.response.GuestResponse;
import com.example.JewelerProgressReport.users.user.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface UserMapper {
    User toUser(CreateUserRequest createUserRequest);
    @Mapping(target = "roles",
            expression = "java(user.getRoles().stream().map(role -> role.getCode()).toList())")
    UserResponse toUserResponse(User user);
    GuestResponse toGuestResponse(User user);
    List<UserResponse> toUserResponseList(List<User> userList);
}
