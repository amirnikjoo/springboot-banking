package com.amir.banking.repository;

import com.amir.banking.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT b FROM BankAccount b WHERE b.accountNumber = :accountNumber")
    BankAccount findByAccountNumber(String accountNumber);

    @Query("SELECT b FROM BankAccount b WHERE b.accountNumber = :accountNumber")
    BankAccount getByAccountNumber(String accountNumber);


    @Modifying
    @Transactional
    @Query("update BankAccount a set a.balance = a.balance + :amount where a.id = :id")
    void depositById(Long id, double amount);


}