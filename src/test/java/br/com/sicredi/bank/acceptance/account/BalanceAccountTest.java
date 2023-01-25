package br.com.sicredi.bank.acceptance.account;

import br.com.sicredi.bank.factory.associate.AssociateBuilder;
import br.com.sicredi.bank.model.request.associate.SaveAssociateRequest;
import br.com.sicredi.bank.model.response.account.BalanceAccountResponse;
import br.com.sicredi.bank.model.response.associate.SaveAssociateResponse;
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

import static br.com.sicredi.bank.utils.Message.ACCOUNT_FIND_ERROR;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Account")
@Epic("Balance Account")
public class BalanceAccountTest {

    AssociateService associateService = new AssociateService();
    AssociateBuilder associateBuilder = new AssociateBuilder();
    AccountService accountService = new AccountService();

    @Test
    @Tag("all")
    @Description("Must find account balance successfully")
    public void mustFindAccountBalanceSuccessfully() {
        SaveAssociateRequest saveRequest = associateBuilder.buildSaveAssociateRequest();

        SaveAssociateResponse saveResponse = associateService
                .saveAssociate(Utils.convertSaveAssociateRequestToJson(saveRequest))
                .then().extract().as(SaveAssociateResponse.class);

        BalanceAccountResponse response = accountService.balanceAccount(saveResponse.getAccounts().get(0).getId())
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_OK)
                    .extract().as(BalanceAccountResponse.class);

        assertEquals(BigDecimal.valueOf(0).setScale(2), response.getBalance());

        associateService.deleteAssociate(saveResponse.getId());
    }

    @Test
    @Tag("all")
    @Description("Must not find account balance with nonexistent id")
    public void mustNotFindAccountBalanceWithNonexistentId() {
        var invalidId = 9999999999999999L;

        accountService.balanceAccount(invalidId)
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .body(containsString(ACCOUNT_FIND_ERROR));
    }

}
