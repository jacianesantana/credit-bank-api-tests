package br.com.sicredi.bank.acceptance.contract;

import br.com.sicredi.bank.builder.associate.AssociateBuilder;
import br.com.sicredi.bank.builder.contract.ContractBuilder;
import br.com.sicredi.bank.dto.request.contract.ContractRequest;
import br.com.sicredi.bank.dto.response.contract.FindContractResponse;
import br.com.sicredi.bank.dto.response.contract.SaveContractResponse;
import br.com.sicredi.bank.service.AssociateService;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Contract")
@Epic("Find Contract")
public class FindContractTest {

    AssociateService associateService = new AssociateService();
    AssociateBuilder associateBuilder = new AssociateBuilder();
    ContractService contractService = new ContractService();
    ContractBuilder contractBuilder = new ContractBuilder();

    @Test
    @Tag("error")
    @Description("Deve buscar contrato com sucesso")
    public void mustFindContractSuccessfully() {
        var idAssociate = 1L;

        ContractRequest contractRequest = contractBuilder.buildContractRequest();
        contractRequest.setIdAssociate(idAssociate);

        SaveContractResponse contractResponse = contractService.signContract(Utils.convertContractRequestToJson(contractRequest))
                .then().extract().as(SaveContractResponse.class);

        FindContractResponse response = contractService.findContract(contractResponse.getId())
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_OK)
                    .extract().as(FindContractResponse.class);

        assertEquals(contractResponse.getId(), response.getId());
        assertEquals(idAssociate, response.getAssociate().getId());
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
    @Description("Deve não buscar contrato com id inexistente")
    public void mustNotFindContractWithNonexistentId() {
        var invalidId = 9999999999999999L;

        contractService.findContract(invalidId)
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .body(containsString(CONTRACT_FIND_ERROR));
    }

}
