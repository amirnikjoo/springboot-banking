package com.amir.banking.strategy;

import com.amir.banking.core.AccountNotFoundException;
import com.amir.banking.dto.TransactionInputDto;
import com.amir.banking.model.BankAccount;
import com.amir.banking.component.TransactionLogger;
import com.amir.banking.repository.BankAccountRepository;
import com.amir.banking.util.AppConstants;
import org.springframework.stereotype.Component;

@Component
public class BalanceStrategy implements TransactionStrategy {
    private final BankAccountRepository bankAccountRepository;
    private final TransactionLogger transactionLogger;

    public BalanceStrategy(BankAccountRepository bankAccountRepository, TransactionLogger transactionLogger) {
        this.bankAccountRepository = bankAccountRepository;
        this.transactionLogger = transactionLogger;
    }

    @Override
    public BankAccount execute(String traceId, TransactionInputDto dto) throws Exception {
        BankAccount account = bankAccountRepository.findByAccountNumber(dto.getAccountNo());
        if (account == null) {
            throw new AccountNotFoundException(traceId);
        }
        transactionLogger.onTransaction(traceId, dto.getAccountNo(), AppConstants.TRANSACTION_TYPE_BALANCE, account.getBalance());
        return account;
    }
}
