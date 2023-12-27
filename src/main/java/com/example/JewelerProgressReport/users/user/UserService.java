package com.example.JewelerProgressReport.users.user;

import com.example.JewelerProgressReport.exception.HttpException;
import com.example.JewelerProgressReport.security.auth.request.TelegramRequest;
import com.example.JewelerProgressReport.users.enums.roles.Role;
import com.example.JewelerProgressReport.users.user.request.CreateUserRequest;
import com.example.JewelerProgressReport.users.user.response.GuestResponse;
import com.example.JewelerProgressReport.users.user.response.UserResponse;
import com.example.JewelerProgressReport.util.map.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Transactional
    public User create(CreateUserRequest request) {
        User user = userMapper.toUser(request);
        userRepository.save(user);
        return user;
    }

    public User getAuthenticated() {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(currentUser).orElseThrow(()-> new UsernameNotFoundException("User not authenticated"));
    }

    public UserResponse getUserResponse(Long id) {
        return userMapper.toUserResponse(getUser(id));
    }

    public GuestResponse getGuestResponse(){
        User authorization = getAuthenticated();
        return userMapper.toGuestResponse(authorization);
    }

    public User getUser(TelegramRequest telegramRequest) {
        long telegramId = telegramRequest.getTelegramId();
        Optional<User> user = userRepository.findByTelegramId(telegramId);

        if (user.isPresent()) {
            return user.get();
        }

        String username = telegramRequest.getUsername() == null || telegramRequest.getUsername().isEmpty()
                ? "username_" + telegramId
                : telegramRequest.getUsername();

        // TODO: добавить проверку  username  по db

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setTelegramId(telegramId);
        newUser.getRoles().add(Role.ROLE_GUEST);

        return userRepository.save(newUser);
    }


    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new HttpException("User by id %d not found".formatted(id), HttpStatus.NOT_FOUND));
    }

    public List<User> readAll() {
        return userRepository.findAll();
    }

    @Transactional
    public void editCountShopDirector(Long userId, int count) {
        userRepository.editCountShopDirector(userId, count);
    }

    @Transactional
    public User update(CreateUserRequest createUserRequest, Long id) {
        User userUpdate = this.getUser(id);
        User request = userMapper.toUser(createUserRequest);

        userUpdate.setUsername(request.getUsername());
        userUpdate.setFirstname(request.getFirstname());
        userUpdate.setPhoneNumber(request.getPhoneNumber());

        return userUpdate;
    }

    @Transactional
    public void delete(Long id) {
        User user = this.getUser(id);
        userRepository.delete(user);
    }
}
