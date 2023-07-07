package com.example.JewelerProgressReport.controllers;


import com.example.JewelerProgressReport.util.map.PersonMapper;
import com.example.JewelerProgressReport.model.request.CreatePersonRequest;
import com.example.JewelerProgressReport.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;
    private final PersonMapper personMapper;

    @Operation(summary = "Crete User")
    @PostMapping()
    public ResponseEntity<?> create(@RequestBody @Validated CreatePersonRequest createPersonRequest){
        personService.create(createPersonRequest);
        return ResponseEntity.ok().body(HttpStatus.CREATED);
    }
    @Operation(summary = "Get by User id ")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id){
        return ResponseEntity.ok().body(personMapper.toPersonResponse(personService.read(id)));
    }
    @Operation(summary = "Get all Users")
    @GetMapping("/all")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok().body(personService.readAll());
    }


    @Operation(summary = "Remove by User id")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        personService.delete(id);
        return  ResponseEntity.ok().body(HttpStatus.OK);
    }

    @Operation (summary = "Update by User id ")
    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id,
                                    @RequestBody @Validated CreatePersonRequest createPersonRequest){
        personService.update(createPersonRequest,id);
        return ResponseEntity.ok().body(HttpStatus.OK);
    }
}
