package br.com.sicredi.bank.builder.associate;

import br.com.sicredi.bank.dto.request.associate.SaveAssociateRequest;
import br.com.sicredi.bank.dto.request.associate.UpdateAssociateRequest;
import br.com.sicredi.bank.dto.response.associate.UpdateAssociateResponse;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AssociateBuilder {

    public SaveAssociateRequest buildSaveAssociateRequest() {
        return SaveAssociateRequest.builder()
                .name("anyName")
                .cpf("99999999999")
                .birthDate(LocalDate.now().minusYears(18))
                .profession("anyProfession")
                .salary(BigDecimal.valueOf(10000))
                .build();
    }

    public SaveAssociateRequest buildSaveAssociateWithInvalidSalary() {
        SaveAssociateRequest invalidAssociate = buildSaveAssociateRequest();
        invalidAssociate.setSalary(BigDecimal.valueOf(1000));

        return invalidAssociate;
    }

    public SaveAssociateRequest buildSaveAssociateWithInvalidBirthDate() {
        SaveAssociateRequest invalidAssociate = buildSaveAssociateRequest();
        invalidAssociate.setBirthDate(LocalDate.now().minusYears(17));

        return invalidAssociate;
    }

    public SaveAssociateRequest buildSaveAssociateWithEmptyFields() {
        SaveAssociateRequest invalidAssociate = buildSaveAssociateRequest();
        invalidAssociate.setName("");
        invalidAssociate.setCpf("");
        invalidAssociate.setProfession("");

        return invalidAssociate;
    }

    public UpdateAssociateRequest buildUpdateAssociateRequest() {
        return UpdateAssociateRequest.builder()
                .profession("Profissão atualizada")
                .salary(BigDecimal.valueOf(5500))
                .build();
    }

    public UpdateAssociateRequest buildUpdateAssociateWithInvalidSalary() {
        UpdateAssociateRequest invalidAssociate = buildUpdateAssociateRequest();
        invalidAssociate.setSalary(BigDecimal.valueOf(1000));

        return invalidAssociate;
    }

    public UpdateAssociateRequest buildUpdateAssociateWithEmptyFields() {
        UpdateAssociateRequest invalidAssociate = buildUpdateAssociateRequest();
        invalidAssociate.setProfession("");

        return invalidAssociate;
    }

}
