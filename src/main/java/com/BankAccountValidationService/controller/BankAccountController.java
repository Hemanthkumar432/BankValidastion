package com.BankAccountValidationService.controller;
import javax.xml.bind.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.BankAccountValidationService.bean.BankAccount;
import com.BankAccountValidationService.service.BankAccountService;

@RestController
public class BankAccountController
{
    @Autowired
    private  BankAccountService bankAccountService;

    @PostMapping(value = "/bank-accounts", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> saveBankAccount(@RequestBody BankAccount bankAccount)
    {
        try
        {
            bankAccountService.saveBankAccount(bankAccount);
          String res=  bankAccountService.getIFSCDetiles(bankAccount.getIfsc());
            return ResponseEntity.ok(res);
        }
        catch (ValidationException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
        catch (Exception ex)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }
}
