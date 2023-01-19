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

import java.time.LocalDate;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Associate")
@Epic("Save Associate")
public class SaveAssociateTest {

    AssociateService associateService = new AssociateService();
    AssociateBuilder associateBuilder = new AssociateBuilder();

    @Test
    @Tag("all")
    @Description("Deve salvar associado com sucesso")
    public void mustSaveAssociateSuccessfully() {
        SaveAssociateRequest request = associateBuilder.buildSaveAssociateRequest();

        SaveAssociateResponse response = associateService.saveAssociate(Utils.convertSaveAssociateRequestToJson(request))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract().as(SaveAssociateResponse.class)
                ;

        assertNotNull(response.getId());
        assertEquals(request.getName(), response.getName());
        assertEquals(request.getCpf(), response.getCpf());
        assertEquals(request.getBirthDate(), response.getBirthDate());
        assertEquals(request.getPhone(), response.getPhone());
        assertEquals(request.getEmail(), response.getEmail());
        assertEquals(request.getProfession(), response.getProfession());
        assertEquals(request.getSalary(), response.getSalary());
        assertEquals(LocalDate.now().toString(), response.getLastPaycheck());
        assertNotNull(response.getAccounts());

        associateService.deleteAssociate(response.getId())
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NO_CONTENT)
        ;
    }

    @Test
    @Tag("all")
    @Description("Deve não salvar associado com cpf já cadastrado")
    public void mustNotSaveAssociateWithRegisteredCpf() {
        SaveAssociateRequest request = associateBuilder.buildSaveAssociateRequest();

        SaveAssociateResponse response = associateService.saveAssociate(Utils.convertSaveAssociateRequestToJson(request))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract().as(SaveAssociateResponse.class)
                ;

        associateService.saveAssociate(Utils.convertSaveAssociateRequestToJson(request))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body(containsString("Não foi possível salvar. Associado já existe!"))
                ;

        associateService.deleteAssociate(response.getId())
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NO_CONTENT)
        ;
    }

    @Test
    @Tag("all")
    @Description("Deve não salvar associado com idade abaixo de 18 anos")
    public void mustNotSaveUnderageAssociate() {
        SaveAssociateRequest invalidRequest = associateBuilder.buildSaveAssociateWithInvalidBirthDate();

        associateService.saveAssociate(Utils.convertSaveAssociateRequestToJson(invalidRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body(containsString("Idade não atende o mínimo necessário!"))
        ;
    }

    @Test
    @Tag("all")
    @Description("Deve não salvar associado com salário negativo")
    public void mustNotSaveAssociateWithNegativeSalary() {
        SaveAssociateRequest invalidRequest = associateBuilder.buildSaveAssociateWithInvalidSalary();

        associateService.saveAssociate(Utils.convertSaveAssociateRequestToJson(invalidRequest))
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(containsString("Salário inválido."))
        ;
    }

    @Test
    @Tag("all")
    @Description("Deve não salvar associado com cpf inválido")
    public void mustNotSaveAssociateWithInvalidCpf() {
        SaveAssociateRequest invalidRequest = associateBuilder.buildSaveAssociateWithInvalidCpf();

        associateService.saveAssociate(Utils.convertSaveAssociateRequestToJson(invalidRequest))
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(containsString("CPF inválido."))
        ;
    }

    @Test
    @Tag("all")
    @Description("Deve não salvar associado com email inválido")
    public void mustNotSaveAssociateWithInvalidEmail() {
        SaveAssociateRequest invalidRequest = associateBuilder.buildSaveAssociateWithInvalidEmail();

        associateService.saveAssociate(Utils.convertSaveAssociateRequestToJson(invalidRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body(containsString("Email inválido."))
        ;
    }

    @Test
    @Tag("all")
    @Description("Deve não salvar associado sem preencher nome")
    public void mustNotSaveAssociateWithEmptyName() {
        SaveAssociateRequest invalidRequest = associateBuilder.buildSaveAssociateWithEmptyName();

        associateService.saveAssociate(Utils.convertSaveAssociateRequestToJson(invalidRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body(containsString("Nome não pode ser vazio."))
        ;
    }

    @Test
    @Tag("all")
    @Description("Deve não salvar associado sem preencher cpf")
    public void mustNotSaveAssociateWithEmptyCpf() {
        SaveAssociateRequest invalidRequest = associateBuilder.buildSaveAssociateWithEmptyCpf();

        associateService.saveAssociate(Utils.convertSaveAssociateRequestToJson(invalidRequest))
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(containsString("CPF não pode ser vazio."))
                .body(containsString("CPF inválido."))
        ;
    }

    @Test
    @Tag("all")
    @Description("Deve não salvar associado sem preencher telefone")
    public void mustNotSaveAssociateWithEmptyPhone() {
        SaveAssociateRequest invalidRequest = associateBuilder.buildSaveAssociateWithEmptyPhone();

        associateService.saveAssociate(Utils.convertSaveAssociateRequestToJson(invalidRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body(containsString("Telefone não pode ser vazio."))
        ;
    }

    @Test
    @Tag("all")
    @Description("Deve não salvar associado sem preencher email")
    public void mustNotSaveAssociateWithEmptyEmail() {
        SaveAssociateRequest invalidRequest = associateBuilder.buildSaveAssociateWithEmptyEmail();

        associateService.saveAssociate(Utils.convertSaveAssociateRequestToJson(invalidRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body(containsString("Email não pode ser vazio."))
                    .body(containsString("Email inválido."))
        ;
    }

    @Test
    @Tag("all")
    @Description("Deve não salvar associado sem preencher profissão")
    public void mustNotSaveAssociateWithEmptyProfession() {
        SaveAssociateRequest invalidRequest = associateBuilder.buildSaveAssociateWithEmptyProfession();

        associateService.saveAssociate(Utils.convertSaveAssociateRequestToJson(invalidRequest))
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(containsString("Profissão não pode ser vazio."))
        ;
    }

    @Test
    @Tag("all")
    @Description("Deve não salvar associado com nome nulo")
    public void mustNotSaveAssociateWithNullName() {
        SaveAssociateRequest invalidRequest = associateBuilder.buildSaveAssociateWithNullName();

        associateService.saveAssociate(Utils.convertSaveAssociateRequestToJson(invalidRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body(containsString("Nome não pode ser nulo."))
        ;
    }

    @Test
    @Tag("all")
    @Description("Deve não salvar associado com cpf nulo")
    public void mustNotSaveAssociateWithNullCpf() {
        SaveAssociateRequest invalidRequest = associateBuilder.buildSaveAssociateWithNullCpf();

        associateService.saveAssociate(Utils.convertSaveAssociateRequestToJson(invalidRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body(containsString("CPF não pode ser nulo."))
        ;
    }

    @Test
    @Tag("all")
    @Description("Deve não salvar associado com data de nascimento nulo")
    public void mustNotSaveAssociateWithNullBirthDate() {
        SaveAssociateRequest invalidRequest = associateBuilder.buildSaveAssociateWithNullBirthDate();

        associateService.saveAssociate(Utils.convertSaveAssociateRequestToJson(invalidRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body(containsString("Data de nascimento não pode ser nula."))
        ;
    }

    @Test
    @Tag("all")
    @Description("Deve não salvar associado com telefone nulo")
    public void mustNotSaveAssociateWithNullPhone() {
        SaveAssociateRequest invalidRequest = associateBuilder.buildSaveAssociateWithNullPhone();

        associateService.saveAssociate(Utils.convertSaveAssociateRequestToJson(invalidRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body(containsString("Telefone não pode ser nulo."))
        ;
    }

    @Test
    @Tag("all")
    @Description("Deve não salvar associado com email nulo")
    public void mustNotSaveAssociateWithNullEmail() {
        SaveAssociateRequest invalidRequest = associateBuilder.buildSaveAssociateWithNullEmail();

        associateService.saveAssociate(Utils.convertSaveAssociateRequestToJson(invalidRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body(containsString("Email não pode ser nulo."))
        ;
    }

    @Test
    @Tag("all")
    @Description("Deve não salvar associado com profissão null")
    public void mustNotSaveAssociateWithNullProfession() {
        SaveAssociateRequest invalidRequest = associateBuilder.buildSaveAssociateWithNullProfession();

        associateService.saveAssociate(Utils.convertSaveAssociateRequestToJson(invalidRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body(containsString("Profissão não pode ser nulo."))
        ;
    }

    @Test
    @Tag("all")
    @Description("Deve não salvar associado com salário nulo")
    public void mustNotSaveAssociateWithNullSalary() {
        SaveAssociateRequest invalidRequest = associateBuilder.buildSaveAssociateWithNullSalary();

        associateService.saveAssociate(Utils.convertSaveAssociateRequestToJson(invalidRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body(containsString("Salário não pode ser nulo."))
        ;
    }

}
