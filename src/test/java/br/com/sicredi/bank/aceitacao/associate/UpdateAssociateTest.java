package br.com.sicredi.bank.aceitacao.associate;

import br.com.sicredi.bank.builder.associate.AssociateBuilder;
import br.com.sicredi.bank.dto.request.associate.SaveAssociateRequest;
import br.com.sicredi.bank.dto.request.associate.UpdateAssociatePaycheckRequest;
import br.com.sicredi.bank.dto.response.associate.SaveAssociateResponse;
import br.com.sicredi.bank.dto.response.associate.UpdateAssociateResponse;
import br.com.sicredi.bank.service.AssociateService;
import br.com.sicredi.bank.utils.Utils;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Associate")
@Epic("Update Associate")
public class UpdateAssociateTest {

    AssociateService associateService = new AssociateService();
    AssociateBuilder associateBuilder = new AssociateBuilder();

    @Test
    @Tag("error")
    @Description("Deve atualizar associado com sucesso")
    public void mustUpdateAssociateSuccessfully() {
        SaveAssociateRequest saveRequest = associateBuilder.buildSaveAssociateRequest();

        SaveAssociateResponse saveResponse = associateService
                .saveAssociate(Utils.convertSaveAssociateRequestToJson(saveRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract().as(SaveAssociateResponse.class)
                ;

        //saveResponse.setLastPaycheck(LocalDate.now().minusMonths(4).toString());

        UpdateAssociatePaycheckRequest updateRequest = associateBuilder.buildUpdateAssociateRequest();

        UpdateAssociateResponse updateResponse = associateService
                .udateAssociate(saveResponse.getId(), Utils.convertUpdateAssociateRequestToJson(updateRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_OK)
                    .extract().as(UpdateAssociateResponse.class)
                ;

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
    @Description("Deve não atualizar associado com id inexistente")
    public void mustNotUpdateAssociateWithNonexistentId() {
        UpdateAssociatePaycheckRequest updateRequest = associateBuilder.buildUpdateAssociateRequest();
        var invalidId = 199391382734712L;

        associateService.udateAssociate(invalidId, Utils.convertUpdateAssociateRequestToJson(updateRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .body(containsString("Associdado não encontrado."))
                ;
    }

/*    @Test
    @Tag("error")
    @Description("Deve não atualizar associado com salário negativo")
    public void mustNotUpdateAssociateWithNegativeSalary() {
        SaveAssociateRequest saveRequest = associateBuilder.buildSaveAssociateRequest();

        SaveAssociateResponse saveResponse = associateService
                .saveAssociate(Utils.convertSaveAssociateRequestToJson(saveRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract().as(SaveAssociateResponse.class)
                ;

        UpdateAssociateRequest updateInvalidRequest = associateBuilder.buildUpdateAssociateWithInvalidSalary();

        associateService.udateAssociate(saveResponse.getId(), Utils.convertUpdateAssociateRequestToJson(updateInvalidRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
        ;

        associateService.deleteAssociate(saveResponse.getId())
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NO_CONTENT)
        ;
    }*/

    @Test
    @Tag("error")
    @Description("Deve não atualizar associado sem preencher profissão")
    public void mustNotUpdateAssociateWithEmptyProfession() {
        SaveAssociateRequest saveRequest = associateBuilder.buildSaveAssociateRequest();

        SaveAssociateResponse saveResponse = associateService
                .saveAssociate(Utils.convertSaveAssociateRequestToJson(saveRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract().as(SaveAssociateResponse.class)
                ;

        UpdateAssociatePaycheckRequest updateInvalidRequest = associateBuilder.buildUpdateAssociateWithEmptyProfession();

        associateService.udateAssociate(saveResponse.getId(), Utils.convertUpdateAssociateRequestToJson(updateInvalidRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body(containsString("Profissão não pode ficar em branco."))
        ;

        associateService.deleteAssociate(saveResponse.getId())
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NO_CONTENT)
        ;
    }

    @Test
    @Tag("all")
    @Description("Deve não atualizar associado com menos de 3 meses da última atualização")
    public void mustNotUpdateAssociateWithInvalidLastPaycheck() {
        SaveAssociateRequest saveRequest = associateBuilder.buildSaveAssociateRequest();

        SaveAssociateResponse saveResponse = associateService
                .saveAssociate(Utils.convertSaveAssociateRequestToJson(saveRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract().as(SaveAssociateResponse.class)
                ;

        UpdateAssociatePaycheckRequest updateInvalidRequest = associateBuilder.buildUpdateAssociateRequest();

        associateService.udateAssociate(saveResponse.getId(), Utils.convertUpdateAssociateRequestToJson(updateInvalidRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body(containsString("Associado com menos de 3 meses desde da última atualização!"))
        ;

        associateService.deleteAssociate(saveResponse.getId())
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NO_CONTENT)
        ;
    }

}
