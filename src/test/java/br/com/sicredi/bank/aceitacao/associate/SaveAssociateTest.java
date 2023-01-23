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

import static br.com.sicredi.bank.dto.Constantes.*;
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
                    .body(containsString(ASSOCIATE_BUSINESS_CPF_ERROR))
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
                    .body(containsString(ASSOCIATE_BUSINESS_AGE_ERROR))
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
                    .body(containsString(SALARY_INVALID))
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
                    .body(containsString(CPF_INVALID))
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
                    .body(containsString(EMAIL_INVALID))
        ;
    }

    @Test
    @Tag("all")
    @Description("Deve não salvar associado sem preencher campos obrigatórios")
    public void mustNotSaveAssociateWithEmptyFields() {
        SaveAssociateRequest invalidRequest = associateBuilder.buildSaveAssociateWithEmptyFields();

        associateService.saveAssociate(Utils.convertSaveAssociateRequestToJson(invalidRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body(containsString("Nome não pode ser vazio."))
                    .body(containsString("CPF não pode ser vazio."))
                    .body(containsString("CPF inválido."))
                    .body(containsString("Telefone não pode ser vazio."))
                    .body(containsString("Email não pode ser vazio."))
                    .body(containsString("Email inválido."))
                    .body(containsString("Profissão não pode ser vazio."))
        ;
    }

    @Test
    @Tag("all")
    @Description("Deve não salvar associado com campos nulos")
    public void mustNotSaveAssociateWithNullFields() {
        SaveAssociateRequest invalidRequest = associateBuilder.buildSaveAssociateWithNullFields();

        associateService.saveAssociate(Utils.convertSaveAssociateRequestToJson(invalidRequest))
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body(containsString("Nome não pode ser nulo."))
                    .body(containsString("CPF não pode ser nulo."))
                    .body(containsString("Data de nascimento não pode ser nula."))
                    .body(containsString("Telefone não pode ser nulo."))
                    .body(containsString("Email não pode ser nulo."))
                    .body(containsString("Profissão não pode ser nulo."))
                    .body(containsString("Salário não pode ser nulo."))
        ;
    }

}
