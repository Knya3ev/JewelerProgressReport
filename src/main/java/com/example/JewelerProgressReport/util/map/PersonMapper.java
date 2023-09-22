package com.example.JewelerProgressReport.util.map;

import com.example.JewelerProgressReport.users.person.Person;
import com.example.JewelerProgressReport.users.person.request.CreatePersonRequest;
import com.example.JewelerProgressReport.users.person.response.PersonResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface PersonMapper {
    Person toPerson(CreatePersonRequest createPersonRequest);
    PersonResponse toPersonResponse(Person person);
    List<PersonResponse> toPersonResponseList(List<Person> personList);
}
