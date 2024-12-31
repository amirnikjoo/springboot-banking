package com.amir.banking.strategy;

import com.amir.banking.core.AccountNotFoundException;
import com.amir.banking.dto.TransactionInputDto;
import com.amir.banking.model.BankAccount;
import com.amir.banking.component.TransactionLogger;
import com.amir.banking.repository.BankAccountRepository;
import com.amir.banking.util.BankingConstants;
import org.springframework.stereotype.Component;

@Component
public class DepositStrategy implements TransactionStrategy {
    private final BankAccountRepository bankAccountRepository;
    private final TransactionLogger transactionLogger;

    public DepositStrategy(BankAccountRepository bankAccountRepository, TransactionLogger transactionLogger) {
        this.bankAccountRepository = bankAccountRepository;
        this.transactionLogger = transactionLogger;
    }

    @Override  
    public BankAccount execute(String traceId, TransactionInputDto dto) throws AccountNotFoundException {
        BankAccount account = bankAccountRepository.findByAccountNumber(dto.getAccountNo());
        if (account == null) {
            throw new AccountNotFoundException(traceId);
        }
        account.deposit(dto.getAmount());
        bankAccountRepository.save(account);

        transactionLogger.onTransaction(traceId, dto.getAccountNo(), BankingConstants.TRANSACTION_TYPE_DEPOSIT, dto.getAmount());
        return account;
    }  
}