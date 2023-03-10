package br.com.sicredi.bank.acceptance.associate;

import br.com.sicredi.bank.factory.associate.AssociateBuilder;
import br.com.sicredi.bank.factory.contract.ContractBuilder;
import br.com.sicredi.bank.model.request.associate.SaveAssociateRequest;
import br.com.sicredi.bank.model.request.contract.ContractRequest;
import br.com.sicredi.bank.model.response.associate.SaveAssociateResponse;
import br.com.sicredi.bank.service.AssociateService;
import br.com.sicredi.bank.service.ContractService;
import br.com.sicredi.bank.utils.Utils;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static br.com.sicredi.bank.utils.Message.ASSOCIATE_BUSINESS_CONTRACT_ERROR;
import static br.com.sicredi.bank.utils.Message.ASSOCIATE_FIND_ERROR;
import static org.hamcrest.Matchers.containsString;

@DisplayName("Associate")
@Epic("Delete Associate")
public class DeleteAssociateTest {

    AssociateService associateService = new AssociateService();
    AssociateBuilder associateBuilder = new AssociateBuilder();
    ContractService contractService = new ContractService();
    ContractBuilder contractBuilder = new ContractBuilder();

    @Test
    @Tag("all")
    @Description("Must delete associate successfully")
    public void mustDeleteAssociateSuccessfully() {
        SaveAssociateRequest request = associateBuilder.buildSaveAssociateRequest();

        SaveAssociateResponse response = associateService.saveAssociate(Utils.convertSaveAssociateRequestToJson(request))
                .then().extract().as(SaveAssociateResponse.class);

        associateService.deleteAssociate(response.getId())
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NO_CONTENT);
    }

    @Test
    @Tag("all")
    @Description("Must not delete associate with nonexistent id")
    public void mustNotDeleteAssociateWithNonexistentId() {
        var invalidId = 9999999999999999L;

        associateService.deleteAssociate(invalidId)
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .body(containsString(ASSOCIATE_FIND_ERROR));
    }

    @Test
    @Tag("all")
    @Description("Must not delete associate with active contract")
    public void mustNotDeleteAssociateWithActiveContract() {
        SaveAssociateRequest associateRequest = associateBuilder.buildSaveAssociateRequest();

        SaveAssociateResponse associateResponse = associateService.saveAssociate(Utils.convertSaveAssociateRequestToJson(associateRequest))
                .then().extract().as(SaveAssociateResponse.class);

        ContractRequest contractRequest = contractBuilder.buildContractRequest();
        contractRequest.setIdAssociate(associateResponse.getId());

        contractService.signContract(Utils.convertContractRequestToJson(contractRequest));

        associateService.deleteAssociate(associateResponse.getId())
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body(containsString(ASSOCIATE_BUSINESS_CONTRACT_ERROR));
    }

}
