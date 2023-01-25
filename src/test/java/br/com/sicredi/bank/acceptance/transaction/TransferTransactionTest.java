package br.com.sicredi.bank.acceptance.transaction;

import br.com.sicredi.bank.factory.account.AccountBuilder;
import br.com.sicredi.bank.factory.associate.AssociateBuilder;
import br.com.sicredi.bank.factory.transaction.TransactionBuilder;
import br.com.sicredi.bank.model.request.account.AccountRequest;
import br.com.sicredi.bank.model.request.associate.SaveAssociateRequest;
import br.com.sicredi.bank.model.request.transaction.DepositTransactionRequest;
import br.com.sicredi.bank.model.request.transaction.TransferTransactionRequest;
import br.com.sicredi.bank.model.response.associate.SaveAssociateResponse;
import br.com.sicredi.bank.model.response.transaction.TransactionResponse;
import br.com.sicredi.bank.service.AssociateService;
import br.com.sicredi.bank.service.TransactionService;
import br.com.sicredi.bank.utils.Utils;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static br.com.sicredi.bank.utils.Message.*;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Transaction")
@Epic("Transfer Transaction")
public class TransferTransactionTest {

    TransactionService transactionService = new TransactionService();
    TransactionBuilder transactionBuilder = new TransactionBuilder();
    AccountBuilder accountBuilder = new AccountBuilder();
    AssociateService associateService = new AssociateService();
    AssociateBuilder associateBuilder = new AssociateBuilder();

