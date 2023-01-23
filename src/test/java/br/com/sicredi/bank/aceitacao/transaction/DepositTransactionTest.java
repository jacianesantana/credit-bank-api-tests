package br.com.sicredi.bank.aceitacao.transaction;

import br.com.sicredi.bank.builder.account.AccountBuilder;
import br.com.sicredi.bank.builder.associate.AssociateBuilder;
import br.com.sicredi.bank.builder.transaction.TransactionBuilder;
import br.com.sicredi.bank.dto.request.account.AccountRequest;
import br.com.sicredi.bank.dto.request.associate.SaveAssociateRequest;
import br.com.sicredi.bank.dto.request.transaction.DepositTransactionRequest;
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
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Transaction")
@Epic("Deposit Transaction")
public class DepositTransactionTest {

    AssociateService associateService = new AssociateService();
    AssociateBuilder associateBuilder = new AssociateBuilder();
    TransactionService transactionService = new TransactionService();
    TransactionBuilder transactionBuilder = new TransactionBuilder();
    AccountBuilder accountBuilder = new AccountBuilder();

    @Test
    @Tag("all")
    @Description("Deve depositar em uma conta com sucesso")
    public void mustDepositToAnAccountSuccessfully() {
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

        TransactionResponse response = transactionService
                .depositTransaction(Utils.convertDepositTransactionRequestToJson(depositRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract().as(TransactionResponse.class)
                ;

        assertEquals(depositRequest.getCreditAccount().getAgency(), response.getAccount().getAgency());
        assertEquals(depositRequest.getCreditAccount().getNumber(), response.getAccount().getNumber());
        assertEquals(depositRequest.getValue(), response.getNewBalance());

        associateService.deleteAssociate(associateResponse.getId())
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NO_CONTENT)
        ;
    }

    @Test
    @Tag("all")
    @Description("Deve não depositar na conta com valor negativo")
    public void mustNotDepositToAnAccountWithNegativeValue() {
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
        DepositTransactionRequest depositRequest = transactionBuilder.buildDepositWithInvalidValue();
        depositRequest.setCreditAccount(accountRequest);

        transactionService.depositTransaction(Utils.convertDepositTransactionRequestToJson(depositRequest))
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
    @Description("Deve não depositar na conta com agência inexistente")
    public void mustNotDepositToAnAccountWithInvalidAgency() {
        SaveAssociateRequest associateRequest = associateBuilder.buildSaveAssociateRequest();

        SaveAssociateResponse associateResponse = associateService
                .saveAssociate(Utils.convertSaveAssociateRequestToJson(associateRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract().as(SaveAssociateResponse.class)
                ;

        AccountRequest accountRequest = accountBuilder.buildAccountRequest();
        accountRequest.setAgency(1);
        accountRequest.setNumber(associateResponse.getAccounts().get(0).getNumber());
        DepositTransactionRequest depositRequest = transactionBuilder.buildDepositTransactionRequest();
        depositRequest.setCreditAccount(accountRequest);

        transactionService.depositTransaction(Utils.convertDepositTransactionRequestToJson(depositRequest))
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
    @Description("Deve não depositar na conta com número inexistente")
    public void mustNotDepositToAnAccountWithInvalidNumber() {
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
        accountRequest.setNumber(1);
        DepositTransactionRequest depositRequest = transactionBuilder.buildDepositTransactionRequest();
        depositRequest.setCreditAccount(accountRequest);

        transactionService.depositTransaction(Utils.convertDepositTransactionRequestToJson(depositRequest))
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
    @Description("Deve não depositar na conta com campos nulos")
    public void mustNotDepositToAnAccountWithNullFields() {
        AccountRequest accountRequest = accountBuilder.buildAccountWithNullFields();
        DepositTransactionRequest depositRequest = transactionBuilder.buildDepositWithNullFields();
        depositRequest.setCreditAccount(accountRequest);

        transactionService.depositTransaction(Utils.convertDepositTransactionRequestToJson(depositRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body(containsString("Valor não pode ser nulo."))
                    .body(containsString("Agência não pode ser nula."))
                    .body(containsString("Número da Conta não pode ser nulo."))
        ;
    }

}
