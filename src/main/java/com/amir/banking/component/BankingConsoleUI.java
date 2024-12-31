package com.amir.banking.component;

import com.amir.banking.dto.TransactionInputDto;
import com.amir.banking.service.BankService;
import com.amir.banking.service.BankServiceImpl2;
import com.amir.banking.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;

import java.util.Scanner;

import static com.amir.banking.util.BankingConstants.TEST_TRACE_ID_LEN;

//@Component
@Order(2)
public class BankingConsoleUI implements CommandLineRunner {
    private final BankService bankService;

    @Autowired
    public BankingConsoleUI(BankServiceImpl2 bankService) {
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
                    System.out.printf("Current Balance: %.2f%n",
                            bankService.getBalance(StringUtil.generateRandomTraceId(TEST_TRACE_ID_LEN), new TransactionInputDto(accountNo)));
                    break;
                case 2:
                    System.out.print("Enter amount to deposit: ");
                    double depositAmount = scanner.nextDouble();
                    bankService.doDeposit(StringUtil.generateRandomTraceId(TEST_TRACE_ID_LEN),
                            new TransactionInputDto(accountNo, depositAmount));
                    break;
                case 3:
                    System.out.print("Enter amount to withdraw: ");
                    double withdrawAmount = scanner.nextDouble();
                    bankService.doWithdraw(StringUtil.generateRandomTraceId(TEST_TRACE_ID_LEN),
                            new TransactionInputDto(accountNo, withdrawAmount));
                    break;
                case 4:
                    System.out.print("Enter destination account: ");
                    String accountNo2 = scanner.next();
                    System.out.print("Enter amount to transfer: ");
                    double transferAmount = scanner.nextDouble();
                    bankService.doTransfer(StringUtil.generateRandomTraceId(TEST_TRACE_ID_LEN),
                            new TransactionInputDto(accountNo, accountNo2, transferAmount));
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

    private void displayMenu() {
        System.out.println("Welcome to the Banking Application");
        System.out.println("-----------------------------------");
        System.out.println("1. View Balance");
        System.out.println("2. Deposit Money");
        System.out.println("3. Withdraw Money");
        System.out.println("4. Transfer");
        System.out.println("0. Exit");
    }
}