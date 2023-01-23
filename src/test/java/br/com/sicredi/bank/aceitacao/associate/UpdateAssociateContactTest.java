package br.com.sicredi.bank.aceitacao.associate;

import br.com.sicredi.bank.builder.associate.AssociateBuilder;
import br.com.sicredi.bank.dto.request.associate.SaveAssociateRequest;
import br.com.sicredi.bank.dto.request.associate.UpdateAssociateContactRequest;
import br.com.sicredi.bank.dto.response.associate.SaveAssociateResponse;
import br.com.sicredi.bank.dto.response.associate.UpdateAssociateContactResponse;
import br.com.sicredi.bank.service.AssociateService;
import br.com.sicredi.bank.utils.Utils;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static br.com.sicredi.bank.dto.Constantes.ASSOCIATE_FIND_ERROR;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Associate")
@Epic("Update Associate Contact")
public class UpdateAssociateContactTest {

    AssociateService associateService = new AssociateService();
    AssociateBuilder associateBuilder = new AssociateBuilder();

    @Test
    @Tag("all")
    @Description("Deve atualizar contato do associado com sucesso")
    public void mustUpdateAssociateContactSuccessfully() {
        SaveAssociateRequest saveRequest = associateBuilder.buildSaveAssociateRequest();

        SaveAssociateResponse saveResponse = associateService
                .saveAssociate(Utils.convertSaveAssociateRequestToJson(saveRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract().as(SaveAssociateResponse.class)
                ;

        UpdateAssociateContactRequest updateRequest = associateBuilder.buildUpdateAssociateContactRequest();

        UpdateAssociateContactResponse updateResponse = associateService
                .updateAssociateContact(saveResponse.getId(), Utils.convertUpdateAssociateContactRequestToJson(updateRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_OK)
                    .extract().as(UpdateAssociateContactResponse.class)
                ;

        assertEquals(saveResponse.getId(), updateResponse.getId());
        assertEquals(updateRequest.getPhone(), updateResponse.getPhone());
        assertEquals(updateRequest.getEmail(), updateResponse.getEmail());

        associateService.deleteAssociate(saveResponse.getId())
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NO_CONTENT)
        ;
    }

    @Test
    @Tag("all")
    @Description("Deve não atualizar contato do associado com id inexistente")
    public void mustNotUpdateAssociateContactWithNonexistentId() {
        var invalidId = 199391382734712L;

        UpdateAssociateContactRequest updateRequest = associateBuilder.buildUpdateAssociateContactRequest();

        associateService.updateAssociateContact(invalidId, Utils.convertUpdateAssociateContactRequestToJson(updateRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .body(containsString(ASSOCIATE_FIND_ERROR))
                ;
    }

    @Test
    @Tag("all")
    @Description("Deve não atualizar contato do associado sem preencher campos obrigatórios")
    public void mustNotUpdateAssociateContactWithEmptyFields() {
        SaveAssociateRequest saveRequest = associateBuilder.buildSaveAssociateRequest();

        SaveAssociateResponse saveResponse = associateService
                .saveAssociate(Utils.convertSaveAssociateRequestToJson(saveRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract().as(SaveAssociateResponse.class)
                ;

        UpdateAssociateContactRequest updateRequest = associateBuilder.buildUpdateAssociateContactWithEmptyFields();

        associateService
                .updateAssociateContact(saveResponse.getId(), Utils.convertUpdateAssociateContactRequestToJson(updateRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body(containsString("Telefone não pode ser vazio."))
                    .body(containsString("Email não pode ser vazio."))
                    .body(containsString("Email inválido."))
                ;

        associateService.deleteAssociate(saveResponse.getId())
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NO_CONTENT)
        ;
    }

    @Test
    @Tag("all")
    @Description("Deve não atualizar contato do associado com campos nulos")
    public void mustNotUpdateAssociateContactWithNullFields() {
        SaveAssociateRequest saveRequest = associateBuilder.buildSaveAssociateRequest();

        SaveAssociateResponse saveResponse = associateService
                .saveAssociate(Utils.convertSaveAssociateRequestToJson(saveRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract().as(SaveAssociateResponse.class)
                ;

        UpdateAssociateContactRequest updateRequest = associateBuilder.buildUpdateAssociateContactWithNullFields();

        associateService
                .updateAssociateContact(saveResponse.getId(), Utils.convertUpdateAssociateContactRequestToJson(updateRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body(containsString("Telefone não pode ser nulo."))
                    .body(containsString("Email não pode ser nulo."))
        ;

        associateService.deleteAssociate(saveResponse.getId())
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NO_CONTENT)
        ;
    }

}
