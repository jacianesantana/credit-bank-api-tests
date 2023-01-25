package br.com.sicredi.bank.acceptance.contract;

import br.com.sicredi.bank.factory.contract.ContractBuilder;
import br.com.sicredi.bank.model.request.contract.ContractRequest;
import br.com.sicredi.bank.model.response.contract.FindContractResponse;
import br.com.sicredi.bank.model.response.contract.SaveContractResponse;
import br.com.sicredi.bank.service.ContractService;
import br.com.sicredi.bank.utils.Utils;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static br.com.sicredi.bank.utils.Message.CONTRACT_FIND_ERROR;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Contract")
@Epic("Find Contract")
public class FindContractTest {

    ContractService contractService = new ContractService();
    ContractBuilder contractBuilder = new ContractBuilder();

    @Test
    @Tag("all")
    @Description("Must find contract successfully")
    public void mustFindContractSuccessfully() {
        // Devido a regra de negócio, não é possível deletar associado com contrato ativo

        var savedIdAssociate = 1L;

        ContractRequest contractRequest = contractBuilder.buildContractRequest();
        contractRequest.setIdAssociate(savedIdAssociate);

        SaveContractResponse contractResponse = contractService
                .signContract(Utils.convertContractRequestToJson(contractRequest))
                .then().extract().as(SaveContractResponse.class);

        FindContractResponse response = contractService.findContract(contractResponse.getId())
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_OK)
                    .extract().as(FindContractResponse.class);

        assertEquals(contractResponse.getId(), response.getId());
        assertEquals(savedIdAssociate, response.getAssociate().getId());
        assertEquals(contractResponse.getProductType(), response.getProduct().getType());
        assertEquals(contractResponse.getValue(), response.getValue());
        assertEquals(contractResponse.getPaidOff(), response.getPaidOff());
        assertEquals(contractResponse.getHireDate(), response.getHireDate());
        assertEquals(contractResponse.getExpirationDate(), response.getExpirationDate());
        assertEquals(contractResponse.getInstallmentsPaid(), response.getInstallmentsPaid());
        assertEquals(contractResponse.getInstallmentsRemaining(), response.getInstallmentsRemaining());
        assertEquals(contractResponse.getFirstPaymentDate(), response.getFirstPaymentDate());
    }

    @Test
    @Tag("all")
    @Description("Must not find contract with nonexistent id")
    public void mustNotFindContractWithNonexistentId() {
        var invalidId = 9999999999999999L;

        contractService.findContract(invalidId)
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .body(containsString(CONTRACT_FIND_ERROR));
    }

}
