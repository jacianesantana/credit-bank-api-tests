package br.com.sicredi.bank.utils;

import br.com.sicredi.bank.dto.request.address.AddressRequest;
import br.com.sicredi.bank.dto.request.associate.SaveAssociateRequest;
import br.com.sicredi.bank.dto.request.associate.UpdateAssociateContactRequest;
import br.com.sicredi.bank.dto.request.associate.UpdateAssociatePaycheckRequest;
import com.google.gson.Gson;

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

}
