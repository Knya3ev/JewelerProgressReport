package com.example.JewelerProgressReport.users.person;


import com.example.JewelerProgressReport.users.person.request.CreatePersonRequest;
import com.example.JewelerProgressReport.users.person.response.PersonResponse;
import com.example.JewelerProgressReport.util.map.PersonMapper;
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
public class PersonController {

    private final PersonService personService;
    private final PersonMapper personMapper;

    @Operation(summary = "Crete User")
    @PostMapping()
    public ResponseEntity<?> create(@RequestBody @Validated CreatePersonRequest createPersonRequest){
        return ResponseEntity.ok().body(personService.create(createPersonRequest));
    }
    @Operation(summary = "Get by User id ")
    @GetMapping("/{id}")
    public ResponseEntity<PersonResponse> getById(@PathVariable("id") Long id){
        return ResponseEntity.ok().body(personMapper.toPersonResponse(personService.read(id)));
    }
    @Operation(summary = "Get all Users")
    @GetMapping("/all")
    public ResponseEntity<List<PersonResponse>> getAll(){
        return ResponseEntity.ok().body(personMapper.toPersonResponseList(personService.readAll()));
    }


    @Operation(summary = "Remove by User id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id){
        personService.delete(id);
        return  ResponseEntity.ok().build();
    }

    @Operation (summary = "Update by User id ")
    @PatchMapping("/{id}")
    public ResponseEntity<PersonResponse> update(@PathVariable("id") Long id,
                                    @RequestBody @Validated CreatePersonRequest createPersonRequest){
        return ResponseEntity.ok().body(personMapper.toPersonResponse(personService.update(createPersonRequest,id)));
    }
}
