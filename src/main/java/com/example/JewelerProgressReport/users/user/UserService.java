package com.example.JewelerProgressReport.users.user;

import com.example.JewelerProgressReport.exception.HttpException;
import com.example.JewelerProgressReport.users.user.request.CreateUserRequest;
import com.example.JewelerProgressReport.users.user.response.UserResponse;
import com.example.JewelerProgressReport.util.map.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Transactional
    public User create(CreateUserRequest request) {
        User user = userMapper.toUser(request);
        userRepository.save(user);
        return user;
    }

    public UserResponse getUserResponse(Long id){
        return userMapper.toUserResponse(getUser(id));
    }

    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new HttpException("User by id %d not found".formatted(id), HttpStatus.NOT_FOUND));
    }

    public User readTelegramId(Long telegramId) {
        return userRepository.findByTelegramId(telegramId)
                .orElseThrow(() -> new HttpException("User by telegram id %d not found".formatted(telegramId), HttpStatus.NOT_FOUND));
    }

    public List<User> readAll() {
        return userRepository.findAll();
    }

    @Transactional
    public void editCountShopDirector(Long userId, int count) {
        userRepository.editCountShopDirector(userId,count);
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
