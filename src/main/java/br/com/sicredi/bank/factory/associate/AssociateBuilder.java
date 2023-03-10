package br.com.sicredi.bank.factory.associate;

import br.com.sicredi.bank.model.request.associate.SaveAssociateRequest;
import br.com.sicredi.bank.model.request.associate.UpdateAssociateContactRequest;
import br.com.sicredi.bank.model.request.associate.UpdateAssociatePaycheckRequest;
import net.datafaker.Faker;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;

import java.math.BigDecimal;

public class AssociateBuilder {

    Faker faker = new Faker();

    public SaveAssociateRequest buildSaveAssociateRequest() {
        return SaveAssociateRequest.builder()
                .name(faker.name().fullName())
                .cpf(faker.cpf().valid(false))
                .birthDate(LocalDate.now().minusYears(18).toString())
                .phone(faker.phoneNumber().cellPhone())
                .email(faker.internet().emailAddress())
                .profession(faker.company().profession())
                .salary(BigDecimal.valueOf(10000).setScale(2))
                .build();
    }

    public SaveAssociateRequest buildSaveAssociateWithInvalidBirthDate() {
        SaveAssociateRequest invalidAssociate = buildSaveAssociateRequest();
        invalidAssociate.setBirthDate(LocalDate.now().minusYears(17).toString());

        return invalidAssociate;
    }

    public SaveAssociateRequest buildSaveAssociateWithInvalidSalary() {
        SaveAssociateRequest invalidAssociate = buildSaveAssociateRequest();
        invalidAssociate.setSalary(BigDecimal.valueOf(-1));

        return invalidAssociate;
    }

    public SaveAssociateRequest buildSaveAssociateWithInvalidCpf() {
        SaveAssociateRequest invalidAssociate = buildSaveAssociateRequest();
        invalidAssociate.setCpf("12345678910");

        return invalidAssociate;
    }
    public SaveAssociateRequest buildSaveAssociateWithInvalidEmail() {
        SaveAssociateRequest invalidAssociate = buildSaveAssociateRequest();
        invalidAssociate.setEmail("anyEmail");

        return invalidAssociate;
    }

    public SaveAssociateRequest buildSaveAssociateWithEmptyFields() {
        SaveAssociateRequest invalidAssociate = buildSaveAssociateRequest();
        invalidAssociate.setName(StringUtils.EMPTY);
        invalidAssociate.setCpf(StringUtils.EMPTY);
        invalidAssociate.setPhone(StringUtils.EMPTY);
        invalidAssociate.setEmail(StringUtils.EMPTY);
        invalidAssociate.setProfession(StringUtils.EMPTY);

        return invalidAssociate;
    }

    public SaveAssociateRequest buildSaveAssociateWithNullFields() {
        SaveAssociateRequest invalidAssociate = buildSaveAssociateRequest();
        invalidAssociate.setName(null);
        invalidAssociate.setCpf(null);
        invalidAssociate.setBirthDate(null);
        invalidAssociate.setPhone(null);
        invalidAssociate.setEmail(null);
        invalidAssociate.setProfession(null);
        invalidAssociate.setSalary(null);

        return invalidAssociate;
    }

    public UpdateAssociateContactRequest buildUpdateAssociateContactRequest() {
        return UpdateAssociateContactRequest.builder()
                .phone(faker.phoneNumber().cellPhone())
                .email(faker.internet().emailAddress())
                .build();
    }

    public UpdateAssociateContactRequest buildUpdateAssociateContactWithEmptyFields() {
        UpdateAssociateContactRequest invalidAssociate = buildUpdateAssociateContactRequest();
        invalidAssociate.setPhone(StringUtils.EMPTY);
        invalidAssociate.setEmail(StringUtils.EMPTY);

        return invalidAssociate;
    }

    public UpdateAssociateContactRequest buildUpdateAssociateContactWithNullFields() {
        UpdateAssociateContactRequest invalidAssociate = buildUpdateAssociateContactRequest();
        invalidAssociate.setPhone(null);
        invalidAssociate.setEmail(null);

        return invalidAssociate;
    }

    public UpdateAssociatePaycheckRequest buildUpdateAssociatePaycheckRequest() {
        return UpdateAssociatePaycheckRequest.builder()
                .profession("Profiss??o atualizada")
                .salary(BigDecimal.valueOf(5500).setScale(2))
                .build();
    }

    public UpdateAssociatePaycheckRequest buildUpdateAssociateWithInvalidSalary() {
        UpdateAssociatePaycheckRequest invalidAssociate = buildUpdateAssociatePaycheckRequest();
        invalidAssociate.setSalary(BigDecimal.valueOf(-1));

        return invalidAssociate;
    }

    public UpdateAssociatePaycheckRequest buildUpdateAssociateWithEmptyProfession() {
        UpdateAssociatePaycheckRequest invalidAssociate = buildUpdateAssociatePaycheckRequest();
        invalidAssociate.setProfession(StringUtils.EMPTY);

        return invalidAssociate;
    }

}
