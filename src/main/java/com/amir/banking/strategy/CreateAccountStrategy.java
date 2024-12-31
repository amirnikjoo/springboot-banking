package com.amir.banking.strategy;

import com.amir.banking.component.TransactionLogger;
import com.amir.banking.dto.TransactionInputDto;
import com.amir.banking.model.BankAccount;
import com.amir.banking.repository.BankAccountRepository;
import com.amir.banking.util.BankingConstants;
import org.springframework.stereotype.Component;

@Component
public class CreateAccountStrategy implements TransactionStrategy {
    private final BankAccountRepository bankAccountRepository;
    private final TransactionLogger transactionLogger;

    public CreateAccountStrategy(BankAccountRepository bankAccountRepository, TransactionLogger transactionLogger) {
        this.bankAccountRepository = bankAccountRepository;
        this.transactionLogger = transactionLogger;
    }

    @Override
    public BankAccount execute(String traceId, TransactionInputDto dto) {
        BankAccount account = new BankAccount(dto.getAccountNo(), dto.getName(), dto.getAmount());
        bankAccountRepository.save(account);
        transactionLogger.onTransaction(traceId, dto.getAccountNo(), BankingConstants.TRANSACTION_TYPE_CREATE_ACCOUNT, account.getBalance());
        return account;
    }
}
