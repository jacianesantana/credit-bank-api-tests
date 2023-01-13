package br.com.sicredi.bank.service;

import br.com.sicredi.bank.utils.Utils;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class AssociateService {

    public Response saveAssociate(String saveAssociateRequest) {
        return
                given()
                        .contentType(ContentType.JSON)
                        .body(saveAssociateRequest)
                .when()
                        .post(Utils.getBaseUrl() + "/associate/save")
                ;
    }

    public Response findAssociate(Long id) {
        return
                given()
                        .pathParam("id", id)
                .when()
                        .get(Utils.getBaseUrl() + "/associate/find/{id}")
                ;
    }

    public Response udateAssociate(Long id, String updateAssociateRequest) {
        return
                given()
                        .pathParam("id", id)
                        .contentType(ContentType.JSON)
                        .body(updateAssociateRequest)
                .when()
                        .patch(Utils.getBaseUrl() + "/associate/updatePaycheck/{id}")
                ;
    }

    public Response deleteAssociate(Long id) {
        return
                given()
                        .pathParam("id", id)
                .when()
                        .delete(Utils.getBaseUrl() + "/associate/delete/{id}")
                ;
    }

}
