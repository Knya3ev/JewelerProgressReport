package com.example.JewelerProgressReport.users.user;


import com.example.JewelerProgressReport.users.user.request.CreateUserRequest;
import com.example.JewelerProgressReport.users.user.response.GuestResponse;
import com.example.JewelerProgressReport.users.user.response.UserResponse;
import com.example.JewelerProgressReport.util.map.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;


    @Operation(summary = "Create User")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> create(@RequestBody @Validated CreateUserRequest createUserRequest) {
        return ResponseEntity.ok().body(userService.create(createUserRequest));
    }

    @Operation(summary = "Get profile")
    @GetMapping
    public ResponseEntity<UserResponse> getProfile(){
        return ResponseEntity.ok(userMapper.toUserResponse(userService.getAuthenticated()));
    }

    @Operation(summary = "Get Guest ")
    @GetMapping(value = "/guest", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GuestResponse> getGuestById() {
        return ResponseEntity.ok().body(userService.getGuestResponse());
    }

    @Operation(summary = "Get by User id ")
    @GetMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> getById(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok().body(userMapper.toUserResponse(userService.getUser(userId)));
    }

    @Operation(summary = "Get all Users")
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserResponse>> getAll() {
        return ResponseEntity.ok().body(userMapper.toUserResponseList(userService.readAll()));
    }

    @Operation(summary = "Remove by User id")
    @DeleteMapping(value = "/{userId}")
    public ResponseEntity<Void> delete(@PathVariable("userId") Long userId) {
        userService.delete(userId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Update by User id ")
    @PatchMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> update(@PathVariable("userId") Long id,
                                               @RequestBody @Validated CreateUserRequest createUserRequest) {
        return ResponseEntity.ok().body(userMapper.toUserResponse(userService.update(createUserRequest, id)));
    }

    @Operation(summary = "change the number of shops for the user")
    @PatchMapping(value = "/{userId}/edit/shops")
    public ResponseEntity<Void> editCountShopDirector(@PathVariable("userId") Long userId,
                                                      @RequestParam("count") int count) {
        userService.editCountShopDirector(userId, count);
        return ResponseEntity.ok().build();
    }
}
