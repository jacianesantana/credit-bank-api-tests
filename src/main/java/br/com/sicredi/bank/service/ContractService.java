package br.com.sicredi.bank.service;

import br.com.sicredi.bank.utils.Utils;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class ContractService {

    public Response signContract(String contractRequest) {
        return
                given()
                        .contentType(ContentType.JSON)
                        .body(contractRequest)
                .when()
                        .post(Utils.getBaseUrl() + "/contract/sign")
                ;
    }

    public Response findContract(Long id) {
        return
                given()
                        .pathParam("id", id)
                .when()
                        .get(Utils.getBaseUrl() + "/contract/find/{id}")
                ;
    }

}