    @Test
    @Tag("all")
    @Description("Must transfer between two accounts successfully")
    public void mustTransferBetweenTwoAccountsSuccessfully() {
        SaveAssociateRequest associateRequest = associateBuilder.buildSaveAssociateRequest();

        SaveAssociateResponse associateResponse = associateService
                .saveAssociate(Utils.convertSaveAssociateRequestToJson(associateRequest))
                .then().extract().as(SaveAssociateResponse.class);

        AccountRequest accountRequest = accountBuilder.buildAccountRequest();
        accountRequest.setAgency(associateResponse.getAccounts().get(0).getAgency());
        accountRequest.setNumber(associateResponse.getAccounts().get(0).getNumber());

        DepositTransactionRequest depositRequest = transactionBuilder.buildDepositTransactionRequest();
        depositRequest.setCreditAccount(accountRequest);

        transactionService.depositTransaction(Utils.convertDepositTransactionRequestToJson(depositRequest));

        AccountRequest creditAccountRequest = accountBuilder.buildAccountRequest();
        creditAccountRequest.setAgency(associateResponse.getAccounts().get(1).getAgency());
        creditAccountRequest.setNumber(associateResponse.getAccounts().get(1).getNumber());

        TransferTransactionRequest transferRequest = transactionBuilder.buildTransferTransactionRequest();
        transferRequest.setDebitAccount(accountRequest);
        transferRequest.setCreditAccount(creditAccountRequest);

        TransactionResponse transferResponse = transactionService
                .transferTransaction(Utils.convertTransferTransactionRequestToJson(transferRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract().as(TransactionResponse.class);

        var balance = BigDecimal.ZERO.add(depositRequest.getValue()).subtract(transferRequest.getValue());

        assertEquals(transferRequest.getDebitAccount().getAgency(), transferResponse.getAccount().getAgency());
        assertEquals(transferRequest.getDebitAccount().getNumber(), transferResponse.getAccount().getNumber());
        assertEquals(balance, transferResponse.getNewBalance());

        associateService.deleteAssociate(associateResponse.getId());
    }

    @Test
    @Tag("all")
    @Description("Must not transfer from an account with value greater than the balance")
    public void mustNotTransferFromAnAccountWithValueGreaterThanTheBalance() {
        SaveAssociateRequest associateRequest = associateBuilder.buildSaveAssociateRequest();

        SaveAssociateResponse associateResponse = associateService
                .saveAssociate(Utils.convertSaveAssociateRequestToJson(associateRequest))
                .then().extract().as(SaveAssociateResponse.class);

        AccountRequest accountRequest = accountBuilder.buildAccountRequest();
        accountRequest.setAgency(associateResponse.getAccounts().get(0).getAgency());
        accountRequest.setNumber(associateResponse.getAccounts().get(0).getNumber());

        AccountRequest creditAccountRequest = accountBuilder.buildAccountRequest();
        creditAccountRequest.setAgency(associateResponse.getAccounts().get(1).getAgency());
        creditAccountRequest.setNumber(associateResponse.getAccounts().get(1).getNumber());

        TransferTransactionRequest transferRequest = transactionBuilder.buildTransferTransactionRequest();
        transferRequest.setDebitAccount(accountRequest);
        transferRequest.setCreditAccount(creditAccountRequest);

        transactionService.transferTransaction(Utils.convertTransferTransactionRequestToJson(transferRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body(containsString(TRANSACTION_BUSINESS_BALANCE_ERROR));

        associateService.deleteAssociate(associateResponse.getId());
    }

    @Test
    @Tag("all")
    @Description("Must not transfer from an account with negative value")
    public void mustNotTransferFromAnAccountWithNegativeValue() {
        SaveAssociateRequest associateRequest = associateBuilder.buildSaveAssociateRequest();

        SaveAssociateResponse associateResponse = associateService
                .saveAssociate(Utils.convertSaveAssociateRequestToJson(associateRequest))
                .then().extract().as(SaveAssociateResponse.class);

        AccountRequest accountRequest = accountBuilder.buildAccountRequest();
        accountRequest.setAgency(associateResponse.getAccounts().get(0).getAgency());
        accountRequest.setNumber(associateResponse.getAccounts().get(0).getNumber());

        AccountRequest creditAccountRequest = accountBuilder.buildAccountRequest();
        creditAccountRequest.setAgency(associateResponse.getAccounts().get(1).getAgency());
        creditAccountRequest.setNumber(associateResponse.getAccounts().get(1).getNumber());

        TransferTransactionRequest transferRequest = transactionBuilder.buildTransferWithInvalidValue();
        transferRequest.setDebitAccount(accountRequest);
        transferRequest.setCreditAccount(creditAccountRequest);

        transactionService.transferTransaction(Utils.convertTransferTransactionRequestToJson(transferRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body(containsString(TRANSACTION_VALUE_INVALID));

        associateService.deleteAssociate(associateResponse.getId());
    }

    @Test
    @Tag("all")
    @Description("Must not transfer from a nonexistent account")
    public void mustNotTransferFromANonexistentAccount() {
        SaveAssociateRequest associateRequest = associateBuilder.buildSaveAssociateRequest();

        SaveAssociateResponse associateResponse = associateService
                .saveAssociate(Utils.convertSaveAssociateRequestToJson(associateRequest))
                .then().extract().as(SaveAssociateResponse.class);

        AccountRequest creditAccountRequest = accountBuilder.buildAccountRequest();
        creditAccountRequest.setAgency(associateResponse.getAccounts().get(1).getAgency());
        creditAccountRequest.setNumber(associateResponse.getAccounts().get(1).getNumber());

        TransferTransactionRequest transferRequest = transactionBuilder.buildTransferTransactionRequest();
        transferRequest.setCreditAccount(creditAccountRequest);

        transactionService.transferTransaction(Utils.convertTransferTransactionRequestToJson(transferRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .body(containsString(ACCOUNT_FIND_ERROR));

        associateService.deleteAssociate(associateResponse.getId());
    }

    @Test
    @Tag("all")
    @Description("Must not transfer to a nonexistent destination account")
    public void mustNotTransferToANonexistentDestinationAccount() {
        SaveAssociateRequest associateRequest = associateBuilder.buildSaveAssociateRequest();

        SaveAssociateResponse associateResponse = associateService
                .saveAssociate(Utils.convertSaveAssociateRequestToJson(associateRequest))
                .then().extract().as(SaveAssociateResponse.class);

        AccountRequest accountRequest = accountBuilder.buildAccountRequest();
        accountRequest.setAgency(associateResponse.getAccounts().get(0).getAgency());
        accountRequest.setNumber(associateResponse.getAccounts().get(0).getNumber());

        TransferTransactionRequest transferRequest = transactionBuilder.buildTransferTransactionRequest();
        transferRequest.setDebitAccount(accountRequest);

        transactionService.transferTransaction(Utils.convertTransferTransactionRequestToJson(transferRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .body(containsString(ACCOUNT_FIND_ERROR));

        associateService.deleteAssociate(associateResponse.getId());
    }

    @Test
    @Tag("all")
    @Description("Must not transfer between two accounts with null fields")
    public void mustNotTransferBetweenTwoAccountsWithNullFields() {
        AccountRequest accountRequest = accountBuilder.buildAccountWithNullFields();
        AccountRequest creditAccountRequest = accountBuilder.buildAccountWithNullFields();

        TransferTransactionRequest transferRequest = transactionBuilder.buildTransferWithNullFields();
        transferRequest.setDebitAccount(accountRequest);
        transferRequest.setCreditAccount(creditAccountRequest);

        transactionService.transferTransaction(Utils.convertTransferTransactionRequestToJson(transferRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body(containsString(TRANSACTION_VALUE_NOT_NULL));
    }

}
