package com.amir.banking.strategy;

import com.amir.banking.core.AccountNotFoundException;
import com.amir.banking.core.InsufficientFundException;
import com.amir.banking.dto.TransactionInputDto;
import com.amir.banking.model.BankAccount;
import com.amir.banking.component.TransactionLogger;
import com.amir.banking.repository.BankAccountRepository;
import com.amir.banking.util.AppConstants;
import org.springframework.stereotype.Component;

@Component
public class TransferStrategy implements TransactionStrategy {
    private final BankAccountRepository bankAccountRepository;
    private final TransactionLogger transactionLogger;

    public TransferStrategy(BankAccountRepository bankAccountRepository, TransactionLogger transactionLogger) {
        this.bankAccountRepository = bankAccountRepository;
        this.transactionLogger = transactionLogger;
    }

    @Override  
    public BankAccount execute(String traceId, TransactionInputDto dto) throws Exception {
        String accountFrom = dto.getAccountNo();
        String accountTo = dto.getAccountTo();

        BankAccount fromAccount = bankAccountRepository.findByAccountNumber(accountFrom);
        BankAccount toAccount = bankAccountRepository.findByAccountNumber(accountTo);

        if (fromAccount == null || toAccount == null) {
            throw new AccountNotFoundException(traceId);
        }
        if (fromAccount.getBalance() < dto.getAmount()) {
            throw new InsufficientFundException(traceId);
        }
        double amount = dto.getAmount();

        fromAccount.withdraw(amount);
        toAccount.deposit(amount);
        bankAccountRepository.save(fromAccount);
        bankAccountRepository.save(toAccount);
        transactionLogger.onTransaction(traceId, accountFrom, AppConstants.TRANSACTION_TYPE_TRANSFER_FROM, amount);
        transactionLogger.onTransaction(traceId, accountTo, AppConstants.TRANSACTION_TYPE_TRANSFER_TO, amount);
        return fromAccount;
    }
}
