package com.example.JewelerProgressReport.service;

import com.example.JewelerProgressReport.entity.Client;
import com.example.JewelerProgressReport.entity.Jewelry;
import com.example.JewelerProgressReport.entity.Person;
import com.example.JewelerProgressReport.entity.Report;
import com.example.JewelerProgressReport.exception.ReportNotFoundException;
import com.example.JewelerProgressReport.model.request.ReportRequest;
import com.example.JewelerProgressReport.model.typeEnum.TypeOfJewelry;
import com.example.JewelerProgressReport.model.typeEnum.TypeOfMetalColor;
import com.example.JewelerProgressReport.model.typeEnum.TypeOfOperation;
import com.example.JewelerProgressReport.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.JewelerProgressReport.model.typeEnum.TypeOfOperation.*;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    private final ClientService clientService;
    private final PersonService personService;
    private final JewelryService jewelryService;

    private final SizeRingService sizeRingService;



    @Transactional
    public void create(Long personId, ReportRequest reportRequest) {
        Person person = personService.read(personId);
        Report report = this.toReport(reportRequest);
        Client client = clientService.checkoutClientOrCreate(report.getPhoneNumber());

        client.addReports(report);
        person.addReport(report);

         jewelryService.createJewelryIfIsNotNullArticle(client, reportRequest);

        reportRepository.save(report);
    }


    public Report read(Long id) {
        return reportRepository.findById(id)
                .orElseThrow(() -> new ReportNotFoundException(String.format("Report by id %d not found", id)));
    }

    public List<Report> readAll() {
        return reportRepository.findAll();
    }


    @Transactional
    public void update(ReportRequest reportRequest, Long id) {
        Report reportUpdate = this.read(id);

        Report report = this.toReport(reportRequest);

        reportUpdate.setPhoneNumber(report.getPhoneNumber());
        reportUpdate.setTypeProduct(report.getTypeProduct());
        reportUpdate.setTypeOfMetalColor(report.getTypeOfMetalColor());
        reportUpdate.setTypeOfOperation(report.getTypeOfOperation());
        reportUpdate.setDetailsOfOperation(report.getDetailsOfOperation());
        reportUpdate.setResizes(sizeRingService.checkoutSizeRingOrCreate(report.getResizes().getBefore(), report.getResizes().getAfter()));
        reportUpdate.setUnionCodeJewelry(report.getUnionCodeJewelry());
        reportUpdate.setArticle(report.getArticle());
        reportUpdate.setClient(clientService.checkoutClientOrCreate(report.getPhoneNumber()));
        reportUpdate.setEdit(true);
        reportUpdate.setEditDate(LocalDateTime.now());
    }


    @Transactional
    public void delete(Long id) {
        Report report = this.read(id);
        report.removePersonAndClient();
        reportRepository.delete(report);
    }

    private Report toReport(ReportRequest reportRequest) {

        List<String> standardOperation = this.getStandardOperation(reportRequest.getTypeOfMetalColor());
        standardOperation.addAll(0, reportRequest.getTypeOfOperation());


        List<String> newList = standardOperation.stream()
                .map(operation -> TypeOfOperation.valueOf(operation).getRu())
                .collect(Collectors.toList());

        return Report.builder()
                .phoneNumber(reportRequest.getPhoneNumber().replace("+7", "8").replace(" ", ""))
                .typeProduct(TypeOfJewelry.valueOf(reportRequest.getTypeProduct()).getRu())
                .typeOfMetalColor(TypeOfMetalColor.valueOf(reportRequest.getTypeOfMetalColor()).getRu())
                .typeOfOperation(String.join(" ,\n", newList))
                .detailsOfOperation(reportRequest.getDetailsOfOperation())
                .resizes(sizeRingService.checkoutSizeRingOrCreate(reportRequest.getSizeBefore(),reportRequest.getSizeAfter()))
                .unionCodeJewelry(reportRequest.getUnionCodeJewelry())
                .article(reportRequest.getArticle())
                .createdDate(LocalDateTime.now())
                .build();
    }

    private List<String> getStandardOperation(String metalColor) {
        TypeOfMetalColor typeOfMetalColor = TypeOfMetalColor.valueOf(metalColor);

        if (typeOfMetalColor == TypeOfMetalColor.white) {
            return new ArrayList<>(Arrays.asList(cleaning.get(), polishing.get(),
                    rhodiumPlating.get()));
        } else return new ArrayList<>(Arrays.asList(cleaning.get(), polishing.get()));
    }
}
