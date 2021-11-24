package com.mindhub.homebanking.service.impl;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.service.PDFGeneratorService;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.Locale;


@Service //Para no tener que instanciar el objeto y poder usar los metodos
public class PDFGeneratorServiceImpl implements PDFGeneratorService {

    @Override
    public void writeTableHeader(PdfPTable table){
            PdfPCell cell = new PdfPCell();
            cell.setBackgroundColor(Color.BLUE);
            cell.setPadding(5);
            Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            font.setColor(Color.WHITE);

            cell.setPhrase(new Phrase("Account",font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell.setPhrase(new Phrase("Destiny",font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell.setPhrase(new Phrase("Amount",font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell.setPhrase(new Phrase("Description",font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

    }
    @Override
    public void writeTableData(PdfPTable table, String accountOrigin, String accountDestiny, Double amount, String description){
        Font fontData = FontFactory.getFont(FontFactory.HELVETICA);
        PdfPCell cell = new PdfPCell();
        cell.setPadding(3);
        cell.setPhrase(new Phrase(accountOrigin,fontData));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell.setPhrase(new Phrase(accountDestiny,fontData));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell.setPhrase(new Phrase("$"+amount,fontData));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell.setPhrase(new Phrase(description,fontData));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);


    }
    @Override
    public void export(HttpServletResponse response, String accountOrigin, String accountDestiny, Double amount, String description, Account account) throws IOException, DocumentException {

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document,response.getOutputStream());
        document.open();
        document.addTitle("Mindhub Brothers Bank");
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setColor(Color.BLACK);
        font.setSize(18);
        Image image = Image.getInstance("https://mbbhomebanking.herokuapp.com/web/img/logoBank.png");
        image.setAbsolutePosition(500,740);
        image.scaleToFit(60,60);
        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fontTitle.setSize(28);
        fontTitle.setColor(Color.MAGENTA);
        Paragraph title = new Paragraph("Mindhub Brothers Bank",fontTitle);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(title);
        document.add(image);
        document.add(new Paragraph(" ",font));
        Paragraph p = new Paragraph("Transaction successful" + "\n" ,font);
        p.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(p);
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {2.5f, 2.5f, 1.5f, 4.5f});
        table.setSpacingBefore(10);
        writeTableHeader(table);
        writeTableData(table,accountOrigin,accountDestiny,amount,description);
        document.add(table);
        document.add(new Paragraph(" ",font));
        document.add(new Paragraph(" ",font));
        Font accountFont = FontFactory.getFont(FontFactory.TIMES_BOLD);
        accountFont.setSize(16);
        Paragraph destination = new Paragraph("Destination account holder: "+account.getOwner().getLastName().toUpperCase()+" "+account.getOwner().getFirstName().toUpperCase(),accountFont);
        Paragraph destination2 = new Paragraph("Recipient's email: "+account.getOwner().getEmail(),accountFont);

        document.add(destination);
        document.add(destination2);

        document.close();


    }
}
