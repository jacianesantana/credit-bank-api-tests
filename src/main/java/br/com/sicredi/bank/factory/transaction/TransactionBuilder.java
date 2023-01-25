package br.com.sicredi.bank.factory.transaction;

import br.com.sicredi.bank.model.request.account.AccountRequest;
import br.com.sicredi.bank.model.request.transaction.DepositTransactionRequest;
import br.com.sicredi.bank.model.request.transaction.TransferTransactionRequest;
import br.com.sicredi.bank.model.request.transaction.WithdrawTransactionRequest;

import java.math.BigDecimal;

public class TransactionBuilder {

    public DepositTransactionRequest buildDepositTransactionRequest() {
        return DepositTransactionRequest.builder()
                .creditAccount(new AccountRequest())
                .value(BigDecimal.valueOf(500).setScale(2))
                .build();
    }

    public DepositTransactionRequest buildDepositWithInvalidValue() {
        DepositTransactionRequest invalidDeposit = buildDepositTransactionRequest();
        invalidDeposit.setValue(BigDecimal.valueOf(-1));

        return invalidDeposit;
    }

    public DepositTransactionRequest buildDepositWithNullFields() {
        DepositTransactionRequest invalidDeposit = buildDepositTransactionRequest();
        invalidDeposit.setValue(null);

        return invalidDeposit;
    }

    public WithdrawTransactionRequest buildWithdrawTransactionRequest() {
        return WithdrawTransactionRequest.builder()
                .debitAccount(new AccountRequest())
                .value(BigDecimal.valueOf(200).setScale(2))
                .build();
    }

    public WithdrawTransactionRequest buildWithdrawWithInvalidValue() {
        WithdrawTransactionRequest invalidWithdraw = buildWithdrawTransactionRequest();
        invalidWithdraw.setValue(BigDecimal.valueOf(-1));

        return invalidWithdraw;
    }

    public WithdrawTransactionRequest buildWithdrawWithNullFields() {
        WithdrawTransactionRequest invalidWithdraw = buildWithdrawTransactionRequest();
        invalidWithdraw.setValue(null);

        return invalidWithdraw;
    }

    public TransferTransactionRequest buildTransferTransactionRequest() {
        return TransferTransactionRequest.builder()
                .debitAccount(new AccountRequest())
                .creditAccount(new AccountRequest())
                .value(BigDecimal.valueOf(100).setScale(2))
                .build();
    }

    public TransferTransactionRequest buildTransferWithInvalidValue() {
        TransferTransactionRequest invalidTransfer = buildTransferTransactionRequest();
        invalidTransfer.setValue(BigDecimal.valueOf(-1));

        return invalidTransfer;
    }

    public TransferTransactionRequest buildTransferWithNullFields() {
        TransferTransactionRequest invalidTransfer = buildTransferTransactionRequest();
        invalidTransfer.setValue(null);

        return invalidTransfer;
    }

}
