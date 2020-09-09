package edu.eci.arsw.exams.moneylaunderingapi;


import edu.eci.arsw.exams.moneylaunderingapi.model.SuspectAccount;
import edu.eci.arsw.exams.moneylaunderingapi.service.MoneyLaunderingService;
import edu.eci.arsw.exams.moneylaunderingapi.service.MoneyLaunderingServiceException;
import edu.eci.arsw.exams.moneylaunderingapi.service.MoneyLaunderingServiceStub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping( value = "/fraud-bank-accounts")
public class MoneyLaunderingController
{

    @Autowired
    MoneyLaunderingService ml;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> offendingAccounts() {
        try {
            return new ResponseEntity<>(ml.getSuspectAccounts(), HttpStatus.ACCEPTED);
        } catch (MoneyLaunderingServiceException e) {
            return new ResponseEntity<>("ERROR 400", HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> crateAccount(@RequestBody SuspectAccount sa){
        try {
            ml.createAccount(sa);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (MoneyLaunderingServiceException ex) {
            return new ResponseEntity<>("500", HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(value = "/{accountId}", method = RequestMethod.GET)
    public ResponseEntity<?> offendingAccountsById(@PathVariable String accountId) {
        try {
            return new ResponseEntity<>(ml.getSuspectAccountsById(accountId), HttpStatus.ACCEPTED);
        } catch (MoneyLaunderingServiceException e) {
            return new ResponseEntity<>("ERROR 400", HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/{accountId}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateAccountStatus(@PathVariable String accountId,@RequestBody SuspectAccount sa){
        try {
            ml.updateAccountStatus(sa);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (MoneyLaunderingServiceException ex) {
            return new ResponseEntity<>("500", HttpStatus.FORBIDDEN);
        }
    }
}
