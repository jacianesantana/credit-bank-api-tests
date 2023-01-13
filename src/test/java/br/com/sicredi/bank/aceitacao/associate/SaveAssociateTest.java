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
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Associate")
@Epic("Save Associate")
public class SaveAssociateTest {

    AssociateService associateService = new AssociateService();
    AssociateBuilder associateBuilder = new AssociateBuilder();

    @Test
    @Tag("all")
    @Description("Deve salvar associado com sucesso")
    public void deveSalvarAssociadoComSucesso() {
        SaveAssociateRequest request = associateBuilder.buildSaveAssociateRequest();

        SaveAssociateResponse response = associateService.saveAssociate(Utils.convertSaveAssociateRequestToJson(request))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract().as(SaveAssociateResponse.class)
                ;

        assertEquals(request.getName(), response.getName());
        assertEquals(request.getCpf(), response.getCpf());

        associateService.deleteAssociate(response.getId())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_NO_CONTENT)
        ;
    }

    @Test
    @Tag("all")
    @Description("Deve não salvar associado com cpf já cadastrado")
    public void deveNaoSalvarAssociadoComCpfJaCadastrado() {
        SaveAssociateRequest request = associateBuilder.buildSaveAssociateRequest();
        SaveAssociateRequest invalidRequest = associateBuilder.buildSaveAssociateRequest();

        SaveAssociateResponse response = associateService.saveAssociate(Utils.convertSaveAssociateRequestToJson(request))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract().as(SaveAssociateResponse.class)
                ;

        associateService.saveAssociate(Utils.convertSaveAssociateRequestToJson(invalidRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body(containsString("CPF JÁ CADASTRADO"))
                ;

        associateService.deleteAssociate(response.getId())
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NO_CONTENT)
        ;
    }

    @Test
    @Tag("all")
    @Description("Deve não salvar associado com salário abaixo de 1500")
    public void deveNaoSalvarAssociadoComSalarioAbaixoDoPermitido() {
        SaveAssociateRequest invalidRequest = associateBuilder.buildSaveAssociateWithInvalidSalary();

        associateService.saveAssociate(Utils.convertSaveAssociateRequestToJson(invalidRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body(containsString("SALÁRIO"))
        ;
    }

    @Test
    @Tag("all")
    @Description("Deve não salvar associado com idade abaixo de 18 anos")
    public void deveNaoSalvarAssociadoComIdadeAbaixoDoPermitido() {
        SaveAssociateRequest invalidRequest = associateBuilder.buildSaveAssociateWithInvalidBirthDate();

        associateService.saveAssociate(Utils.convertSaveAssociateRequestToJson(invalidRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body(containsString("IDADE"))
        ;
    }

    @Test
    @Tag("all")
    @Description("Deve não salvar associado sem preencher campos obrigatórios")
    public void deveNaoSalvarAssociadoSemPreencherCamposObrigatorios() {
        SaveAssociateRequest invalidRequest = associateBuilder.buildSaveAssociateWithEmptyFields();

        associateService.saveAssociate(Utils.convertSaveAssociateRequestToJson(invalidRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body(containsString("CAMPOS VAZIOS"))
        ;
    }

}
