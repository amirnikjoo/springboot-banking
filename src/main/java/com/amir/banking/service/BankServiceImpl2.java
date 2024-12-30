package com.amir.banking.service;

import com.amir.banking.component.TransactionLogger;
import com.amir.banking.core.AccountAlreadyExistsException;
import com.amir.banking.dto.TransactionDto;
import com.amir.banking.model.BankAccount;
import com.amir.banking.repository.BankAccountRepository;
import com.amir.banking.strategy.*;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Primary
public class BankServiceImpl2 implements BankService {
    private final BankAccountRepository bankAccountRepository;
    private final TransactionContext transactionContext;
    private final TransactionLogger logger;

    public BankServiceImpl2(BankAccountRepository bankAccountRepository, TransactionContext transactionContext, TransactionLogger logger) {
        this.bankAccountRepository = bankAccountRepository;
        this.transactionContext = transactionContext;
        this.logger = logger;
    }

    @Transactional
    public BankAccount createAccount(String traceId, TransactionDto dto) throws Exception {
        createAccountValidation(traceId, dto);

        transactionContext.setStrategy(new CreateAccountStrategy(bankAccountRepository, logger));
        return transactionContext.executeTransaction(traceId, new TransactionDto(dto.getAccount(), "", dto.getHolderName(), dto.getAmount()));
    }

    private void createAccountValidation(String traceId, TransactionDto dto) throws Exception {
        //todo: account validation and checking here
        BankAccount account = bankAccountRepository.findByAccountNumber(dto.getAccount());
        if (account != null) {
            throw new AccountAlreadyExistsException(traceId);
        }
    }

    @Transactional
    @Override
    public double doDeposit(String traceId, TransactionDto dto) throws Exception {
        transactionContext.setStrategy(new DepositStrategy(bankAccountRepository, logger));
        BankAccount bankAccount = transactionContext.executeTransaction(traceId, dto);
        return bankAccount.getBalance();
    }

    @Override
    @Transactional
    public double doWithdraw(String traceId, TransactionDto dto) throws Exception {
        transactionContext.setStrategy(new WithdrawStrategy(bankAccountRepository, logger));
        BankAccount bankAccount = transactionContext.executeTransaction(traceId, dto);
        return bankAccount.getBalance();
    }

    @Override
    @Transactional
    public double doTransfer(String traceId, TransactionDto dto) throws Exception {
        transactionContext.setStrategy(new TransferStrategy(bankAccountRepository, logger));
        BankAccount bankAccount = transactionContext.executeTransaction(traceId, dto);
        return bankAccount.getBalance();
    }

    @Override
    @Transactional
    public double getBalance(String traceId, TransactionDto dto) throws Exception {
        transactionContext.setStrategy(new BalanceStrategy(bankAccountRepository, logger));
        BankAccount bankAccount = transactionContext.executeTransaction(traceId, dto);
        return bankAccount.getBalance();
    }
}