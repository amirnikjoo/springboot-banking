package com.amir.banking.strategy;

import com.amir.banking.component.TransactionLogger;
import com.amir.banking.dto.TransactionDto;
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
    public BankAccount execute(String traceId, TransactionDto dto) {
        BankAccount account = new BankAccount(dto.getAccount(), dto.getHolderName(), dto.getAmount());
        bankAccountRepository.save(account);
        transactionLogger.onTransaction(traceId, dto.getAccount(), BankingConstants.TRANSACTION_TYPE_CREATE_ACCOUNT, account.getBalance());
        return account;
    }
}
