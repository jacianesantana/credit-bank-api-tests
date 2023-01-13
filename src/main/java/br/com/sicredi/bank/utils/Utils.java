package br.com.sicredi.bank.utils;

import br.com.sicredi.bank.dto.request.associate.SaveAssociateRequest;
import br.com.sicredi.bank.dto.request.associate.UpdateAssociateRequest;
import com.google.gson.Gson;

public class Utils {

    public static String getBaseUrl() {
        String baseUrl = "http://localhost:8080/api/v1/swagger-ui/index.html#/";

        return baseUrl;
    }

    public static String convertSaveAssociateRequestToJson(SaveAssociateRequest saveAssociateRequest) {
        return new Gson().toJson(saveAssociateRequest);
    }

    public static String convertUpdateAssociateRequestToJson(UpdateAssociateRequest updateAssociateRequest) {
        return new Gson().toJson(updateAssociateRequest);
    }

}
