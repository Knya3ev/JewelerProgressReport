package com.example.JewelerProgressReport.util.map;

import com.example.JewelerProgressReport.users.user.User;
import com.example.JewelerProgressReport.users.user.request.CreateUserRequest;
import com.example.JewelerProgressReport.users.user.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface UserMapper {
    User toUser(CreateUserRequest createUserRequest);
    UserResponse toUserResponse(User user);
    List<UserResponse> toUserResponseList(List<User> userList);
}
