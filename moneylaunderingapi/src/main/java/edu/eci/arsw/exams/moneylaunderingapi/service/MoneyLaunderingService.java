package edu.eci.arsw.exams.moneylaunderingapi.service;

import edu.eci.arsw.exams.moneylaunderingapi.model.SuspectAccount;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("MoneyLaunderingService")
public interface MoneyLaunderingService {
    void updateAccountStatus(SuspectAccount suspectAccount) throws  MoneyLaunderingServiceException;
    SuspectAccount getAccountStatus(String accountId) throws MoneyLaunderingServiceException;
    List<SuspectAccount> getSuspectAccounts() throws MoneyLaunderingServiceException;
    void createAccount(SuspectAccount suspectAccount) throws  MoneyLaunderingServiceException;
    SuspectAccount getSuspectAccountsById(String accountId) throws MoneyLaunderingServiceException;
}
