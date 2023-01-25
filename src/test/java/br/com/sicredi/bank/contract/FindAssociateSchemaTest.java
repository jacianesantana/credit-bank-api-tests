package br.com.sicredi.bank.contract;

import br.com.sicredi.bank.acceptance.base.BaseTest;
import br.com.sicredi.bank.service.AssociateService;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;

@DisplayName("Associate")
@Epic("Contract Find Associate")
public class FindAssociateSchemaTest extends BaseTest {

    AssociateService associateService = new AssociateService();

    @Test
    @Tag("contract")
    @Description("Deve exibir associado de acordo com o esquema")
    public void mustDisplayAssociateAccordingToSchema() {
        associateService.findAssociate(1L)
                .then()
                    .assertThat()
                    .body(matchesJsonSchema(new File("./src/main/resources/jsonSchema/associate.json")));
    }

}
