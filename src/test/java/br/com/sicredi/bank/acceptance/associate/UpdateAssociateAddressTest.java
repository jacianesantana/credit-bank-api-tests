package br.com.sicredi.bank.acceptance.associate;

import br.com.sicredi.bank.factory.associate.AddressBuilder;
import br.com.sicredi.bank.factory.associate.AssociateBuilder;
import br.com.sicredi.bank.model.request.address.AddressRequest;
import br.com.sicredi.bank.model.request.associate.SaveAssociateRequest;
import br.com.sicredi.bank.model.response.address.AddressResponse;
import br.com.sicredi.bank.model.response.associate.SaveAssociateResponse;
import br.com.sicredi.bank.service.AssociateService;
import br.com.sicredi.bank.utils.Utils;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static br.com.sicredi.bank.utils.Message.*;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Associate")
@Epic("Update Associate Address")
public class UpdateAssociateAddressTest {

    AssociateService associateService = new AssociateService();
    AssociateBuilder associateBuilder = new AssociateBuilder();
    AddressBuilder addressBuilder = new AddressBuilder();

    @Test
    @Tag("all")
    @Description("Must update associate address successfully")
    public void mustUpdateAssociateAddressSuccessfully() {
        SaveAssociateRequest associateRequest = associateBuilder.buildSaveAssociateRequest();

        SaveAssociateResponse associateResponse = associateService
                .saveAssociate(Utils.convertSaveAssociateRequestToJson(associateRequest))
                .then().extract().as(SaveAssociateResponse.class);

        AddressRequest saveAddressRequest = addressBuilder.buildAddressRequest();

        AddressResponse saveAddressResponse = associateService
                .saveAssociateAddress(associateResponse.getId(), Utils.convertAddressRequestToJson(saveAddressRequest))
                .then().extract().as(AddressResponse.class);

        AddressRequest updateAddressRequest = addressBuilder.buildUpdateAddressRequest();

        AddressResponse response = associateService
                .updateAssociateAddress(saveAddressResponse.getId(), associateResponse.getId(),
                        Utils.convertAddressRequestToJson(updateAddressRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_OK)
                    .extract().as(AddressResponse.class);

        assertEquals(saveAddressResponse.getId(), response.getId());
        assertEquals(updateAddressRequest.getZipCode(), response.getZipCode());
        assertEquals(updateAddressRequest.getStreetName(), response.getStreetName());
        assertEquals(updateAddressRequest.getNumber(), response.getNumber());
        assertEquals(updateAddressRequest.getCity(), response.getCity());
        assertEquals(updateAddressRequest.getState(), response.getState());
        assertEquals(updateAddressRequest.getCountry(), response.getCountry());

        associateService.deleteAssociate(associateResponse.getId());
    }

    @Test
    @Tag("all")
    @Description("Must not update associate address with nonexistent idAssociate")
    public void mustNotUpdateAssociateAddressWithNonexistentIdAssociate() {
        var invalidId = 9999999999999999L;

        AddressRequest addressRequest = addressBuilder.buildUpdateAddressRequest();

        associateService.updateAssociateAddress(invalidId, invalidId, Utils.convertAddressRequestToJson(addressRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .body(containsString(ASSOCIATE_FIND_ERROR));
    }

    @Test
    @Tag("all")
    @Description("Must not update associate address with nonexistent id")
    public void mustNotUpdateAssociateAddressWithNonexistentId() {
        SaveAssociateRequest associateRequest = associateBuilder.buildSaveAssociateRequest();

        SaveAssociateResponse associateResponse = associateService
                .saveAssociate(Utils.convertSaveAssociateRequestToJson(associateRequest))
                    .then().extract().as(SaveAssociateResponse.class);

        var invalidId = 9999999999999999L;

        AddressRequest updateAddressRequest = addressBuilder.buildUpdateAddressRequest();

        associateService
                .updateAssociateAddress(invalidId, associateResponse.getId(),
                        Utils.convertAddressRequestToJson(updateAddressRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .body(containsString(ADDRESS_FIND_ERROR));

        associateService.deleteAssociate(associateResponse.getId());
    }

    @Test
    @Tag("all")
    @Description("Must not update address that is not linked to the associate")
    public void mustNotUpdateAddressThatIsNotLinkedToTheAssociate() {
        SaveAssociateRequest associateOneRequest = associateBuilder.buildSaveAssociateRequest();

        SaveAssociateResponse associateOneResponse = associateService
                .saveAssociate(Utils.convertSaveAssociateRequestToJson(associateOneRequest))
                .then().extract().as(SaveAssociateResponse.class);

        SaveAssociateRequest associateTwoRequest = associateBuilder.buildSaveAssociateRequest();

        SaveAssociateResponse associateTwoResponse = associateService
                .saveAssociate(Utils.convertSaveAssociateRequestToJson(associateTwoRequest))
                .then().extract().as(SaveAssociateResponse.class);

        AddressRequest saveAddressRequest = addressBuilder.buildAddressRequest();

        AddressResponse saveAddressOneResponse = associateService
                .saveAssociateAddress(associateOneResponse.getId(), Utils.convertAddressRequestToJson(saveAddressRequest))
                .then().extract().as(AddressResponse.class);

        AddressRequest updateAddressRequest = addressBuilder.buildUpdateAddressRequest();

        associateService
                .updateAssociateAddress(saveAddressOneResponse.getId(), associateTwoResponse.getId(),
                        Utils.convertAddressRequestToJson(updateAddressRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .body(containsString(ADDRESS_FIND_ERROR));

        associateService.deleteAssociate(associateOneResponse.getId());
        associateService.deleteAssociate(associateTwoResponse.getId());
    }

    @Test
    @Tag("all")
    @Description("Must not update associate address with empty fields")
    public void mustNotUpdateAssociateAddressWithEmptyFields() {
        SaveAssociateRequest associateRequest = associateBuilder.buildSaveAssociateRequest();

        SaveAssociateResponse associateResponse = associateService
                .saveAssociate(Utils.convertSaveAssociateRequestToJson(associateRequest))
                .then().extract().as(SaveAssociateResponse.class);

        AddressRequest saveAddressRequest = addressBuilder.buildAddressRequest();

        AddressResponse saveAddressResponse = associateService
                .saveAssociateAddress(associateResponse.getId(), Utils.convertAddressRequestToJson(saveAddressRequest))
                .then().extract().as(AddressResponse.class);

        AddressRequest updateAddressRequest = addressBuilder.buildUpdateAddressWithEmptyFields();

        associateService
                .updateAssociateAddress(saveAddressResponse.getId(), associateResponse.getId(),
                        Utils.convertAddressRequestToJson(updateAddressRequest))
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
    @Description("Must not update associate address with null fields")
    public void mustNotUpdateAssociateAddressWithNullFields() {
        SaveAssociateRequest associateRequest = associateBuilder.buildSaveAssociateRequest();

        SaveAssociateResponse associateResponse = associateService
                .saveAssociate(Utils.convertSaveAssociateRequestToJson(associateRequest))
                .then().extract().as(SaveAssociateResponse.class);

        AddressRequest saveAddressRequest = addressBuilder.buildAddressRequest();

        AddressResponse saveAddressResponse = associateService
                .saveAssociateAddress(associateResponse.getId(), Utils.convertAddressRequestToJson(saveAddressRequest))
                .then().extract().as(AddressResponse.class);

        AddressRequest updateAddressRequest = addressBuilder.buildUpdateAddressWithNullFields();

        associateService
                .updateAssociateAddress(saveAddressResponse.getId(), associateResponse.getId(),
                        Utils.convertAddressRequestToJson(updateAddressRequest))
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
