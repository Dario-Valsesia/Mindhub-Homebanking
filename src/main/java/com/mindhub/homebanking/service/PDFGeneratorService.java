package com.mindhub.homebanking.service;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfPTable;
import com.mindhub.homebanking.models.Account;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public interface PDFGeneratorService {
     void writeTableHeader(PdfPTable table);
     void writeTableData(PdfPTable table, String accountOrigin, String accountDestiny, Double amount, String description);
     void export(HttpServletResponse response, String accountOrigin, String accountDestiny, Double amount, String description, Account account) throws IOException, DocumentException;
}
