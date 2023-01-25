package br.com.sicredi.bank.acceptance.associate;

import br.com.sicredi.bank.factory.associate.AssociateBuilder;
import br.com.sicredi.bank.model.request.associate.SaveAssociateRequest;
import br.com.sicredi.bank.model.request.associate.UpdateAssociatePaycheckRequest;
import br.com.sicredi.bank.model.response.associate.SaveAssociateResponse;
import br.com.sicredi.bank.model.response.associate.UpdateAssociatePaycheckResponse;
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
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Associate")
@Epic("Update Associate Paycheck")
public class UpdateAssociatePaycheckTest {

    AssociateService associateService = new AssociateService();
    AssociateBuilder associateBuilder = new AssociateBuilder();

    @Test
    @Tag("all")
    @Description("Must update associate paycheck successfully")
    public void mustUpdateAssociatePaycheckSuccessfully() {
        // Devido a regra de negócio, é necessário id de associado com mais de 3 meses desde a última atualização

        var savedId = 1L;

        UpdateAssociatePaycheckRequest updateRequest = associateBuilder.buildUpdateAssociatePaycheckRequest();

        UpdateAssociatePaycheckResponse updateResponse = associateService
                .updateAssociatePaycheck(savedId, Utils.convertUpdateAssociatePaycheckRequestToJson(updateRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_OK)
                    .extract().as(UpdateAssociatePaycheckResponse.class);

        assertEquals(savedId, updateResponse.getId());
        assertEquals(updateRequest.getProfession(), updateResponse.getProfession());
        assertEquals(updateRequest.getSalary(), updateResponse.getSalary());
        assertEquals(LocalDate.now().toString(), updateResponse.getLastPaycheck());
    }

    @Test
    @Tag("all")
    @Description("Must not update associate paycheck with nonexistent id")
    public void mustNotUpdateAssociatePaycheckWithNonexistentId() {
        var invalidId = 9999999999999999L;

        UpdateAssociatePaycheckRequest updateRequest = associateBuilder.buildUpdateAssociatePaycheckRequest();

        associateService.updateAssociatePaycheck(invalidId, Utils.convertUpdateAssociatePaycheckRequestToJson(updateRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .body(containsString(ASSOCIATE_FIND_ERROR));
    }

    @Test
    @Tag("all")
    @Description("Must not update associate paycheck with negative salary")
    public void mustNotUpdateAssociatePaycheckWithNegativeSalary() {
        SaveAssociateRequest saveRequest = associateBuilder.buildSaveAssociateRequest();

        SaveAssociateResponse saveResponse = associateService
                .saveAssociate(Utils.convertSaveAssociateRequestToJson(saveRequest))
                .then().extract().as(SaveAssociateResponse.class);

        UpdateAssociatePaycheckRequest updateInvalidRequest = associateBuilder.buildUpdateAssociateWithInvalidSalary();

        associateService.updateAssociatePaycheck(saveResponse.getId(), Utils.convertUpdateAssociatePaycheckRequestToJson(updateInvalidRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body(containsString(ASSOCIATE_SALARY_INVALID));

        associateService.deleteAssociate(saveResponse.getId());
    }

    @Test
    @Tag("all")
    @Description("Must not update associate paycheck with empty profession")
    public void mustNotUpdateAssociatePaycheckWithEmptyProfession() {
        SaveAssociateRequest saveRequest = associateBuilder.buildSaveAssociateRequest();

        SaveAssociateResponse saveResponse = associateService
                .saveAssociate(Utils.convertSaveAssociateRequestToJson(saveRequest))
                .then().extract().as(SaveAssociateResponse.class);

        UpdateAssociatePaycheckRequest updateInvalidRequest = associateBuilder.buildUpdateAssociateWithEmptyProfession();

        associateService.updateAssociatePaycheck(saveResponse.getId(), Utils.convertUpdateAssociatePaycheckRequestToJson(updateInvalidRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body(containsString(ASSOCIATE_PROFESSION_NOT_BLANK));

        associateService.deleteAssociate(saveResponse.getId());
    }

    @Test
    @Tag("all")
    @Description("Must not update associate paycheck less than 3 months after the last update")
    public void mustNotUpdateAssociatePaycheckWithInvalidLastPaycheck() {
        SaveAssociateRequest saveRequest = associateBuilder.buildSaveAssociateRequest();

        SaveAssociateResponse saveResponse = associateService
                .saveAssociate(Utils.convertSaveAssociateRequestToJson(saveRequest))
                .then().extract().as(SaveAssociateResponse.class);

        UpdateAssociatePaycheckRequest updateInvalidRequest = associateBuilder.buildUpdateAssociatePaycheckRequest();

        associateService.updateAssociatePaycheck(saveResponse.getId(), Utils.convertUpdateAssociatePaycheckRequestToJson(updateInvalidRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body(containsString(ASSOCIATE_BUSINESS_PAYCHECK_ERROR));

        associateService.deleteAssociate(saveResponse.getId());
    }

}
