package br.com.sicredi.bank.aceitacao.account;

import br.com.sicredi.bank.builder.associate.AssociateBuilder;
import br.com.sicredi.bank.dto.request.associate.SaveAssociateRequest;
import br.com.sicredi.bank.dto.response.account.BalanceAccountResponse;
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
@Epic("Balance Account")
public class BalanceAccountTest {

    AccountService accountService = new AccountService();
    AssociateService associateService = new AssociateService();
    AssociateBuilder associateBuilder = new AssociateBuilder();

    @Test
    @Tag("all")
    @Description("Deve buscar saldo de conta com sucesso")
    public void mustFindAccountBalanceSuccessfully() {
        SaveAssociateRequest saveRequest = associateBuilder.buildSaveAssociateRequest();

        SaveAssociateResponse saveResponse = associateService.saveAssociate(Utils.convertSaveAssociateRequestToJson(saveRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract().as(SaveAssociateResponse.class)
                ;

        BalanceAccountResponse response = accountService.balance(saveResponse.getAccounts().get(0).getId())
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_OK)
                    .extract().as(BalanceAccountResponse.class)
                ;

        assertEquals(BigDecimal.valueOf(0).setScale(2), response.getBalance());

        associateService.deleteAssociate(saveResponse.getId())
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NO_CONTENT)
        ;
    }

    @Test
    @Tag("all")
    @Description("Deve não buscar saldo de conta com id inexistente")
    public void mustNotFindAccountBalanceWithNonexistentId() {
        var invalidId = 16261837107330L;

        accountService.balance(invalidId)
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .body(containsString(ACCOUNT_ERROR))
                ;
    }

}
