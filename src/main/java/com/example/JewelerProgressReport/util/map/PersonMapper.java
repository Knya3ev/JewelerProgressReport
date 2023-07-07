package com.example.JewelerProgressReport.util.map;

import com.example.JewelerProgressReport.entity.Person;
import com.example.JewelerProgressReport.model.request.CreatePersonRequest;
import com.example.JewelerProgressReport.model.response.PersonResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface PersonMapper {
    Person toPerson(CreatePersonRequest createPersonRequest);
    PersonResponse toPersonResponse(Person person);
    List<PersonResponse> toPersonResponseList(List<Person> personList);
}
