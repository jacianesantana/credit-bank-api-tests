package br.com.sicredi.bank.aceitacao.associate;

import br.com.sicredi.bank.builder.associate.AssociateBuilder;
import br.com.sicredi.bank.dto.request.associate.SaveAssociateRequest;
import br.com.sicredi.bank.dto.request.associate.UpdateAssociatePaycheckRequest;
import br.com.sicredi.bank.dto.response.associate.SaveAssociateResponse;
import br.com.sicredi.bank.dto.response.associate.UpdateAssociatePaycheckResponse;
import br.com.sicredi.bank.service.AssociateService;
import br.com.sicredi.bank.utils.Utils;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static br.com.sicredi.bank.dto.Constantes.ASSOCIATE_BUSINESS_PAYCHECK_ERROR;
import static br.com.sicredi.bank.dto.Constantes.ASSOCIATE_FIND_ERROR;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Associate")
@Epic("Update Associate Paycheck")
public class UpdateAssociatePaycheckTest {

    AssociateService associateService = new AssociateService();
    AssociateBuilder associateBuilder = new AssociateBuilder();

    @Test
    @Tag("error")
    @Description("Deve atualizar contra-cheque do associado com sucesso")
    public void mustUpdateAssociatePaycheckSuccessfully() {
        SaveAssociateRequest saveRequest = associateBuilder.buildSaveAssociateRequest();

        SaveAssociateResponse saveResponse = associateService
                .saveAssociate(Utils.convertSaveAssociateRequestToJson(saveRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract().as(SaveAssociateResponse.class)
                ;

        saveResponse.setLastPaycheck(LocalDate.now().minusMonths(4).toString());

        UpdateAssociatePaycheckRequest updateRequest = associateBuilder.buildUpdateAssociatePaycheckRequest();

        UpdateAssociatePaycheckResponse updateResponse = associateService
                .updateAssociatePaycheck(saveResponse.getId(), Utils.convertUpdateAssociatePaycheckRequestToJson(updateRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_OK)
                    .extract().as(UpdateAssociatePaycheckResponse.class)
                ;

        assertEquals(saveResponse.getId(), updateResponse.getId());
        assertEquals(updateRequest.getProfession(), updateResponse.getProfession());
        assertEquals(updateRequest.getSalary(), updateResponse.getSalary());
        assertEquals(LocalDate.now().toString(), updateResponse.getLastPaycheck());

        associateService.deleteAssociate(saveResponse.getId())
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NO_CONTENT)
        ;
    }

    @Test
    @Tag("all")
    @Description("Deve não atualizar contra-cheque do associado com id inexistente")
    public void mustNotUpdateAssociatePaycheckWithNonexistentId() {
        var invalidId = 199391382734712L;

        UpdateAssociatePaycheckRequest updateRequest = associateBuilder.buildUpdateAssociatePaycheckRequest();

        associateService.updateAssociatePaycheck(invalidId, Utils.convertUpdateAssociatePaycheckRequestToJson(updateRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .body(containsString(ASSOCIATE_FIND_ERROR))
                ;
    }

    @Test
    @Tag("all")
    @Description("Deve não atualizar contra-cheque do associado com salário negativo")
    public void mustNotUpdateAssociatePaycheckWithNegativeSalary() {
        SaveAssociateRequest saveRequest = associateBuilder.buildSaveAssociateRequest();

        SaveAssociateResponse saveResponse = associateService
                .saveAssociate(Utils.convertSaveAssociateRequestToJson(saveRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract().as(SaveAssociateResponse.class)
                ;

        saveResponse.setLastPaycheck(LocalDate.now().minusMonths(4).toString());

        UpdateAssociatePaycheckRequest updateInvalidRequest = associateBuilder.buildUpdateAssociateWithInvalidSalary();

        associateService.updateAssociatePaycheck(saveResponse.getId(), Utils.convertUpdateAssociatePaycheckRequestToJson(updateInvalidRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body(containsString("Salário inválido."))
        ;

        associateService.deleteAssociate(saveResponse.getId())
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NO_CONTENT)
        ;
    }

    @Test
    @Tag("all")
    @Description("Deve não atualizar contra-cheque do associado sem preencher profissão")
    public void mustNotUpdateAssociatePaycheckWithEmptyProfession() {
        SaveAssociateRequest saveRequest = associateBuilder.buildSaveAssociateRequest();

        SaveAssociateResponse saveResponse = associateService
                .saveAssociate(Utils.convertSaveAssociateRequestToJson(saveRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract().as(SaveAssociateResponse.class)
                ;

        UpdateAssociatePaycheckRequest updateInvalidRequest = associateBuilder.buildUpdateAssociateWithEmptyProfession();

        associateService.updateAssociatePaycheck(saveResponse.getId(), Utils.convertUpdateAssociatePaycheckRequestToJson(updateInvalidRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body(containsString("Profissão não pode ser vazio."))
        ;

        associateService.deleteAssociate(saveResponse.getId())
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NO_CONTENT)
        ;
    }

    @Test
    @Tag("all")
    @Description("Deve não atualizar contra-cheque do associado com menos de 3 meses da última atualização")
    public void mustNotUpdateAssociatePaycheckWithInvalidLastPaycheck() {
        SaveAssociateRequest saveRequest = associateBuilder.buildSaveAssociateRequest();

        SaveAssociateResponse saveResponse = associateService
                .saveAssociate(Utils.convertSaveAssociateRequestToJson(saveRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract().as(SaveAssociateResponse.class)
                ;

        UpdateAssociatePaycheckRequest updateInvalidRequest = associateBuilder.buildUpdateAssociatePaycheckRequest();

        associateService.updateAssociatePaycheck(saveResponse.getId(), Utils.convertUpdateAssociatePaycheckRequestToJson(updateInvalidRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body(containsString(ASSOCIATE_BUSINESS_PAYCHECK_ERROR))
        ;

        associateService.deleteAssociate(saveResponse.getId())
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NO_CONTENT)
        ;
    }

}
