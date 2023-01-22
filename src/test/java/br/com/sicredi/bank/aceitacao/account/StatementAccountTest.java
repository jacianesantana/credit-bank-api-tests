package br.com.sicredi.bank.aceitacao.account;

import br.com.sicredi.bank.builder.associate.AssociateBuilder;
import br.com.sicredi.bank.dto.request.associate.SaveAssociateRequest;
import br.com.sicredi.bank.dto.response.account.StatementAccountResponse;
import br.com.sicredi.bank.dto.response.associate.SaveAssociateResponse;
import br.com.sicredi.bank.service.AccountService;
import br.com.sicredi.bank.service.AssociateService;
import br.com.sicredi.bank.utils.Utils;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static br.com.sicredi.bank.dto.Constantes.ACCOUNT_ERROR;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Account")
@Epic("Statement Account")
public class StatementAccountTest {

    AccountService accountService = new AccountService();
    AssociateService associateService = new AssociateService();
    AssociateBuilder associateBuilder = new AssociateBuilder();

    @Test
    @Tag("error")
    @Description("Deve buscar extrato de conta com sucesso")
    public void mustFindAccountStatementSuccessfully() {
        // FALTA ADD TRANSAÇÕES

        SaveAssociateRequest saveRequest = associateBuilder.buildSaveAssociateRequest();

        SaveAssociateResponse saveResponse = associateService.saveAssociate(Utils.convertSaveAssociateRequestToJson(saveRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract().as(SaveAssociateResponse.class)
                ;

        StatementAccountResponse response = accountService.statement(saveResponse.getAccounts().get(0).getId())
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_OK)
                    .extract().as(StatementAccountResponse.class)
                ;

        assertEquals(saveResponse.getAccounts().get(0).getType(), response.getType());
        assertEquals(saveResponse.getAccounts().get(0).getAgency(), response.getAgency());
        assertEquals(saveResponse.getAccounts().get(0).getNumber(), response.getNumber());
        assertEquals(BigDecimal.valueOf(0).setScale(2), response.getBalance());
        assertEquals(0, response.getTransactions().size());

        associateService.deleteAssociate(saveResponse.getId())
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NO_CONTENT)
        ;
    }

    @Test
    @Tag("all")
    @Description("Deve buscar extrato de conta sem transações com sucesso")
    public void mustFindEmptyAccountStatementSuccessfully() {
        SaveAssociateRequest saveRequest = associateBuilder.buildSaveAssociateRequest();

        SaveAssociateResponse saveResponse = associateService.saveAssociate(Utils.convertSaveAssociateRequestToJson(saveRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract().as(SaveAssociateResponse.class)
                ;

        StatementAccountResponse response = accountService.statement(saveResponse.getAccounts().get(0).getId())
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_OK)
                    .extract().as(StatementAccountResponse.class)
                ;

        assertEquals(0, response.getTransactions().size());
        assertEquals(saveResponse.getAccounts().get(0).getType(), response.getType());
        assertEquals(saveResponse.getAccounts().get(0).getAgency(), response.getAgency());
        assertEquals(saveResponse.getAccounts().get(0).getNumber(), response.getNumber());
        assertEquals(BigDecimal.valueOf(0).setScale(2), response.getBalance());

        associateService.deleteAssociate(saveResponse.getId())
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NO_CONTENT)
        ;
    }

    @Test
    @Tag("all")
    @Description("Deve não buscar extrato de conta com id inexistente")
    public void mustNotFindAccountStatementWithNonexistentId() {
        var invalidId = 16261837107330L;

        accountService.statement(invalidId)
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .body(containsString(ACCOUNT_ERROR))
                ;
    }

}
