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

    public Response updateAssociateContact(Long id, String updateAssociateContactRequest) {
        return
                given()
                        .pathParam("id", id)
                        .contentType(ContentType.JSON)
                        .body(updateAssociateContactRequest)
                .when()
                        .patch(Utils.getBaseUrl() + "/associate/updateContact/{id}")
                ;
    }

    public Response updateAssociatePaycheck(Long id, String updateAssociatePaycheckRequest) {
        return
                given()
                        .pathParam("id", id)
                        .contentType(ContentType.JSON)
                        .body(updateAssociatePaycheckRequest)
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

    public Response saveAssociateAddress(Long idAssociate, String addressRequest) {
        return
                given()
                        .queryParam("idAssociate", idAssociate)
                        .contentType(ContentType.JSON)
                        .body(addressRequest)
                .when()
                        .post(Utils.getBaseUrl() + "/associate/address/save")
                ;
    }

    public Response updateAssociateAddress(Long id, Long idAssociate, String addressRequest) {
        return
                given()
                        .pathParam("id", id)
                        .queryParam("idAssociate", idAssociate)
                        .contentType(ContentType.JSON)
                        .body(addressRequest)
                .when()
                        .put(Utils.getBaseUrl() + "/associate/address/update/{id}")
                ;
    }

}
