package br.com.sicredi.bank.acceptance.associate;

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

import static br.com.sicredi.bank.utils.Message.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Associate")
@Epic("Save Associate Address")
public class SaveAssociateAddressTest {

    AssociateService associateService = new AssociateService();
    AssociateBuilder associateBuilder = new AssociateBuilder();
    AddressBuilder addressBuilder = new AddressBuilder();

    @Test
    @Tag("all")
    @Description("Deve salvar endereço do associado com sucesso")
    public void mustSaveAssociateAddressSuccessfully() {
        SaveAssociateRequest associateRequest = associateBuilder.buildSaveAssociateRequest();

        SaveAssociateResponse associateResponse = associateService
                .saveAssociate(Utils.convertSaveAssociateRequestToJson(associateRequest))
                .then().extract().as(SaveAssociateResponse.class);

        AddressRequest addressRequest = addressBuilder.buildAddressRequest();

        AddressResponse response = associateService
                .saveAssociateAddress(associateResponse.getId(), Utils.convertAddressRequestToJson(addressRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract().as(AddressResponse.class);

        assertNotNull(response.getId());
        assertEquals(addressRequest.getZipCode(), response.getZipCode());
        assertEquals(addressRequest.getStreetName(), response.getStreetName());
        assertEquals(addressRequest.getNumber(), response.getNumber());
        assertEquals(addressRequest.getCity(), response.getCity());
        assertEquals(addressRequest.getState(), response.getState());
        assertEquals(addressRequest.getCountry(), response.getCountry());

        associateService.deleteAssociate(associateResponse.getId());
    }

    @Test
    @Tag("all")
    @Description("Deve não salvar endereço do associado com idAssociate inexistente")
    public void mustNotSaveAssociateAddressWithNonexistentIdAssociate() {
        var invalidId = 9999999999999999L;

        AddressRequest addressRequest = addressBuilder.buildAddressRequest();

        associateService.saveAssociateAddress(invalidId, Utils.convertAddressRequestToJson(addressRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .body(containsString(ASSOCIATE_FIND_ERROR));
    }

    @Test
    @Tag("all")
    @Description("Deve não salvar endereço do associado sem preencher campos obrigatórios")
    public void mustNotSaveAssociateAddressWithEmptyFields() {
        SaveAssociateRequest associateRequest = associateBuilder.buildSaveAssociateRequest();

        SaveAssociateResponse associateResponse = associateService
                .saveAssociate(Utils.convertSaveAssociateRequestToJson(associateRequest))
                .then().extract().as(SaveAssociateResponse.class);

        AddressRequest addressRequest = addressBuilder.buildAddressWithEmptyFields();

        associateService.saveAssociateAddress(associateResponse.getId(), Utils.convertAddressRequestToJson(addressRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body(containsString(ADDRESS_CEP_NOT_BLANK))
                    .body(containsString(ADDRESS_STREET_NAME_NOT_BLANK))
                    .body(containsString(ADDRESS_NUMBER_NOT_BLANK))
                    .body(containsString(ADDRESS_CITY_NOT_BLANK))
                    .body(containsString(ADDRESS_STATE_NOT_BLANK))
                    .body(containsString(ADDRESS_COUNTRY_NOT_BLANK));

        associateService.deleteAssociate(associateResponse.getId());
    }

    @Test
    @Tag("all")
    @Description("Deve não salvar endereço do associado com campos nulos")
    public void mustNotSaveAssociateAddressWithNullFields() {
        SaveAssociateRequest associateRequest = associateBuilder.buildSaveAssociateRequest();

        SaveAssociateResponse associateResponse = associateService
                .saveAssociate(Utils.convertSaveAssociateRequestToJson(associateRequest))
                .then().extract().as(SaveAssociateResponse.class);

        AddressRequest addressRequest = addressBuilder.buildAddressWithNullFields();

        associateService.saveAssociateAddress(associateResponse.getId(), Utils.convertAddressRequestToJson(addressRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body(containsString(ADDRESS_CEP_NOT_NULL))
                    .body(containsString(ADDRESS_STREET_NAME_NOT_NULL))
                    .body(containsString(ADDRESS_NUMBER_NOT_NULL))
                    .body(containsString(ADDRESS_CITY_NOT_NULL))
                    .body(containsString(ADDRESS_STATE_NOT_NULL))
                    .body(containsString(ADDRESS_COUNTRY_NOT_NULL));

        associateService.deleteAssociate(associateResponse.getId());
    }

}
