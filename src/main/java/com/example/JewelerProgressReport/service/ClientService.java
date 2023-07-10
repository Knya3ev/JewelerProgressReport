package com.example.JewelerProgressReport.service;

import com.example.JewelerProgressReport.entity.Client;
import com.example.JewelerProgressReport.exception.ClientNotFoundException;
import com.example.JewelerProgressReport.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;


    @Transactional
    public void create(Client client) {
        clientRepository.save(client);
    }
    public Client read(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException(String.format("Client by id %d is not found", id)));
    }
    public Client read(String phoneNumber) {
        return clientRepository.findByNumberPhone(phoneNumber)
                .orElseThrow(() -> new ClientNotFoundException(String.format("Client with the phone number %s is not found", phoneNumber)));
    }

    public List<Client> readAll() {
        return clientRepository.findAll();
    }

    @Transactional
    public void update(Client client, Long id) {
        Client clientUpdate = this.read(id);

        clientUpdate.setNumberPhone(client.getNumberPhone());
    }
    @Transactional
    public void delete(Long id) {
        Client client = this.read(id);
        clientRepository.delete(client);
    }
    public Client checkoutClientOrCreate(String phoneNumber){
        return this.checkoutClient(phoneNumber,false);
    }
    public Client checkoutClientOrCreate(String phoneNumber, boolean updateForReport){
        return this.checkoutClient(phoneNumber,updateForReport);
    }

    private Client checkoutClient(String phoneNumber, boolean updateForReport){
        String replacedPhoneNumber = phoneNumber.replace("+7", "8").replace(" ", "");
        try{
            Client client = this.read(replacedPhoneNumber);
            if (!updateForReport) clientRepository.updateLastVisitAndCountVisit(client.getId());
            return client;

        }catch (ClientNotFoundException e){

            Client client = new Client(replacedPhoneNumber, LocalDateTime.now());
            this.create(client);
            return client;
        }
    }
}
