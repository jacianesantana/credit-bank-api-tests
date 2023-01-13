package br.com.sicredi.bank.aceitacao.associate;

import br.com.sicredi.bank.builder.associate.AssociateBuilder;
import br.com.sicredi.bank.dto.request.associate.SaveAssociateRequest;
import br.com.sicredi.bank.dto.response.associate.SaveAssociateResponse;
import br.com.sicredi.bank.service.AssociateService;
import br.com.sicredi.bank.utils.Utils;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.containsString;

@DisplayName("Associate")
@Epic("Delete Associate")
public class DeleteAssociateTest {

    private static final String DELETE_SUCCESS = "Associado excluido com sucesso!";
    private static final String DELETE_ERROR = "Associado contém contratos ativos!";

    AssociateService associateService = new AssociateService();
    AssociateBuilder associateBuilder = new AssociateBuilder();

    @Test
    @Tag("all")
    @Description("Deve deletar associado com sucesso")
    public void deveDeletarAssociadoComSucesso() {
        SaveAssociateRequest request = associateBuilder.buildSaveAssociateRequest();

        SaveAssociateResponse response = associateService.saveAssociate(Utils.convertSaveAssociateRequestToJson(request))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract().as(SaveAssociateResponse.class)
                ;

        associateService.deleteAssociate(response.getId())
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NO_CONTENT)
                    .body(containsString(DELETE_SUCCESS))
        ;
    }

    @Test
    @Tag("all")
    @Description("Deve não deletar associado com id inexistente")
    public void deveNaoDeletarAssociadoComIdInexistente() {
        var invalidId = 179416461470170L;

        associateService.deleteAssociate(invalidId)
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .body(containsString("ID INEXISTENTE"))
        ;
    }

/*    @Test
    @Tag("all")
    @Description("Deve não deletar associado com contrato ativo")
    public void deveNaoDeletarAssociadoComContratoAtivo() {
        // FALTA ADICIONAR CONTRATO

        SaveAssociateRequest request = associateBuilder.buildSaveAssociateRequest();

        SaveAssociateResponse response = associateService.saveAssociate(Utils.convertSaveAssociateRequestToJson(request))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract().as(SaveAssociateResponse.class)
                ;

        associateService.deleteAssociate(response.getId())
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body(containsString(DELETE_ERROR))
        ;
    }*/

}
