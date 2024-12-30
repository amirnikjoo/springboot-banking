package com.amir.banking.service;

import com.amir.banking.component.TransactionLogger;
import com.amir.banking.core.AccountAlreadyExistsException;
import com.amir.banking.core.AccountNotFoundException;
import com.amir.banking.core.InsufficientFundException;
import com.amir.banking.dto.TransactionDto;
import com.amir.banking.model.BankAccount;
import com.amir.banking.repository.BankAccountRepository;
import com.amir.banking.util.BankingConstants;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
//@Primary
public class BankServiceImpl implements BankService {
    private final BankAccountRepository bankAccountRepository;
    private final TransactionLogger transactionLogger;

    public BankServiceImpl(BankAccountRepository bankAccountRepository, TransactionLogger logger) {
        this.bankAccountRepository = bankAccountRepository;
        this.transactionLogger = logger;
    }

    @Transactional
    public BankAccount createAccount(String traceId, TransactionDto dto) throws Exception{
        createAccountValidation(traceId, dto);

        BankAccount newAccount = bankAccountRepository.save( new BankAccount(dto.getAccount(), dto.getHolderName(), dto.getAmount()));
        transactionLogger.onTransaction(traceId, dto.getAccount(), BankingConstants.TRANSACTION_TYPE_CREATE_ACCOUNT, dto.getAmount());

        return newAccount;
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
        BankAccount account = bankAccountRepository.findByAccountNumber(dto.getAccount());
        if (account == null) {
            throw new AccountNotFoundException(traceId);
        }

        account.deposit(dto.getAmount());
        bankAccountRepository.save(account);

        transactionLogger.onTransaction(traceId, dto.getAccount(), BankingConstants.TRANSACTION_TYPE_DEPOSIT, dto.getAmount());
        return account.getBalance();
    }

    @Override
    @Transactional
    public double doWithdraw(String traceId, TransactionDto dto) throws Exception {
        BankAccount account = bankAccountRepository.findByAccountNumber(dto.getAccount());
        if (account == null) {
            throw new AccountNotFoundException(traceId);
        }
        if (account.getBalance() < dto.getAmount()) {
            throw new InsufficientFundException(traceId);
        }
        account.withdraw(dto.getAmount());
        bankAccountRepository.save(account);

        transactionLogger.onTransaction(traceId, dto.getAccount(), BankingConstants.TRANSACTION_TYPE_WITHDRAW, dto.getAmount());
        return account.getBalance();
    }

    @Override
    @Transactional
    public double doTransfer(String traceId, TransactionDto dto) throws Exception {
        BankAccount fromAccount = bankAccountRepository.findByAccountNumber(dto.getAccount());
        BankAccount toAccount = bankAccountRepository.findByAccountNumber(dto.getAccountTo());

        if (fromAccount == null || toAccount == null) {
            throw new AccountNotFoundException(traceId);
        }

        double fromBalance = fromAccount.withdraw(dto.getAmount());
        toAccount.deposit(dto.getAmount());
        bankAccountRepository.save(fromAccount);
        bankAccountRepository.save(toAccount);
        transactionLogger.onTransaction(traceId, dto.getAccount(), BankingConstants.TRANSACTION_TYPE_TRANSFER_FROM, dto.getAmount());
        transactionLogger.onTransaction(traceId, dto.getAccountTo(), BankingConstants.TRANSACTION_TYPE_TRANSFER_TO, dto.getAmount());
        return fromBalance;

    }

    public double getBalance(String traceId, TransactionDto dto) throws Exception {
        BankAccount account = bankAccountRepository.getByAccountNumber(dto.getAccount());
        if (account == null) {
            throw new AccountNotFoundException(traceId);
        }
        transactionLogger.onTransaction(traceId, dto.getAccount(), BankingConstants.TRANSACTION_TYPE_BALANCE, account.getBalance());
        return account.getBalance();
    }
}