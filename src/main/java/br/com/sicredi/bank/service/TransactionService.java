package br.com.sicredi.bank.service;

import br.com.sicredi.bank.utils.Utils;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class TransactionService {

    public Response depositTransaction(String depositTransactionRequest) {
        return
                given()
                        .contentType(ContentType.JSON)
                        .body(depositTransactionRequest)
                .when()
                        .post(Utils.getBaseUrl() + "/transaction/deposit")
                ;
    }

    public Response withdrawTransaction(String withdrawTransactionRequest) {
        return
                given()
                        .contentType(ContentType.JSON)
                        .body(withdrawTransactionRequest)
                .when()
                        .post(Utils.getBaseUrl() + "/transaction/withdraw")
                ;
    }

    public Response transferTransaction(String transferTransactionRequest) {
        return
                given()
                        .contentType(ContentType.JSON)
                        .body(transferTransactionRequest)
                .when()
                        .post(Utils.getBaseUrl() + "/transaction/transfer")
                ;
    }

}
