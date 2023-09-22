package com.example.JewelerProgressReport.users.person;


import com.example.JewelerProgressReport.users.person.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PersonRepository  extends JpaRepository<Person,Long> {
    Optional<Person> findByTelegramId(Long telegramId);
}
