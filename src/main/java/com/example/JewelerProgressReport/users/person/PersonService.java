package com.example.JewelerProgressReport.users.person;

import com.example.JewelerProgressReport.exception.HttpException;
import com.example.JewelerProgressReport.util.map.PersonMapper;
import com.example.JewelerProgressReport.users.person.request.CreatePersonRequest;
import com.example.JewelerProgressReport.users.person.response.PersonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class PersonService {
    private final PersonRepository personRepository;
    private final PersonMapper personMapper;


    @Transactional
    public Person create(CreatePersonRequest request) {
        Person person = personMapper.toPerson(request);
        personRepository.save(person);
        return person;
    }

    public Person read(Long id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new HttpException("User by id %d not found".formatted(id), HttpStatus.NOT_FOUND));
    }

    public Person readTelegramId(Long telegramId) {
        return personRepository.findByTelegramId(telegramId)
                .orElseThrow(() -> new HttpException("User by telegram id %d not found".formatted(telegramId), HttpStatus.NOT_FOUND));
    }

    public List<Person> readAll() {
        return personRepository.findAll();
    }

    @Transactional
    public Person update(CreatePersonRequest createPersonRequest, Long id) {
        Person personUpdate = this.read(id);
        Person request = personMapper.toPerson(createPersonRequest);

        personUpdate.setUsername(request.getUsername());
        personUpdate.setFirstname(request.getFirstname());
        personUpdate.setPhoneNumber(request.getPhoneNumber());

        return personUpdate;
    }

    @Transactional
    public void delete(Long id) {
        Person person = this.read(id);
        personRepository.delete(person);
    }
}
