package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.AccountServices;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.UserService;
import com.techelevator.tenmo.services.TransferService;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);

    private AuthenticatedUser currentUser;

    private final AccountServices accountServices = new AccountServices();
    private final UserService userService = new UserService(API_BASE_URL);
    private final TransferService transferService = new TransferService(API_BASE_URL);
    NumberFormat currFormat = NumberFormat.getCurrencyInstance();
    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if(menuSelection == 5){
                viewTransferHistory();
            } else if (menuSelection == 6) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

	private void viewCurrentBalance() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        accountServices.setAuthToken(currentUser.getToken());
        System.out.println(formatter.format(accountServices.getAccountBalance()));
	}

	private void viewTransferHistory() {
		// TODO Auto-generated method stub
		userService.setAuthToken(currentUser.getToken());
        transferService.setAuthToken(currentUser.getToken());
        // Get the transfer history of the current user
        displayTransfers(transferService.getTransfers(currentUser.getUser().getId()));
	}

    private void displayTransfers(List<TransferResponse> transfers) {
        String divider = "---------------------------------";
        System.out.println(divider);
        System.out.println("Transfer");
        System.out.println("Id     Sender        Recipient       Amount");
        System.out.println(divider);
        for(TransferResponse transfer: transfers){
            System.out.println(transfer.getTransferId() + "     " +
                    userService.getUserById(transfer.getSenderId()).getUsername() + "        " +
                    userService.getUserById(transfer.getReceiverId()).getUsername() + "       " + // fix possible
                    currFormat.format(transfer.getTransferAmount()));
        }
        System.out.println(divider);

        int menuSelection = -1;
        while(menuSelection != 0){
            menuSelection = consoleService.promptForInt("Please enter transfer ID to view details (0  to cancel):");
            if(menuSelection != 0){
                displayTransferDetails(currentUser.getUser().getId(), menuSelection);
                System.out.println();
            }

        }
    }

    private void viewPendingRequests() {
		// TODO Auto-generated method stub
		
	}

	private void sendBucks() {
        userService.setAuthToken(currentUser.getToken());
        transferService.setAuthToken(currentUser.getToken());
        List<User> users = userService.getUsers();
        displayUsers(users);
        int recipientID = consoleService.promptForMenuSelection("Enter the account ID of the user you want to send TEbucks to: ");
        BigDecimal amount = consoleService.promptForBigDecimal("Enter the amount you would like to transfer: $");
        transferService.sendTEBucks(new TransferRequest(currentUser.getUser().getId(), recipientID, amount));
	}

	private void requestBucks() {
		// TODO Auto-generated method stub
		
	}
    private void displayUsers(List<User> users){
        for(User user: users){
            System.out.println((user.getId()+1000) + " " + user.getUsername());
        }
    }

    private void displayTransferDetails(int userid, int transferResponseId){
        TransferResponse transferResponse = transferService.getTransferById(userid, transferResponseId);
        String divider = "---------------------------------------";
        System.out.println("Transfer Details ");
        System.out.println("Id: " + transferResponse.getTransferId() );
        System.out.println("From: " + userService.getUserById(transferResponse.getSenderId()).getUsername());
        System.out.println("To: " + userService.getUserById(transferResponse.getReceiverId()).getUsername());
        System.out.println("Type: " + transferResponse.getTransferType());
        System.out.println("Status: " + transferResponse.getTransferStatus());


        String transferAmount = currFormat.format(transferResponse.getTransferAmount());
        System.out.println("Amount: " + transferAmount);
        System.out.println(divider);
        System.out.println();
    }


}
