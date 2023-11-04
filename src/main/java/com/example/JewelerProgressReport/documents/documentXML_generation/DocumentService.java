package com.example.JewelerProgressReport.documents.documentXML_generation;


import com.example.JewelerProgressReport.documents.Report;
import com.example.JewelerProgressReport.documents.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final ReportRepository reportRepository;
    private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");


    private final String[] HEADER = {
            "дата",
            "изделие",
            "металл",
            "количество",
            "номер телефона клиента",
            "коррекция размера",
            "проводимые операции",
            "доп. информация",
            "уникальный код"
    };

    public String getFileName(int month, int year){
        YearMonth yearMonth = year > 1 ? YearMonth.of(year, month) : YearMonth.of(LocalDate.now().getYear(), Month.of(month));

        return "report_for_%s_%d.xlsx"
                .formatted( Month.of(month).toString().toLowerCase(),yearMonth.getYear());
    }
    public InputStreamResource generationDocument(Long personId, int month, int year) {

        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("Data");

        this.createHeader(sheet);
        this.writerData(getListReport(personId,month,0),sheet);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            workbook.write(outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        InputStreamResource resource = new InputStreamResource(inputStream);

        return resource;
    }

    private List<Report> getListReport(Long personId, int month, int year) {
        YearMonth yearMonth = year > 1 ? YearMonth.of(year, month) : YearMonth.of(LocalDate.now().getYear(), Month.of(month));

        LocalDateTime startOfMonth = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = yearMonth.atEndOfMonth().atTime(LocalTime.MAX);

        return reportRepository.getListForDocument(startOfMonth, endOfMonth, personId);
    }

    private void writerData(List<Report> reports, Sheet sheet) {
        int STEP = 1;
        for (int i = 0; i < reports.size(); i++) {
            Row row = sheet.createRow(i + STEP);
            Report report = reports.get(i);

            Cell data = row.createCell(0);
            data.setCellValue(report.getCreatedDate().format(dateFormat));

            Cell typeProduct = row.createCell(1);
            typeProduct.setCellValue(report.getJewelleryProduct().getRu());

            Cell typeMetal = row.createCell(2);
            typeMetal.setCellValue(report.getMetal().getRu());

            Cell count = row.createCell(3);
            count.setCellValue(report.getCount());

            Cell phoneNumber = row.createCell(4);
            phoneNumber.setCellValue(report.getClient().getNumberPhone());

            Cell resize = row.createCell(5);
            resize.setCellValue(report.getResize() == null ? "" : report.getResize().getRingResizing());

            Cell typeOfOperation = row.createCell(6);
            typeOfOperation.setCellValue(
                    String.join(", ", report.getJewelleryOperations()
                            .stream().map(operation -> operation.getRu()).collect(Collectors.toList()))
            );

            Cell details = row.createCell(7);
            details.setCellValue(report.getDetailsOfOperation());

            Cell unionCode = row.createCell(8);
            unionCode.setCellValue(report.getUnionCodeJewelry());
        }

        for (int i = 0; i < this.HEADER.length; i++) sheet.autoSizeColumn(i);
    }

    private void createHeader(Sheet sheet) {
        Row header = sheet.createRow(0);

        for (int i = 0; i < this.HEADER.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(this.HEADER[i]);
        }
    }

}
