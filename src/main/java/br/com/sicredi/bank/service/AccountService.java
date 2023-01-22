package br.com.sicredi.bank.service;

import br.com.sicredi.bank.utils.Utils;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class AccountService {

    public Response balance(Long id) {
        return
                given()
                        .pathParam("id", id)
                .when()
                        .get(Utils.getBaseUrl() + "/account/balance/{id}")
                ;
    }

    public Response statement(Long id) {
        return
                given()
                        .pathParam("id", id)
                .when()
                        .get(Utils.getBaseUrl() + "/account/statement/{id}")
                ;
    }

}
