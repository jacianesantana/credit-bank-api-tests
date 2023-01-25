package br.com.sicredi.bank.utils;

import br.com.sicredi.bank.model.request.address.AddressRequest;
import br.com.sicredi.bank.model.request.associate.SaveAssociateRequest;
import br.com.sicredi.bank.model.request.associate.UpdateAssociateContactRequest;
import br.com.sicredi.bank.model.request.associate.UpdateAssociatePaycheckRequest;
import br.com.sicredi.bank.model.request.contract.ContractRequest;
import br.com.sicredi.bank.model.request.transaction.DepositTransactionRequest;
import br.com.sicredi.bank.model.request.transaction.TransferTransactionRequest;
import br.com.sicredi.bank.model.request.transaction.WithdrawTransactionRequest;
import com.google.gson.Gson;
import net.datafaker.Faker;

import java.util.Locale;

public class Utils {

    public static String getBaseUrl() {
        String baseUrl = "http://localhost:8080/bank-api/v1";

        return baseUrl;
    }

    public static String convertSaveAssociateRequestToJson(SaveAssociateRequest saveAssociateRequest) {
        return new Gson().toJson(saveAssociateRequest);
    }

    public static String convertUpdateAssociatePaycheckRequestToJson(UpdateAssociatePaycheckRequest updateAssociatePaycheckRequest) {
        return new Gson().toJson(updateAssociatePaycheckRequest);
    }

    public static String convertUpdateAssociateContactRequestToJson(UpdateAssociateContactRequest updateAssociateContactRequest) {
        return new Gson().toJson(updateAssociateContactRequest);
    }

    public static String convertAddressRequestToJson(AddressRequest addressRequest) {
        return new Gson().toJson(addressRequest);
    }

    public static String convertContractRequestToJson(ContractRequest contractRequest) {
        return new Gson().toJson(contractRequest);
    }

    public static String convertDepositTransactionRequestToJson(DepositTransactionRequest depositTransactionRequest) {
        return new Gson().toJson(depositTransactionRequest);
    }

    public static String convertWithdrawTransactionRequestToJson(WithdrawTransactionRequest withdrawTransactionRequest) {
        return new Gson().toJson(withdrawTransactionRequest);
    }

    public static String convertTransferTransactionRequestToJson(TransferTransactionRequest transferTransactionRequest) {
        return new Gson().toJson(transferTransactionRequest);
    }

}
