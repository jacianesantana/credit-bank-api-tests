package br.com.sicredi.bank.acceptance.base;

import br.com.sicredi.bank.utils.Utils;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public class BaseTest {

    @BeforeAll
    public static void setUp(){
        RestAssured.baseURI = Utils.getBaseUrl();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

}
