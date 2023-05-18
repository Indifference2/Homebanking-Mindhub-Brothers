package com.Mindhub.homebanking.controllers;

import com.Mindhub.homebanking.dtos.AccountDTO;
import com.Mindhub.homebanking.dtos.ClientDTO;
import com.Mindhub.homebanking.dtos.TransactionDTO;
import com.Mindhub.homebanking.models.Account;
import com.Mindhub.homebanking.models.Client;
import com.Mindhub.homebanking.models.Transaction;
import com.Mindhub.homebanking.models.TransactionType;
import com.Mindhub.homebanking.services.AccountService;
import com.Mindhub.homebanking.services.ClientService;
import com.Mindhub.homebanking.services.TransactionService;
import com.Mindhub.homebanking.utils.Utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.FileOutputStream;
import java.io.IOException;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Locale;


@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;
    @RequestMapping("/transactions")
    public List<TransactionDTO> getTransactions(){
        return transactionService.getAllTransactions();
    }

    @RequestMapping("/accounts/{id}/transactions")
    public ResponseEntity<Object> getTransactionsByAccountId(@PathVariable Long id, Authentication authentication){
        Account accountRequest = accountService.findById(id);
        ClientDTO clientAuthenticated = clientService.getCurrentClient(authentication);

        if(accountRequest == null){
            return new ResponseEntity<>("The account doesn't exist", HttpStatus.FORBIDDEN);
        }
        AccountDTO accountDTORequest = new AccountDTO(accountRequest);

        if(clientAuthenticated.getAccount()
                .stream()
                .noneMatch(account -> account.getNumber().equals(accountRequest.getNumber()))){
            return new ResponseEntity<>("This account doesn't belong to you", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(accountDTORequest.getTransaction(), HttpStatus.ACCEPTED);
    }

    @RequestMapping("/accounts/{id}/transactions/dateBetween")
    public ResponseEntity<Object> getTransactionsByIdAndDateBetween (@PathVariable Long id, @RequestParam String startDate, @RequestParam String endDate, Authentication authentication) throws ParseException {
        Account accountRequest = accountService.findById(id);
        ClientDTO clientAuthenticated = clientService.getCurrentClient(authentication);

        if(accountRequest == null){
            return new ResponseEntity<>("The account doesn't exist", HttpStatus.FORBIDDEN);
        }
        AccountDTO accountDTORequest = new AccountDTO(accountRequest);

        if(clientAuthenticated.getAccount()
                .stream()
                .noneMatch(account -> account.getNumber().equals(accountRequest.getNumber()))){
            return new ResponseEntity<>("This account doesn't belong to you", HttpStatus.FORBIDDEN);
        }

        if (startDate.isBlank()){
            return new ResponseEntity<>("Start date can't on blank", HttpStatus.FORBIDDEN);
        }
        if(endDate.isBlank()){
            return new ResponseEntity<>("End date can't be on blank", HttpStatus.FORBIDDEN);
        }
        Date start = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
        Date end = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);

        LocalDateTime startLocalDateTime = Utils.dateToLocalDateTime(start);
        LocalDateTime endLocalDateTime = Utils.dateToLocalDateTime(end).plusDays(1).minusSeconds(1);

;       return new ResponseEntity<>(transactionService.getTransactionsByIdAndDateBetween(accountRequest, startLocalDateTime, endLocalDateTime),HttpStatus.ACCEPTED);
    }

    @PostMapping("/accounts/{id}/transactions/dateBetween/pdf")
    public ResponseEntity<Object> createPDF (@PathVariable Long id, @RequestParam String startDate , @RequestParam String endDate, Authentication authentication) throws DocumentException, IOException, ParseException {

        Account accountRequest = accountService.findById(id);
        ClientDTO clientAuthenticated = clientService.getCurrentClient(authentication);

        if(accountRequest == null){
            return new ResponseEntity<>("The account doesn't exist", HttpStatus.FORBIDDEN);
        }
        if (startDate.isBlank()){
            return new ResponseEntity<>("Start date can't on blank", HttpStatus.FORBIDDEN);
        }
        if(endDate.isBlank()){
            return new ResponseEntity<>("End date can't be on blank", HttpStatus.FORBIDDEN);
        }
        if(clientAuthenticated.getAccount()
                .stream()
                .noneMatch(account -> account.getNumber().equals(accountRequest.getNumber()))){
            return new ResponseEntity<>("This account doesn't belong to you", HttpStatus.FORBIDDEN);
        }

        Date start = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
        Date end = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);

        LocalDateTime startLocalDateTime = Utils.dateToLocalDateTime(start);
        LocalDateTime endLocalDateTime = Utils.dateToLocalDateTime(end).plusDays(1).minusSeconds(1);

        List<TransactionDTO> transactionsDTOList = transactionService.getTransactionsByIdAndDateBetween(accountRequest, startLocalDateTime, endLocalDateTime);

        Document document = new Document();

        PdfWriter.getInstance(document, new FileOutputStream("C:\\Users\\Rates\\Documents\\Transaction from " + startDate + " to " + endDate + ".pdf"));
        document.open();

        Image logo = Image.getInstance("C:\\Users\\Rates\\Documents\\Java Projects\\Homebanking-Mindhub-Brothers\\src\\main\\resources\\static\\web\\img\\logo.png");
        logo.scaleToFit(120, 120);
        document.add(logo);

        Font fontTitle = new Font(Font.FontFamily.HELVETICA, 25, Font.BOLD);
        Paragraph title = new Paragraph("Hello " + clientAuthenticated.getFirstName() +" "+ clientAuthenticated.getLastName(), fontTitle);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        title.setSpacingAfter(20);
        title.setSpacingBefore(20);
        document.add(title);

        Font fontSubTitle  = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        Paragraph subTitle = new Paragraph("Here are your transactions from " + startDate + " to " + endDate + " from your account " + accountRequest.getNumber(), fontSubTitle);
        subTitle.setAlignment(Paragraph.ALIGN_CENTER);
        subTitle.setSpacingAfter(20);
        subTitle.setSpacingBefore(20);
        document.add(subTitle);

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);

        PdfPCell cellAmount = new PdfPCell(new Paragraph("Amount"));
        cellAmount.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellAmount);

        PdfPCell cellDate = new PdfPCell(new Paragraph("Date"));
        cellDate.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellDate);

        PdfPCell cellDescription = new PdfPCell(new Paragraph("Description"));
        cellDescription.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellDescription);

        PdfPCell cellType = new PdfPCell(new Paragraph("Type"));
        cellType.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellType);

        PdfPCell cellTime = new PdfPCell(new Paragraph("Time"));
        cellTime.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellTime);


        for (TransactionDTO transactionDTO: transactionsDTOList){
            String date = Utils.getStringDateFromLocalDateTime(transactionDTO.getDate());
            String hour = Utils.getStringHourFromLocalDateTime(transactionDTO.getDate());

            PdfPCell cellTransactionAmount = new PdfPCell(new Paragraph(
                    transactionDTO.getType().name().equals("DEBIT")? "-" + (NumberFormat.getNumberInstance(Locale.US).format(transactionDTO.getAmount())) : NumberFormat.getNumberInstance(Locale.US).format(transactionDTO.getAmount())));
            cellTransactionAmount.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellTransactionAmount.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cellTransactionAmount.setFixedHeight(50);
            table.addCell(cellTransactionAmount);

            PdfPCell cellTransactionDate = new PdfPCell(new Paragraph(date));
            cellTransactionDate.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellTransactionDate.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cellTransactionDate.setFixedHeight(50);
            table.addCell(cellTransactionDate);

            PdfPCell cellTransactionDescription = new PdfPCell(new Paragraph(transactionDTO.getDescription()));
            cellTransactionDescription.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellTransactionDescription.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cellTransactionDescription.setFixedHeight(50);
            table.addCell(cellTransactionDescription);

            PdfPCell cellTransactionType = new PdfPCell(new Paragraph(transactionDTO.getType().name()));
            cellTransactionType.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellTransactionType.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cellTransactionType.setFixedHeight(50);
            table.addCell(cellTransactionType);

            PdfPCell cellTransactionTime = new PdfPCell(new Paragraph(hour));
            cellTransactionTime.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellTransactionTime.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cellTransactionTime.setFixedHeight(50);
            table.addCell(cellTransactionTime);

        }
        document.add(table);

        document.close();

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> createTransaction(
            @RequestParam double balance, @RequestParam String description,
            @RequestParam String numberAccountOrigin, @RequestParam String numberAccountDestiny,
            Authentication authentication){

        Account accountOrigin = accountService.findByNumber(numberAccountOrigin);
        Account accountDestiny = accountService.findByNumber(numberAccountDestiny);
        Client clientAuthenticated = clientService.findByEmail(authentication.getName());

        if(accountOrigin == null){
            return new ResponseEntity<>("Account origin doesn't exist", HttpStatus.FORBIDDEN);
        }
        if(accountDestiny == null){
            return new ResponseEntity<>("Account destiny doesn't exist", HttpStatus.FORBIDDEN);
        }
        if(clientAuthenticated.getAccounts()
                .stream().noneMatch(account -> account.getNumber().contains(numberAccountOrigin))){
            return new ResponseEntity<>("This account doesn't belong you", HttpStatus.FORBIDDEN);
        }
        if(accountOrigin.getBalance() <= balance){
            return new ResponseEntity<>("You don't have enough balance", HttpStatus.FORBIDDEN);
        }
        if(numberAccountOrigin.equals(numberAccountDestiny)){
            return new ResponseEntity<>("Account origin number and account destiny number can't be the same", HttpStatus.FORBIDDEN);
        }
        if (balance < 1.00){
            return new ResponseEntity<>("Balance has be greater than US$1.00", HttpStatus.FORBIDDEN);
        }
        if(description.isBlank()){
            return new ResponseEntity<>("Description is in blank", HttpStatus.FORBIDDEN);
        }
        if(numberAccountOrigin.isBlank()){
            return new ResponseEntity<>("Account origin is in blank", HttpStatus.FORBIDDEN);
        }
        if(numberAccountDestiny.isBlank()){
            return new ResponseEntity<>("Account destiny is in blank", HttpStatus.FORBIDDEN);
        }

        Transaction transactionDebit = new Transaction(TransactionType.DEBIT, balance, description, LocalDateTime.now());
        accountOrigin.addTransaction(transactionDebit);
        accountOrigin.setBalance(accountOrigin.getBalance() - balance);
        transactionService.saveTransaction(transactionDebit);

        Transaction transactionCredit = new Transaction(TransactionType.CREDIT, balance, description, LocalDateTime.now());
        accountDestiny.addTransaction(transactionCredit);
        accountDestiny.setBalance(accountDestiny.getBalance() + balance);
        transactionService.saveTransaction(transactionCredit);

        accountService.saveAccount(accountOrigin);
        accountService.saveAccount(accountDestiny);

        return new ResponseEntity<>("The transactions was successfully", HttpStatus.CREATED);
    }
}
