package br.com.sicredi.bank.aceitacao.contract;

import br.com.sicredi.bank.builder.associate.AssociateBuilder;
import br.com.sicredi.bank.builder.contract.ContractBuilder;
import br.com.sicredi.bank.dto.request.associate.SaveAssociateRequest;
import br.com.sicredi.bank.dto.request.contract.ContractRequest;
import br.com.sicredi.bank.dto.response.associate.SaveAssociateResponse;
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

import java.time.LocalDate;

import static br.com.sicredi.bank.dto.Constantes.ASSOCIATE_FIND_ERROR;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Contract")
@Epic("Sign Contract")
public class SignContractTest {

    AssociateService associateService = new AssociateService();
    AssociateBuilder associateBuilder = new AssociateBuilder();
    ContractService contractService = new ContractService();
    ContractBuilder contractBuilder = new ContractBuilder();

    @Test
    @Tag("error")
    @Description("Deve assinar contrato com sucesso")
    public void mustSignContractSuccessfully() {
        // NÃO CONSIGO DELETAR O ASSOCIADO JÁ QUE TEM CONTRATO ATIVO

        SaveAssociateRequest associateRequest = associateBuilder.buildSaveAssociateRequest();

        SaveAssociateResponse associateResponse = associateService
                .saveAssociate(Utils.convertSaveAssociateRequestToJson(associateRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract().as(SaveAssociateResponse.class)
                ;

        ContractRequest contractRequest = contractBuilder.buildContractRequest();
        contractRequest.setIdAssociate(associateResponse.getId());

        SaveContractResponse response = contractService.signContract(Utils.convertContractRequestToJson(contractRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract().as(SaveContractResponse.class)
                ;

        assertNotNull(response.getId());
        assertEquals(contractRequest.getProductType(), response.getProductType());
        assertEquals(contractRequest.getValue(), response.getValue());
        assertEquals(false, response.getPaidOff());
        assertEquals(LocalDate.now().toString(), response.getHireDate());
        assertEquals(LocalDate.now().plusMonths(1).toString(), response.getFirstPaymentDate());
        assertEquals(LocalDate.now().plusMonths(contractRequest.getNumberOfInstallments() + 1).toString(), response.getExpirationDate() );
        assertEquals(0, response.getInstallmentsPaid() );
        assertEquals(contractRequest.getNumberOfInstallments(), response.getInstallmentsRemaining() );

        associateService.deleteAssociate(associateResponse.getId())
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NO_CONTENT)
        ;
    }

    @Test
    @Tag("all")
    @Description("Deve não assinar contrato com idAssociate inexistente")
    public void mustNotSignContractWithNonexistentIdAssociate() {
        var invalidId = 199391382734712L;

        ContractRequest contractRequest = contractBuilder.buildContractRequest();
        contractRequest.setIdAssociate(invalidId);

        contractService.signContract(Utils.convertContractRequestToJson(contractRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .body(containsString(ASSOCIATE_FIND_ERROR))
                ;
    }

}
