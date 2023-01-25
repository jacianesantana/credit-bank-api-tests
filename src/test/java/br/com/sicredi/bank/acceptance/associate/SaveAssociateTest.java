package br.com.sicredi.bank.acceptance.associate;

import br.com.sicredi.bank.factory.associate.AssociateBuilder;
import br.com.sicredi.bank.model.request.associate.SaveAssociateRequest;
import br.com.sicredi.bank.model.response.associate.SaveAssociateResponse;
import br.com.sicredi.bank.service.AssociateService;
import br.com.sicredi.bank.utils.Utils;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static br.com.sicredi.bank.utils.Message.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Associate")
@Epic("Save Associate")
public class SaveAssociateTest {

    AssociateService associateService = new AssociateService();
    AssociateBuilder associateBuilder = new AssociateBuilder();

    @Test
    @Tag("all")
    @Description("Must save associate successfully")
    public void mustSaveAssociateSuccessfully() {
        SaveAssociateRequest request = associateBuilder.buildSaveAssociateRequest();

        SaveAssociateResponse response = associateService.saveAssociate(Utils.convertSaveAssociateRequestToJson(request))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract().as(SaveAssociateResponse.class)
                ;

        assertNotNull(response.getId());
        assertEquals(request.getName(), response.getName());
        assertEquals(request.getCpf(), response.getCpf());
        assertEquals(request.getBirthDate(), response.getBirthDate());
        assertEquals(request.getPhone(), response.getPhone());
        assertEquals(request.getEmail(), response.getEmail());
        assertEquals(request.getProfession(), response.getProfession());
        assertEquals(request.getSalary(), response.getSalary());
        assertEquals(LocalDate.now().toString(), response.getLastPaycheck());
        assertNotNull(response.getAccounts());

        associateService.deleteAssociate(response.getId());
    }

    @Test
    @Tag("all")
    @Description("Must not save associate with registered CPF")
    public void mustNotSaveAssociateWithRegisteredCpf() {
        SaveAssociateRequest request = associateBuilder.buildSaveAssociateRequest();

        SaveAssociateResponse response = associateService.saveAssociate(Utils.convertSaveAssociateRequestToJson(request))
                .then().extract().as(SaveAssociateResponse.class);

        associateService.saveAssociate(Utils.convertSaveAssociateRequestToJson(request))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body(containsString(ASSOCIATE_BUSINESS_CPF_ERROR));

        associateService.deleteAssociate(response.getId());
    }

    @Test
    @Tag("all")
    @Description("Must not save underage associate")
    public void mustNotSaveUnderageAssociate() {
        SaveAssociateRequest invalidRequest = associateBuilder.buildSaveAssociateWithInvalidBirthDate();

        associateService.saveAssociate(Utils.convertSaveAssociateRequestToJson(invalidRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body(containsString(ASSOCIATE_BUSINESS_AGE_ERROR));
    }

    @Test
    @Tag("all")
    @Description("Must not save associate with negative salary")
    public void mustNotSaveAssociateWithNegativeSalary() {
        SaveAssociateRequest invalidRequest = associateBuilder.buildSaveAssociateWithInvalidSalary();

        associateService.saveAssociate(Utils.convertSaveAssociateRequestToJson(invalidRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body(containsString(ASSOCIATE_SALARY_INVALID));
    }

    @Test
    @Tag("all")
    @Description("Must not save associate with invalid CPF")
    public void mustNotSaveAssociateWithInvalidCpf() {
        SaveAssociateRequest invalidRequest = associateBuilder.buildSaveAssociateWithInvalidCpf();

        associateService.saveAssociate(Utils.convertSaveAssociateRequestToJson(invalidRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body(containsString(ASSOCIATE_CPF_INVALID));
    }

    @Test
    @Tag("all")
    @Description("Must not save associate with invalid email")
    public void mustNotSaveAssociateWithInvalidEmail() {
        SaveAssociateRequest invalidRequest = associateBuilder.buildSaveAssociateWithInvalidEmail();

        associateService.saveAssociate(Utils.convertSaveAssociateRequestToJson(invalidRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body(containsString(ASSOCIATE_EMAIL_INVALID));
    }

    @Test
    @Tag("all")
    @Description("Must not save associate with empty fields")
    public void mustNotSaveAssociateWithEmptyFields() {
        SaveAssociateRequest invalidRequest = associateBuilder.buildSaveAssociateWithEmptyFields();

        associateService.saveAssociate(Utils.convertSaveAssociateRequestToJson(invalidRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body(containsString(ASSOCIATE_NAME_NOT_BLANK))
                    .body(containsString(ASSOCIATE_CPF_INVALID))
                    .body(containsString(ASSOCIATE_PHONE_NOT_BLANK))
                    .body(containsString(ASSOCIATE_EMAIL_INVALID))
                    .body(containsString(ASSOCIATE_PROFESSION_NOT_BLANK));
    }

    @Test
    @Tag("all")
    @Description("Must not save associate with null fields")
    public void mustNotSaveAssociateWithNullFields() {
        SaveAssociateRequest invalidRequest = associateBuilder.buildSaveAssociateWithNullFields();

        associateService.saveAssociate(Utils.convertSaveAssociateRequestToJson(invalidRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body(containsString(ASSOCIATE_NAME_NOT_NULL))
                    .body(containsString(ASSOCIATE_CPF_NOT_NULL))
                    .body(containsString(ASSOCIATE_BIRTH_DATE_NOT_NULL))
                    .body(containsString(ASSOCIATE_PHONE_NOT_NULL))
                    .body(containsString(ASSOCIATE_EMAIL_NOT_NULL))
                    .body(containsString(ASSOCIATE_PROFESSION_NOT_NULL))
                    .body(containsString(ASSOCIATE_SALARY_NOT_NULL));
    }

}
