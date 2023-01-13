package br.com.sicredi.bank.aceitacao.associate;

import br.com.sicredi.bank.builder.associate.AssociateBuilder;
import br.com.sicredi.bank.dto.request.associate.SaveAssociateRequest;
import br.com.sicredi.bank.dto.request.associate.UpdateAssociateRequest;
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

import java.math.BigDecimal;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Associate")
@Epic("Update Associate")
public class UpdateAssociateTest {

    private static final String UPDATE_SUCCESS = "Associado atualizado com sucesso!";
    private static final String UPDATE_ERROR = "Associado com menos de 3 meses desde da última atualização!";
    private static final BigDecimal MIN_SALARY_ACCEPTABLE = BigDecimal.valueOf(1500);

    AssociateService associateService = new AssociateService();
    AssociateBuilder associateBuilder = new AssociateBuilder();

    @Test
    @Tag("all")
    @Description("Deve atualizar associado com sucesso")
    public void deveAtualizarAssociadoComSucesso() {
        SaveAssociateRequest saveRequest = associateBuilder.buildSaveAssociateRequest();

        SaveAssociateResponse saveResponse = associateService
                .saveAssociate(Utils.convertSaveAssociateRequestToJson(saveRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract().as(SaveAssociateResponse.class)
                ;

        UpdateAssociateRequest updateRequest = associateBuilder.buildUpdateAssociateRequest();

        UpdateAssociateResponse updateResponse = associateService
                .udateAssociate(saveResponse.getId(), Utils.convertUpdateAssociateRequestToJson(updateRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NO_CONTENT)
                    .extract().as(UpdateAssociateResponse.class)
                ;

        assertTrue(updateResponse.getUpdated());
        assertEquals(UPDATE_SUCCESS, updateResponse.getMessage());

        associateService.deleteAssociate(saveResponse.getId())
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NO_CONTENT)
        ;
    }

    @Test
    @Tag("all")
    @Description("Deve não atualizar associado com id inexistente")
    public void deveNaoAtualizarAssociadoComIdInexistente() {
        UpdateAssociateRequest updateRequest = associateBuilder.buildUpdateAssociateRequest();
        var invalidId = 199391382734712L;

        associateService.udateAssociate(invalidId, Utils.convertUpdateAssociateRequestToJson(updateRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .body(containsString("ID INEXISTENTE"))
                ;
    }

    @Test
    @Tag("all")
    @Description("Deve não atualizar associado com salário abaixo de 1500")
    public void deveNaoAtualizarAssociadoComSalarioAbaixoDoPermitido() {
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
                    .body(containsString("SALÁRIO"))
        ;

        associateService.deleteAssociate(saveResponse.getId())
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NO_CONTENT)
        ;
    }

    @Test
    @Tag("all")
    @Description("Deve não atualizar associado sem preencher campos obrigatórios")
    public void deveNaoAtualizarAssociadoSemPreencherCamposObrigatorios() {
        SaveAssociateRequest saveRequest = associateBuilder.buildSaveAssociateRequest();

        SaveAssociateResponse saveResponse = associateService
                .saveAssociate(Utils.convertSaveAssociateRequestToJson(saveRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract().as(SaveAssociateResponse.class)
                ;

        UpdateAssociateRequest updateInvalidRequest = associateBuilder.buildUpdateAssociateWithEmptyFields();

        associateService.udateAssociate(saveResponse.getId(), Utils.convertUpdateAssociateRequestToJson(updateInvalidRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body(containsString("CAMPOS VAZIOS"))
        ;

        associateService.deleteAssociate(saveResponse.getId())
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NO_CONTENT)
        ;
    }

}
