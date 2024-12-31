package com.amir.banking.component;

import com.amir.banking.dto.TransactionInputDto;
import com.amir.banking.model.BankAccount;
import com.amir.banking.service.BankService;
import com.amir.banking.service.BankServiceImplStrategy;
import com.amir.banking.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;

import java.util.Scanner;

import static com.amir.banking.util.AppConstants.TEST_TRACE_ID_LEN;

//@Component
@Order(2)
public class BankingConsoleUI implements CommandLineRunner {
    private final BankService bankService;

    @Autowired
    public BankingConsoleUI(BankServiceImplStrategy bankService) {
        this.bankService = bankService;
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        int choice = 0;
        String accountNo;

        System.out.print("enter source accountNo: ");
        accountNo = scanner.next();
        try {
            bankService.getBalance(StringUtil.generateRandomTraceId(TEST_TRACE_ID_LEN), new TransactionInputDto(accountNo));
        } catch (Exception e) {
            System.out.println("invalid account number, try again...");
        }

        do {
            displayMenu();
            System.out.print("Choose an option: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    BankAccount balance = bankService.getBalance(StringUtil.generateRandomTraceId(TEST_TRACE_ID_LEN), new TransactionInputDto(accountNo));
                    printMe(String.format("Account detail Balance: %s", balance.toString()));
                    break;
                case 2:
                    System.out.print("Enter amount to deposit: ");
                    double depositAmount = scanner.nextDouble();
                    BankAccount deposit = bankService.doDeposit(StringUtil.generateRandomTraceId(TEST_TRACE_ID_LEN),
                            new TransactionInputDto(accountNo, depositAmount));
                    printMe(String.format("Account detail After deposit: %s", deposit.toString()));

                    break;
                case 3:
                    System.out.print("Enter amount to withdraw: ");
                    double withdrawAmount = scanner.nextDouble();
                    BankAccount withdraw = bankService.doWithdraw(StringUtil.generateRandomTraceId(TEST_TRACE_ID_LEN),
                            new TransactionInputDto(accountNo, withdrawAmount));
                    printMe(String.format("Account detail After withdraw: %s", withdraw.toString()));
                    break;
                case 4:
                    System.out.print("Enter destination account: ");
                    String accountNo2 = scanner.next();
                    System.out.print("Enter amount to transfer: ");
                    double transferAmount = scanner.nextDouble();
                    BankAccount transfer = bankService.doTransfer(StringUtil.generateRandomTraceId(TEST_TRACE_ID_LEN),
                            new TransactionInputDto(accountNo, accountNo2, transferAmount));
                    printMe(String.format("Account detail After transfer: %s", transfer.toString()));
                    break;
                case 0:
                    System.out.println("Thank you for using the Banking Application!");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (choice != 0);

        System.out.println("Have a nice time...");
        scanner.close();
    }

    private void printMe(String input) {
        System.out.println(input);
    }

    private void displayMenu() {
        System.out.println("\nWelcome to the Banking Application");
        System.out.println("-----------------------------------");
        System.out.println("1. View Balance");
        System.out.println("2. Deposit Money");
        System.out.println("3. Withdraw Money");
        System.out.println("4. Transfer");
        System.out.println("0. Exit");
    }
}