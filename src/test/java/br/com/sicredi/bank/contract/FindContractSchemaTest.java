package br.com.sicredi.bank.contract;

import br.com.sicredi.bank.service.ContractService;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;

@DisplayName("Contract")
@Epic("Contract Find Contract")
public class FindContractSchemaTest {

    ContractService contractService = new ContractService();

    @Test
    @Tag("contract")
    @Description("Deve exibir contrato de acordo com o esquema")
    public void mustDisplayContractAccordingToSchema() {
        contractService.findContract(1L)
                .then()
                    .assertThat()
                    .body(matchesJsonSchema(new File("./src/main/resources/jsonSchema/contract.json")));
    }

}
