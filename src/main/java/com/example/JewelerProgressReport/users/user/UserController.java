package com.example.JewelerProgressReport.users.user;


import com.example.JewelerProgressReport.users.user.request.CreateUserRequest;
import com.example.JewelerProgressReport.users.user.response.UserResponse;
import com.example.JewelerProgressReport.util.map.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @Operation(summary = "Create User")
    @PostMapping()
    public ResponseEntity<User> create(@RequestBody @Validated CreateUserRequest createUserRequest){
        return ResponseEntity.ok().body(userService.create(createUserRequest));
    }

    @Operation(summary = "Get by User id ")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable("id") Long id){
        return ResponseEntity.ok().body(userMapper.toUserResponse(userService.getUser(id)));
    }

    @Operation(summary = "Get all Users")
    @GetMapping("/all")
    public ResponseEntity<List<UserResponse>> getAll(){
        return ResponseEntity.ok().body(userMapper.toUserResponseList(userService.readAll()));
    }

    @Operation(summary = "Remove by User id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id){
        userService.delete(id);
        return  ResponseEntity.ok().build();
    }

    @Operation (summary = "Update by User id ")
    @PatchMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable("id") Long id,
                                               @RequestBody @Validated CreateUserRequest createUserRequest){
        return ResponseEntity.ok().body(userMapper.toUserResponse(userService.update(createUserRequest,id)));
    }
}
