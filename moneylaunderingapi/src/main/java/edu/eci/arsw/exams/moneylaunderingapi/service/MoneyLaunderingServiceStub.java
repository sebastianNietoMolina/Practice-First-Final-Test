package edu.eci.arsw.exams.moneylaunderingapi.service;

import edu.eci.arsw.exams.moneylaunderingapi.model.SuspectAccount;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class MoneyLaunderingServiceStub implements MoneyLaunderingService {

    List<SuspectAccount> cuentas;

    public MoneyLaunderingServiceStub() {
        cuentas=new CopyOnWriteArrayList<>();
        SuspectAccount cuenta1=new SuspectAccount("1",346);
        SuspectAccount cuenta2=new SuspectAccount("2",568);
        SuspectAccount cuenta3=new SuspectAccount("3",452);
        cuentas.add(cuenta1);
        cuentas.add(cuenta2);
        cuentas.add(cuenta3);
    }

    @Override
    public void updateAccountStatus(SuspectAccount suspectAccount) throws  MoneyLaunderingServiceException  {

        SuspectAccount cuenta = getSuspectAccountsById(suspectAccount.getAccountId());
        cuenta.setAmountOfSmallTransactions(suspectAccount.getAmountOfSmallTransactions());

    }

    @Override
    public SuspectAccount getAccountStatus(String accountId) throws MoneyLaunderingServiceException{
        for(SuspectAccount cuenta: cuentas){
            if(cuenta.getAccountId().equals(accountId)){
                return cuenta;
            }
        }
        throw new MoneyLaunderingServiceException("No existe esa cuenta");
    }

    @Override
    public List<SuspectAccount> getSuspectAccounts() throws MoneyLaunderingServiceException{
        return cuentas;
    }

    @Override
    public void createAccount(SuspectAccount suspectAccount) throws MoneyLaunderingServiceException {
        cuentas.add(suspectAccount);
    }

    @Override
    public SuspectAccount getSuspectAccountsById(String accountId) throws MoneyLaunderingServiceException {
        for(SuspectAccount cuenta: cuentas){
            if(cuenta.getAccountId().equals(accountId)){
                return cuenta;
            }
        }
        throw new MoneyLaunderingServiceException("No existe esa cuenta");
    }
}
