package com.BankAccountValidationService.service;

import java.util.regex.Pattern;
import javax.xml.bind.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.BankAccountValidationService.bean.BankAccount;
import com.BankAccountValidationService.repo.BankAccountRepository;

@Service
public class BankAccountService
{

    @Autowired
	private  BankAccountRepository repository;
    @Autowired
    private  RestTemplate restTemplate;
    String url = "https://ifsc.razorpay.com/";

    public void saveBankAccount(BankAccount bankAccount) throws ValidationException {
        boolean isIFSCValid;
        boolean isPANValid;
        boolean isAadharValid;
        StringBuilder errorMessages = new StringBuilder();

        try
        {
            isIFSCValid = validateIFSC(bankAccount.getIfsc());
            isPANValid = validatePAN(bankAccount.getPan());
            isAadharValid = validateAadhar(bankAccount.getAadhar());
        }
        catch (Exception e)
        {
            throw new ValidationException(e.getMessage());
        }

        if (!isIFSCValid)
        {
            errorMessages.append("Invalid IFSC code. ");
        }

        if (!isPANValid)
        {
            errorMessages.append("Invalid PAN number. ");
        }

        if (!isAadharValid)
        {
            errorMessages.append("Invalid Aadhar number. ");
        }

        if (errorMessages.length() > 0)
        {
            throw new ValidationException(errorMessages.toString());
        }

        try
        {
            repository.save(bankAccount);
        }
        catch (Exception e)
        {
            throw new ValidationException(e.getMessage());
        }
    }

    private boolean validateIFSC(String ifsc) throws ValidationException
    {
        if (ifsc == null)
        {
            throw new ValidationException("IFSC is null. Please enter IFSC.");
        }

        if (ifsc.isEmpty())
        {
            throw new ValidationException("IFSC is empty. Please enter IFSC.");
        }

        try
        {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url+ifsc, String.class);
            return responseEntity.getStatusCode().is2xxSuccessful() ? true : false;
        }
        catch (Exception e)
        {
            throw new ValidationException("Given IFSC is invalid.Please enter a valid IFSC.", e);
        }
    }

    public String getIFSCDetiles (String ifsc) throws ValidationException
    {
       try
       {
           ResponseEntity<String> responseEntity = restTemplate.getForEntity(url+ifsc, String.class);
           String responseBody = responseEntity.getBody();
           return responseBody;
       }
       catch (Exception e)
       {
           throw new ValidationException(e.getMessage());
       }
    }

    private boolean validatePAN(String panNumber)
    {
        String panPattern = "^[A-Z]{5}[0-9]{4}[A-Z]{1}$";
        return Pattern.matches(panPattern, panNumber);
    }
    private boolean validateAadhar(String aadharNumber)
    {
        String aadharPattern = "^[2-9]{1}[0-9]{3}\\s[0-9]{4}\\s[0-9]{4}$";
        return Pattern.matches(aadharPattern, aadharNumber);
    }
}
