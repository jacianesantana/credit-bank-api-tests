package br.com.sicredi.bank.aceitacao.transaction;

import br.com.sicredi.bank.builder.account.AccountBuilder;
import br.com.sicredi.bank.builder.associate.AssociateBuilder;
import br.com.sicredi.bank.builder.transaction.TransactionBuilder;
import br.com.sicredi.bank.dto.request.account.AccountRequest;
import br.com.sicredi.bank.dto.request.associate.SaveAssociateRequest;
import br.com.sicredi.bank.dto.request.transaction.DepositTransactionRequest;
import br.com.sicredi.bank.dto.request.transaction.TransferTransactionRequest;
import br.com.sicredi.bank.dto.response.associate.SaveAssociateResponse;
import br.com.sicredi.bank.dto.response.transaction.TransactionResponse;
import br.com.sicredi.bank.service.AssociateService;
import br.com.sicredi.bank.service.TransactionService;
import br.com.sicredi.bank.utils.Utils;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static br.com.sicredi.bank.dto.Constantes.ACCOUNT_FIND_ERROR;
import static br.com.sicredi.bank.dto.Constantes.TRANSACTION_BUSINESS_BALANCE_ERROR;
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
    @Description("Deve transferir entre duas contas com sucesso")
    public void mustTransferBetweenTwoAccountsSuccessfully() {
        SaveAssociateRequest associateRequest = associateBuilder.buildSaveAssociateRequest();

        SaveAssociateResponse associateResponse = associateService
                .saveAssociate(Utils.convertSaveAssociateRequestToJson(associateRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract().as(SaveAssociateResponse.class)
                ;

        AccountRequest accountRequest = accountBuilder.buildAccountRequest();
        accountRequest.setAgency(associateResponse.getAccounts().get(0).getAgency());
        accountRequest.setNumber(associateResponse.getAccounts().get(0).getNumber());
        DepositTransactionRequest depositRequest = transactionBuilder.buildDepositTransactionRequest();
        depositRequest.setCreditAccount(accountRequest);

        TransactionResponse depositResponse = transactionService
                .depositTransaction(Utils.convertDepositTransactionRequestToJson(depositRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract().as(TransactionResponse.class)
                ;

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
                    .extract().as(TransactionResponse.class)
                ;

        assertEquals(transferRequest.getDebitAccount().getAgency(), transferResponse.getAccount().getAgency());
        assertEquals(transferRequest.getDebitAccount().getNumber(), transferResponse.getAccount().getNumber());
        assertEquals(depositResponse.getNewBalance().subtract(transferRequest.getValue()), transferResponse.getNewBalance());

        associateService.deleteAssociate(associateResponse.getId())
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NO_CONTENT)
        ;
    }

    @Test
    @Tag("all")
    @Description("Deve não transferir de uma conta com valor maior que o saldo")
    public void mustNotTransferFromAnAccountWithValueGreaterThanTheBalance() {
        SaveAssociateRequest associateRequest = associateBuilder.buildSaveAssociateRequest();

        SaveAssociateResponse associateResponse = associateService
                .saveAssociate(Utils.convertSaveAssociateRequestToJson(associateRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract().as(SaveAssociateResponse.class)
                ;

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
                    .body(containsString(TRANSACTION_BUSINESS_BALANCE_ERROR))
                ;

        associateService.deleteAssociate(associateResponse.getId())
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NO_CONTENT)
        ;
    }

    @Test
    @Tag("all")
    @Description("Deve não transferir de uma conta com valor negativo")
    public void mustNotTransferFromAnAccountWithNegativeValue() {
        SaveAssociateRequest associateRequest = associateBuilder.buildSaveAssociateRequest();

        SaveAssociateResponse associateResponse = associateService
                .saveAssociate(Utils.convertSaveAssociateRequestToJson(associateRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract().as(SaveAssociateResponse.class)
                ;

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
                    .body(containsString("Valor inválido."))
        ;

        associateService.deleteAssociate(associateResponse.getId())
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NO_CONTENT)
        ;
    }

    @Test
    @Tag("all")
    @Description("Deve não transferir de uma conta inexistente")
    public void mustNotTransferFromANonexistentAccount() {
        SaveAssociateRequest associateRequest = associateBuilder.buildSaveAssociateRequest();

        SaveAssociateResponse associateResponse = associateService
                .saveAssociate(Utils.convertSaveAssociateRequestToJson(associateRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract().as(SaveAssociateResponse.class)
                ;

        AccountRequest creditAccountRequest = accountBuilder.buildAccountRequest();
        creditAccountRequest.setAgency(associateResponse.getAccounts().get(1).getAgency());
        creditAccountRequest.setNumber(associateResponse.getAccounts().get(1).getNumber());
        TransferTransactionRequest transferRequest = transactionBuilder.buildTransferTransactionRequest();
        transferRequest.setCreditAccount(creditAccountRequest);

        transactionService.transferTransaction(Utils.convertTransferTransactionRequestToJson(transferRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .body(containsString(ACCOUNT_FIND_ERROR))
        ;

        associateService.deleteAssociate(associateResponse.getId())
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NO_CONTENT)
        ;
    }

    @Test
    @Tag("all")
    @Description("Deve não transferir para uma conta destino inexistente")
    public void mustNotTransferToANonexistentDestinationAccount() {
        SaveAssociateRequest associateRequest = associateBuilder.buildSaveAssociateRequest();

        SaveAssociateResponse associateResponse = associateService
                .saveAssociate(Utils.convertSaveAssociateRequestToJson(associateRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract().as(SaveAssociateResponse.class)
                ;

        AccountRequest accountRequest = accountBuilder.buildAccountRequest();
        accountRequest.setAgency(associateResponse.getAccounts().get(0).getAgency());
        accountRequest.setNumber(associateResponse.getAccounts().get(0).getNumber());
        TransferTransactionRequest transferRequest = transactionBuilder.buildTransferTransactionRequest();
        transferRequest.setDebitAccount(accountRequest);

        transactionService.transferTransaction(Utils.convertTransferTransactionRequestToJson(transferRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .body(containsString(ACCOUNT_FIND_ERROR))
        ;

        associateService.deleteAssociate(associateResponse.getId())
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NO_CONTENT)
        ;
    }

    @Test
    @Tag("error")
    @Description("Deve não transferir entre duas contas com campos nulos")
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
                    .body(containsString("Valor não pode ser nulo."))
                    .body(containsString("Agência não pode ser nula."))
                    .body(containsString("Número da Conta não pode ser nulo."))
        ;
    }

}
