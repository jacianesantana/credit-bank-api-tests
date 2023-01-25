package br.com.sicredi.bank.acceptance.contract;

import br.com.sicredi.bank.factory.contract.ContractBuilder;
import br.com.sicredi.bank.model.request.contract.ContractRequest;
import br.com.sicredi.bank.model.response.contract.SaveContractResponse;
import br.com.sicredi.bank.service.ContractService;
import br.com.sicredi.bank.utils.Utils;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static br.com.sicredi.bank.utils.Message.*;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Contract")
@Epic("Sign Contract")
public class SignContractTest {

    ContractService contractService = new ContractService();
    ContractBuilder contractBuilder = new ContractBuilder();

    @Test
    @Tag("all")
    @Description("Must sign contract successfully")
    public void mustSignContractSuccessfully() {
        // Devido a regra de negócio, não é possível deletar associado com contrato ativo

        var savedIdAssociate = 1L;

        ContractRequest contractRequest = contractBuilder.buildContractRequest();
        contractRequest.setIdAssociate(savedIdAssociate);

        SaveContractResponse response = contractService.signContract(Utils.convertContractRequestToJson(contractRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract().as(SaveContractResponse.class);

        assertNotNull(response.getId());
        assertEquals(contractRequest.getProductType(), response.getProductType());
        assertEquals(contractRequest.getValue(), response.getValue());
        assertEquals(false, response.getPaidOff());
        assertEquals(LocalDate.now().toString(), response.getHireDate());
        assertEquals(LocalDate.now().plusMonths(1).toString(), response.getFirstPaymentDate());
        assertEquals(LocalDate.now().plusMonths(contractRequest.getNumberOfInstallments() + 1).toString(), response.getExpirationDate() );
        assertEquals(0, response.getInstallmentsPaid() );
        assertEquals(contractRequest.getNumberOfInstallments(), response.getInstallmentsRemaining() );
    }

    @Test
    @Tag("all")
    @Description("Must not sign contract with nonexistent idAssociate")
    public void mustNotSignContractWithNonexistentIdAssociate() {
        var invalidId = 9999999999999999L;

        ContractRequest contractRequest = contractBuilder.buildContractRequest();
        contractRequest.setIdAssociate(invalidId);

        contractService.signContract(Utils.convertContractRequestToJson(contractRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .body(containsString(ASSOCIATE_FIND_ERROR));
    }

    @Test
    @Tag("all")
    @Description("Must not sign contract with null fields")
    public void mustNotSignContractWithNullFields() {
        ContractRequest contractRequest = contractBuilder.buildContractWithNullFields();

        contractService.signContract(Utils.convertContractRequestToJson(contractRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body(containsString(CONTRACT_ID_ASSOCIATE_NOT_NULL))
                    .body(containsString(CONTRACT_PRODUCT_TYPE_NOT_NULL))
                    .body(containsString(CONTRACT_NUMBER_OF_INSTALLMENTS_NOT_NULL))
                    .body(containsString(CONTRACT_VALUE_NOT_NULL));
    }

}
