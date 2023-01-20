package br.com.sicredi.bank.aceitacao.associate;

import br.com.sicredi.bank.builder.associate.AddressBuilder;
import br.com.sicredi.bank.builder.associate.AssociateBuilder;
import br.com.sicredi.bank.dto.request.address.AddressRequest;
import br.com.sicredi.bank.dto.request.associate.SaveAssociateRequest;
import br.com.sicredi.bank.dto.response.address.AddressResponse;
import br.com.sicredi.bank.dto.response.associate.SaveAssociateResponse;
import br.com.sicredi.bank.service.AssociateService;
import br.com.sicredi.bank.utils.Utils;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Associate")
@Epic("Update Associate Address")
public class UpdateAssociateAddressTest {

    AssociateService associateService = new AssociateService();
    AssociateBuilder associateBuilder = new AssociateBuilder();
    AddressBuilder addressBuilder = new AddressBuilder();

    @Test
    @Tag("all")
    @Description("Deve atualizar endereço do associado com sucesso")
    public void mustUpdateAssociateAddressSuccessfully() {
        SaveAssociateRequest associateRequest = associateBuilder.buildSaveAssociateRequest();

        SaveAssociateResponse associateResponse = associateService
                .saveAssociate(Utils.convertSaveAssociateRequestToJson(associateRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract().as(SaveAssociateResponse.class)
                ;

        AddressRequest saveAddressRequest = addressBuilder.buildAddressRequest();

        AddressResponse saveAddressResponse = associateService
                .saveAssociateAddress(associateResponse.getId(), Utils.convertAddressRequestToJson(saveAddressRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract().as(AddressResponse.class)
                ;

        AddressRequest updateAddressRequest = addressBuilder.buildUpdateAddressRequest();

        AddressResponse response = associateService
                .updateAssociateAddress(saveAddressResponse.getId(), associateResponse.getId(),
                        Utils.convertAddressRequestToJson(updateAddressRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_OK)
                    .extract().as(AddressResponse.class)
                ;

        assertEquals(saveAddressResponse.getId(), response.getId());
        assertEquals(updateAddressRequest.getZipCode(), response.getZipCode());
        assertEquals(updateAddressRequest.getStreetName(), response.getStreetName());
        assertEquals(updateAddressRequest.getNumber(), response.getNumber());
        assertEquals(updateAddressRequest.getCity(), response.getCity());
        assertEquals(updateAddressRequest.getState(), response.getState());
        assertEquals(updateAddressRequest.getCountry(), response.getCountry());

        associateService.deleteAssociate(associateResponse.getId())
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NO_CONTENT)
        ;
    }

    @Test
    @Tag("all")
    @Description("Deve não atualizar endereço do associado com idAssociate inexistente")
    public void mustNotUpdateAssociateAddressWithNonexistentIdAssociate() {
        var invalidId = 199391382734712L;

        AddressRequest addressRequest = addressBuilder.buildUpdateAddressRequest();

        associateService.updateAssociateAddress(invalidId, invalidId, Utils.convertAddressRequestToJson(addressRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .body(containsString("Associdado não encontrado."))
                ;
    }

    @Test
    @Tag("all")
    @Description("Deve não atualizar endereço do associado com id inexistente")
    public void mustNotUpdateAssociateAddressWithNonexistentId() {
        SaveAssociateRequest associateRequest = associateBuilder.buildSaveAssociateRequest();

        SaveAssociateResponse associateResponse = associateService
                .saveAssociate(Utils.convertSaveAssociateRequestToJson(associateRequest))
                    .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract().as(SaveAssociateResponse.class)
                ;

        var invalidId = 199391382734712L;

        AddressRequest updateAddressRequest = addressBuilder.buildUpdateAddressRequest();

        associateService
                .updateAssociateAddress(invalidId, associateResponse.getId(), Utils.convertAddressRequestToJson(updateAddressRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .body(containsString("Endereço não encontrado."))
                ;

        associateService.deleteAssociate(associateResponse.getId())
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NO_CONTENT)
        ;
    }

    @Test
    @Tag("all")
    @Description("Deve não atualizar endereço do associado sem preencher campos obrigatórios")
    public void mustNotUpdateAssociateAddressWithEmptyFields() {
        SaveAssociateRequest associateRequest = associateBuilder.buildSaveAssociateRequest();

        SaveAssociateResponse associateResponse = associateService
                .saveAssociate(Utils.convertSaveAssociateRequestToJson(associateRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract().as(SaveAssociateResponse.class)
                ;

        AddressRequest saveAddressRequest = addressBuilder.buildAddressRequest();

        AddressResponse saveAddressResponse = associateService
                .saveAssociateAddress(associateResponse.getId(), Utils.convertAddressRequestToJson(saveAddressRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract().as(AddressResponse.class)
                ;

        AddressRequest updateAddressRequest = addressBuilder.buildUpdateAddressWithEmptyFields();

        associateService.updateAssociateAddress(saveAddressResponse.getId(), associateResponse.getId(),
                        Utils.convertAddressRequestToJson(updateAddressRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body(containsString("CEP não pode ser vazio."))
                    .body(containsString("Rua não pode ser vazio."))
                    .body(containsString("Número não pode ser vazio."))
                    .body(containsString("Cidade não pode ser vazio."))
                    .body(containsString("Estado não pode ser vazio."))
                    .body(containsString("País não pode ser vazio."))
                ;

        associateService.deleteAssociate(associateResponse.getId())
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NO_CONTENT)
        ;
    }

    @Test
    @Tag("all")
    @Description("Deve não atualizar endereço do associado com campos nulos")
    public void mustNotUpdateAssociateAddressWithNullFields() {
        SaveAssociateRequest associateRequest = associateBuilder.buildSaveAssociateRequest();

        SaveAssociateResponse associateResponse = associateService
                .saveAssociate(Utils.convertSaveAssociateRequestToJson(associateRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract().as(SaveAssociateResponse.class)
                ;

        AddressRequest saveAddressRequest = addressBuilder.buildAddressRequest();

        AddressResponse saveAddressResponse = associateService
                .saveAssociateAddress(associateResponse.getId(), Utils.convertAddressRequestToJson(saveAddressRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract().as(AddressResponse.class)
                ;

        AddressRequest updateAddressRequest = addressBuilder.buildUpdateAddressWithNullFields();

        associateService.updateAssociateAddress(saveAddressResponse.getId(), associateResponse.getId(),
                        Utils.convertAddressRequestToJson(updateAddressRequest))
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body(containsString("CEP não pode ser nulo."))
                    .body(containsString("Rua não pode ser nulo."))
                    .body(containsString("Número não pode ser nulo."))
                    .body(containsString("Cidade não pode ser nulo."))
                    .body(containsString("Estado não pode ser nulo."))
                    .body(containsString("País não pode ser nulo."))
        ;

        associateService.deleteAssociate(associateResponse.getId())
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NO_CONTENT)
        ;
    }

}
