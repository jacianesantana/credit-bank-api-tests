package br.com.sicredi.bank.acceptance.account;

import br.com.sicredi.bank.builder.account.AccountBuilder;
import br.com.sicredi.bank.builder.associate.AssociateBuilder;
import br.com.sicredi.bank.builder.transaction.TransactionBuilder;
import br.com.sicredi.bank.dto.enums.TransactionType;
import br.com.sicredi.bank.dto.request.account.AccountRequest;
import br.com.sicredi.bank.dto.request.associate.SaveAssociateRequest;
import br.com.sicredi.bank.dto.request.transaction.DepositTransactionRequest;
import br.com.sicredi.bank.dto.request.transaction.TransferTransactionRequest;
import br.com.sicredi.bank.dto.request.transaction.WithdrawTransactionRequest;
import br.com.sicredi.bank.dto.response.account.StatementAccountResponse;
import br.com.sicredi.bank.dto.response.associate.SaveAssociateResponse;
import br.com.sicredi.bank.service.AccountService;
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

import static br.com.sicredi.bank.utils.Message.ACCOUNT_FIND_ERROR;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Account")
@Epic("Statement Account")
public class StatementAccountTest {

    AssociateService associateService = new AssociateService();
    AssociateBuilder associateBuilder = new AssociateBuilder();
    AccountService accountService = new AccountService();
    AccountBuilder accountBuilder = new AccountBuilder();
    TransactionService transactionService = new TransactionService();
    TransactionBuilder transactionBuilder = new TransactionBuilder();

    @Test
    @Tag("all")
    @Description("Deve buscar extrato de conta com sucesso")
    public void mustFindAccountStatementSuccessfully() {
        SaveAssociateRequest saveRequest = associateBuilder.buildSaveAssociateRequest();

        SaveAssociateResponse associateResponse = associateService.saveAssociate(Utils.convertSaveAssociateRequestToJson(saveRequest))
                .then().extract().as(SaveAssociateResponse.class);

        AccountRequest accountRequest = accountBuilder.buildAccountRequest();
        accountRequest.setAgency(associateResponse.getAccounts().get(0).getAgency());
        accountRequest.setNumber(associateResponse.getAccounts().get(0).getNumber());

        AccountRequest creditAccountRequest = accountBuilder.buildAccountRequest();
        creditAccountRequest.setAgency(associateResponse.getAccounts().get(1).getAgency());
        creditAccountRequest.setNumber(associateResponse.getAccounts().get(1).getNumber());

        DepositTransactionRequest depositRequest = transactionBuilder.buildDepositTransactionRequest();
        depositRequest.setCreditAccount(accountRequest);

        transactionService.depositTransaction(Utils.convertDepositTransactionRequestToJson(depositRequest));

        WithdrawTransactionRequest withdrawRequest = transactionBuilder.buildWithdrawTransactionRequest();
        withdrawRequest.setDebitAccount(accountRequest);

        transactionService.withdrawTransaction(Utils.convertWithdrawTransactionRequestToJson(withdrawRequest));

        TransferTransactionRequest transferRequest = transactionBuilder.buildTransferTransactionRequest();
        transferRequest.setDebitAccount(accountRequest);
        transferRequest.setCreditAccount(creditAccountRequest);

        transactionService.transferTransaction(Utils.convertTransferTransactionRequestToJson(transferRequest));

        StatementAccountResponse response = accountService.statementAccount(associateResponse.getAccounts().get(0).getId())
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_OK)
                    .extract().as(StatementAccountResponse.class);

        var balance = BigDecimal.ZERO.add(depositRequest.getValue())
                .subtract(withdrawRequest.getValue())
                .subtract(transferRequest.getValue());

        assertEquals(3, response.getTransactions().size());
        assertEquals(balance, response.getBalance());
        assertEquals(TransactionType.DEPOSIT, response.getTransactions().get(0).getType());
        assertEquals(TransactionType.WITHDRAW, response.getTransactions().get(1).getType());
        assertEquals(TransactionType.TRANSFER, response.getTransactions().get(2).getType());
        assertEquals(accountRequest.getAgency(), response.getAgency());
        assertEquals(accountRequest.getNumber(), response.getNumber());

        associateService.deleteAssociate(associateResponse.getId());
    }

    @Test
    @Tag("all")
    @Description("Deve buscar extrato de conta sem transações com sucesso")
    public void mustFindEmptyAccountStatementSuccessfully() {
        SaveAssociateRequest saveRequest = associateBuilder.buildSaveAssociateRequest();

        SaveAssociateResponse saveResponse = associateService.saveAssociate(Utils.convertSaveAssociateRequestToJson(saveRequest))
                .then().extract().as(SaveAssociateResponse.class);

        StatementAccountResponse response = accountService.statementAccount(saveResponse.getAccounts().get(0).getId())
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_OK)
                    .extract().as(StatementAccountResponse.class);

        assertEquals(0, response.getTransactions().size());
        assertEquals(saveResponse.getAccounts().get(0).getType(), response.getType());
        assertEquals(saveResponse.getAccounts().get(0).getAgency(), response.getAgency());
        assertEquals(saveResponse.getAccounts().get(0).getNumber(), response.getNumber());
        assertEquals(BigDecimal.valueOf(0).setScale(2), response.getBalance());

        associateService.deleteAssociate(saveResponse.getId());
    }

    @Test
    @Tag("all")
    @Description("Deve não buscar extrato de conta com id inexistente")
    public void mustNotFindAccountStatementWithNonexistentId() {
        var invalidId = 9999999999999999L;

        accountService.statementAccount(invalidId)
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .body(containsString(ACCOUNT_FIND_ERROR));
    }

}
