package com.example.JewelerProgressReport.users.person;

import com.example.JewelerProgressReport.exception.UserNotFoundException;
import com.example.JewelerProgressReport.exception.UserTelegramIdNotFoundException;
import com.example.JewelerProgressReport.util.map.PersonMapper;
import com.example.JewelerProgressReport.users.person.request.CreatePersonRequest;
import com.example.JewelerProgressReport.users.person.response.PersonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class PersonService {
    private final PersonRepository personRepository;
    private final PersonMapper personMapper;


    @Transactional
    public void create(CreatePersonRequest person) {
        personRepository.save(personMapper.toPerson(person));
    }

    public Person read(Long id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format("User by id %d not found", id)));
    }

    public Person readTelegramId(Long telegramId) {
        return personRepository.findByTelegramId(telegramId)
                .orElseThrow(() -> new UserTelegramIdNotFoundException(String.format("User by telegram id %d not found", telegramId)));
    }

    public List<PersonResponse> readAll() {
        return personMapper.toPersonResponseList(personRepository.findAll());
    }

    @Transactional
    public void update(CreatePersonRequest createPersonRequest, Long id) {
        Person personUpdate = this.read(id);
        Person request = personMapper.toPerson(createPersonRequest);

        personUpdate.setUsername(request.getUsername());
        personUpdate.setFirstname(request.getFirstname());
        personUpdate.setPhoneNumber(request.getPhoneNumber());
        personUpdate.setAddress(request.getAddress());
    }

    @Transactional
    public void delete(Long id) {
        Person person = this.read(id);
        personRepository.delete(person);
    }
}
