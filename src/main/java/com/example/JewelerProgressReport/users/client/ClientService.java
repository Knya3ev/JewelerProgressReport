package com.example.JewelerProgressReport.users.client;

import com.example.JewelerProgressReport.documents.request.ReportRequest;
import com.example.JewelerProgressReport.exception.HttpException;
import com.example.JewelerProgressReport.jewelry.Jewelry;
import com.example.JewelerProgressReport.jewelry.JewelryRepository;
import com.example.JewelerProgressReport.jewelry.JewelryService;
import com.example.JewelerProgressReport.jewelry.enums.JewelleryProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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
                .orElseThrow(() -> new HttpException("Client by id %d is not found".formatted(id), HttpStatus.NOT_FOUND));
    }

    public Client read(String phoneNumber) {
        return clientRepository.findByNumberPhone(phoneNumber)
                .orElseThrow(() -> new HttpException("Client with the phone number %s is not found".formatted(phoneNumber), HttpStatus.NOT_FOUND));
    }

    public void addJewelry( String phoneNumber, Jewelry jewelry){
        Client client = checkoutClientOrCreate(phoneNumber);
        if (!client.getJewelries().contains(jewelry)) {
            client.addJewelry(jewelry);
        }
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

        }catch (HttpException e){

            Client client = new Client(replacedPhoneNumber, LocalDateTime.now());
            this.create(client);
            return client;
        }
    }
}
