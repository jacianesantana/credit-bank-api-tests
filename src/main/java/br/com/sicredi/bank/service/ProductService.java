package br.com.sicredi.bank.service;

import br.com.sicredi.bank.utils.Utils;
import io.restassured.response.Response;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;

public class ProductService {

    public Response listProducts(BigDecimal salary) {
        return
                given()
                        .queryParam("salary", salary)
                .when()
                        .get(Utils.getBaseUrl() + "/product/products")
                ;
    }

}
