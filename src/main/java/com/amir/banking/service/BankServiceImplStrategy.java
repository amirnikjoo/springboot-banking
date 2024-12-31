package com.amir.banking.service;

import com.amir.banking.component.TransactionLogger;
import com.amir.banking.core.AccountAlreadyExistsException;
import com.amir.banking.dto.TransactionInputDto;
import com.amir.banking.model.BankAccount;
import com.amir.banking.repository.BankAccountRepository;
import com.amir.banking.strategy.*;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Primary
public class BankServiceImplStrategy implements BankService {
    private final BankAccountRepository bankAccountRepository;
    private final TransactionContext transactionContext;
    private final TransactionLogger logger;

    public BankServiceImplStrategy(BankAccountRepository bankAccountRepository, TransactionContext transactionContext, TransactionLogger logger) {
        this.bankAccountRepository = bankAccountRepository;
        this.transactionContext = transactionContext;
        this.logger = logger;
    }

    @Transactional
    public BankAccount createAccount(String traceId, TransactionInputDto dto) throws Exception {
        createAccountValidation(traceId, dto);

        transactionContext.setStrategy(new CreateAccountStrategy(bankAccountRepository, logger));
        return transactionContext.executeTransaction(traceId, new TransactionInputDto(dto.getAccountNo(), "", dto.getName(), dto.getAmount()));
    }

    private void createAccountValidation(String traceId, TransactionInputDto dto) throws Exception {
        //todo: account validation and checking here
        BankAccount account = bankAccountRepository.findByAccountNumber(dto.getAccountNo());
        if (account != null) {
            throw new AccountAlreadyExistsException(traceId);
        }
        throw new RuntimeException("alaki");
    }

    @Transactional
    @Override
    public BankAccount doDeposit(String traceId, TransactionInputDto dto) throws Exception {
        transactionContext.setStrategy(new DepositStrategy(bankAccountRepository, logger));
        BankAccount bankAccount = transactionContext.executeTransaction(traceId, dto);
        return bankAccount;
    }

    @Override
    @Transactional
    public BankAccount doWithdraw(String traceId, TransactionInputDto dto) throws Exception {
        transactionContext.setStrategy(new WithdrawStrategy(bankAccountRepository, logger));
        BankAccount bankAccount = transactionContext.executeTransaction(traceId, dto);
        return bankAccount;
    }

    @Override
    @Transactional
    public BankAccount doTransfer(String traceId, TransactionInputDto dto) throws Exception {
        transactionContext.setStrategy(new TransferStrategy(bankAccountRepository, logger));
        BankAccount bankAccount = transactionContext.executeTransaction(traceId, dto);
        return bankAccount;
    }

    @Override
    @Transactional
    public BankAccount getBalance(String traceId, TransactionInputDto dto) throws Exception {
        transactionContext.setStrategy(new BalanceStrategy(bankAccountRepository, logger));
        BankAccount bankAccount = transactionContext.executeTransaction(traceId, dto);
        return bankAccount;
    }
}