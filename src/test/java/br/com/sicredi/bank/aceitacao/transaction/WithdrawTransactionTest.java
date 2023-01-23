package br.com.sicredi.bank.aceitacao.transaction;

import br.com.sicredi.bank.builder.account.AccountBuilder;
import br.com.sicredi.bank.builder.associate.AssociateBuilder;
import br.com.sicredi.bank.builder.transaction.TransactionBuilder;
import br.com.sicredi.bank.dto.request.account.AccountRequest;
import br.com.sicredi.bank.dto.request.associate.SaveAssociateRequest;
import br.com.sicredi.bank.dto.request.transaction.DepositTransactionRequest;
import br.com.sicredi.bank.dto.request.transaction.WithdrawTransactionRequest;
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
@Epic("Withdraw Transaction")
public class WithdrawTransactionTest {

    AssociateService associateService = new AssociateService();
    AssociateBuilder associateBuilder = new AssociateBuilder();
    TransactionService transactionService = new TransactionService();
    TransactionBuilder transactionBuilder = new TransactionBuilder();
    AccountBuilder accountBuilder = new AccountBuilder();

    @Test
    @Tag("all")
    @Description("Deve sacar de uma conta com sucesso")
    public void mustWithdrawFromAnAccountSuccessfully() {
        SaveAssociateRequest associateRequest = associateBuilder.buildSaveAssociateRequest();

        SaveAssociateResponse associateResponse = associateService
                .saveAssociate(Utils.convertSaveAssociateRequestToJson(associateRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract().as(SaveAssociateResponse.class)
                ;

        DepositTransactionRequest depositRequest = transactionBuilder.buildDepositTransactionRequest();
        AccountRequest accountRequest = accountBuilder.buildAccountRequest();
        accountRequest.setAgency(associateResponse.getAccounts().get(0).getAgency());
        accountRequest.setNumber(associateResponse.getAccounts().get(0).getNumber());
        depositRequest.setCreditAccount(accountRequest);

        TransactionResponse depositResponse = transactionService
                .depositTransaction(Utils.convertDepositTransactionRequestToJson(depositRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract().as(TransactionResponse.class)
                ;

        WithdrawTransactionRequest withdrawRequest = transactionBuilder.buildWithdrawTransactionRequest();
        withdrawRequest.setDebitAccount(accountRequest);

        TransactionResponse withdrawResponse = transactionService
                .withdrawTransaction(Utils.convertWithdrawTransactionRequestToJson(withdrawRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract().as(TransactionResponse.class)
                ;

        assertEquals(withdrawRequest.getDebitAccount().getAgency(), withdrawResponse.getAccount().getAgency());
        assertEquals(withdrawRequest.getDebitAccount().getNumber(), withdrawResponse.getAccount().getNumber());
        assertEquals(depositResponse.getNewBalance().subtract(withdrawRequest.getValue()), withdrawResponse.getNewBalance());

        associateService.deleteAssociate(associateResponse.getId())
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NO_CONTENT)
        ;
    }

    @Test
    @Tag("all")
    @Description("Deve não sacar de uma conta com valor maior que o saldo")
    public void mustNotWithdrawFromAnAccountWithValueGreaterThanTheBalance() {
        SaveAssociateRequest associateRequest = associateBuilder.buildSaveAssociateRequest();

        SaveAssociateResponse associateResponse = associateService
                .saveAssociate(Utils.convertSaveAssociateRequestToJson(associateRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract().as(SaveAssociateResponse.class)
                ;

        WithdrawTransactionRequest withdrawRequest = transactionBuilder.buildWithdrawTransactionRequest();
        AccountRequest accountRequest = accountBuilder.buildAccountRequest();
        accountRequest.setAgency(associateResponse.getAccounts().get(0).getAgency());
        accountRequest.setNumber(associateResponse.getAccounts().get(0).getNumber());
        withdrawRequest.setDebitAccount(accountRequest);

        transactionService.withdrawTransaction(Utils.convertWithdrawTransactionRequestToJson(withdrawRequest))
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
    @Description("Deve não sacar de uma conta com valor negativo")
    public void mustNotWithdrawFromAnAccountWithNegativeValue() {
        SaveAssociateRequest associateRequest = associateBuilder.buildSaveAssociateRequest();

        SaveAssociateResponse associateResponse = associateService
                .saveAssociate(Utils.convertSaveAssociateRequestToJson(associateRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract().as(SaveAssociateResponse.class)
                ;

        AccountRequest accountRequest = accountBuilder.buildAccountRequest();
        WithdrawTransactionRequest withdrawRequest = transactionBuilder.buildWithdrawWithInvalidValue();
        accountRequest.setAgency(associateResponse.getAccounts().get(0).getAgency());
        accountRequest.setNumber(associateResponse.getAccounts().get(0).getNumber());
        withdrawRequest.setDebitAccount(accountRequest);

        transactionService.withdrawTransaction(Utils.convertWithdrawTransactionRequestToJson(withdrawRequest))
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
    @Description("Deve não sacar de uma conta com agência inexistente")
    public void mustNotWithdrawFromAnAccountWithInvalidAgency() {
        SaveAssociateRequest associateRequest = associateBuilder.buildSaveAssociateRequest();

        SaveAssociateResponse associateResponse = associateService
                .saveAssociate(Utils.convertSaveAssociateRequestToJson(associateRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract().as(SaveAssociateResponse.class)
                ;

        AccountRequest accountRequest = accountBuilder.buildAccountRequest();
        WithdrawTransactionRequest withdrawRequest = transactionBuilder.buildWithdrawTransactionRequest();
        accountRequest.setAgency(1);
        accountRequest.setNumber(associateResponse.getAccounts().get(0).getNumber());
        withdrawRequest.setDebitAccount(accountRequest);

        transactionService.withdrawTransaction(Utils.convertWithdrawTransactionRequestToJson(withdrawRequest))
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
    @Description("Deve não sacar de uma conta com número inexistente")
    public void mustNotWithdrawFromAnAccountWithInvalidNumber() {
        SaveAssociateRequest associateRequest = associateBuilder.buildSaveAssociateRequest();

        SaveAssociateResponse associateResponse = associateService
                .saveAssociate(Utils.convertSaveAssociateRequestToJson(associateRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract().as(SaveAssociateResponse.class)
                ;

        AccountRequest accountRequest = accountBuilder.buildAccountRequest();
        WithdrawTransactionRequest withdrawRequest = transactionBuilder.buildWithdrawTransactionRequest();
        accountRequest.setAgency(associateResponse.getAccounts().get(0).getAgency());
        accountRequest.setNumber(1);
        withdrawRequest.setDebitAccount(accountRequest);

        transactionService.withdrawTransaction(Utils.convertWithdrawTransactionRequestToJson(withdrawRequest))
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
    @Description("Deve não sacar de uma conta com campos nulos")
    public void mustNotWithdrawFromAnAccountWithNullFields() {
        AccountRequest accountRequest = accountBuilder.buildAccountWithNullFields();
        WithdrawTransactionRequest withdrawRequest = transactionBuilder.buildWithdrawWithNullFields();
        withdrawRequest.setDebitAccount(accountRequest);

        transactionService.withdrawTransaction(Utils.convertWithdrawTransactionRequestToJson(withdrawRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body(containsString("Valor não pode ser nulo."))
                    .body(containsString("Agência não pode ser nula."))
                    .body(containsString("Número da Conta não pode ser nulo."))
        ;
    }

}