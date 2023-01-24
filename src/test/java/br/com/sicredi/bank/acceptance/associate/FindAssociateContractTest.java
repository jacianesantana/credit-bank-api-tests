package br.com.sicredi.bank.acceptance.associate;

import br.com.sicredi.bank.builder.associate.AssociateBuilder;
import br.com.sicredi.bank.dto.request.associate.SaveAssociateRequest;
import br.com.sicredi.bank.dto.response.associate.FindAssociateResponse;
import br.com.sicredi.bank.dto.response.associate.SaveAssociateResponse;
import br.com.sicredi.bank.service.AssociateService;
import br.com.sicredi.bank.utils.Utils;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static br.com.sicredi.bank.utils.Message.ASSOCIATE_FIND_ERROR;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Associate")
@Epic("Find Associate")
public class FindAssociateContractTest {

    AssociateService associateService = new AssociateService();
    AssociateBuilder associateBuilder = new AssociateBuilder();

    @Test
    @Tag("all")
    @Description("Deve buscar associado com sucesso")
    public void mustFindAssociateSuccessfully() {
        SaveAssociateRequest saveRequest = associateBuilder.buildSaveAssociateRequest();

        SaveAssociateResponse saveResponse = associateService.saveAssociate(Utils.convertSaveAssociateRequestToJson(saveRequest))
                .then().extract().as(SaveAssociateResponse.class);

        FindAssociateResponse findResponse = associateService.findAssociate(saveResponse.getId())
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_OK)
                    .extract().as(FindAssociateResponse.class);

        assertEquals(saveResponse.getId(), findResponse.getId());
        assertEquals(saveRequest.getName(), findResponse.getName());
        assertEquals(saveRequest.getCpf(), findResponse.getCpf());
        assertEquals(saveRequest.getBirthDate(), findResponse.getBirthDate());
        assertEquals(saveRequest.getPhone(), findResponse.getPhone());
        assertEquals(saveRequest.getEmail(), findResponse.getEmail());
        assertEquals(saveRequest.getProfession(), findResponse.getProfession());
        assertEquals(saveRequest.getSalary(), findResponse.getSalary());
        assertEquals(0, findResponse.getAddresses().size());
        assertEquals(2, findResponse.getAccounts().size());
        assertEquals(0, findResponse.getContracts().size());

        associateService.deleteAssociate(findResponse.getId());
    }

    @Test
    @Tag("all")
    @Description("Deve não buscar associado com id inexistente")
    public void mustNotFindAssociateWithNonexistentId() {
        var invalidId = 9999999999999999L;

        associateService.findAssociate(invalidId)
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .body(containsString(ASSOCIATE_FIND_ERROR));
    }

}
