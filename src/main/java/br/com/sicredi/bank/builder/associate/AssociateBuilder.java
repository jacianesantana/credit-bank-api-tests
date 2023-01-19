package br.com.sicredi.bank.builder.associate;

import br.com.sicredi.bank.dto.request.associate.SaveAssociateRequest;
import br.com.sicredi.bank.dto.request.associate.UpdateAssociatePaycheckRequest;
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
                .salary(BigDecimal.valueOf(10000))
                .build();
    }

    public SaveAssociateRequest buildSaveAssociateWithInvalidBirthDate() {
        SaveAssociateRequest invalidAssociate = buildSaveAssociateRequest();
        invalidAssociate.setBirthDate(LocalDate.now().minusYears(17).toString());

        return invalidAssociate;
    }

    public SaveAssociateRequest buildSaveAssociateWithNullBirthDate() {
        SaveAssociateRequest invalidAssociate = buildSaveAssociateRequest();
        invalidAssociate.setBirthDate(null);

        return invalidAssociate;
    }

    public SaveAssociateRequest buildSaveAssociateWithInvalidSalary() {
        SaveAssociateRequest invalidAssociate = buildSaveAssociateRequest();
        invalidAssociate.setSalary(BigDecimal.valueOf(-1));

        return invalidAssociate;
    }

    public SaveAssociateRequest buildSaveAssociateWithNullSalary() {
        SaveAssociateRequest invalidAssociate = buildSaveAssociateRequest();
        invalidAssociate.setSalary(null);

        return invalidAssociate;
    }

    public SaveAssociateRequest buildSaveAssociateWithInvalidCpf() {
        SaveAssociateRequest invalidAssociate = buildSaveAssociateRequest();
        invalidAssociate.setCpf("12345678910");

        return invalidAssociate;
    }

    public SaveAssociateRequest buildSaveAssociateWithEmptyCpf() {
        SaveAssociateRequest invalidAssociate = buildSaveAssociateRequest();
        invalidAssociate.setCpf(StringUtils.EMPTY);

        return invalidAssociate;
    }

    public SaveAssociateRequest buildSaveAssociateWithNullCpf() {
        SaveAssociateRequest invalidAssociate = buildSaveAssociateRequest();
        invalidAssociate.setCpf(null);

        return invalidAssociate;
    }

    public SaveAssociateRequest buildSaveAssociateWithInvalidEmail() {
        SaveAssociateRequest invalidAssociate = buildSaveAssociateRequest();
        invalidAssociate.setEmail("anyEmail");

        return invalidAssociate;
    }

    public SaveAssociateRequest buildSaveAssociateWithEmptyEmail() {
        SaveAssociateRequest invalidAssociate = buildSaveAssociateRequest();
        invalidAssociate.setEmail(StringUtils.EMPTY);

        return invalidAssociate;
    }

    public SaveAssociateRequest buildSaveAssociateWithNullEmail() {
        SaveAssociateRequest invalidAssociate = buildSaveAssociateRequest();
        invalidAssociate.setEmail(null);

        return invalidAssociate;
    }

    public SaveAssociateRequest buildSaveAssociateWithEmptyName() {
        SaveAssociateRequest invalidAssociate = buildSaveAssociateRequest();
        invalidAssociate.setName(StringUtils.EMPTY);

        return invalidAssociate;
    }

    public SaveAssociateRequest buildSaveAssociateWithNullName() {
        SaveAssociateRequest invalidAssociate = buildSaveAssociateRequest();
        invalidAssociate.setName(null);

        return invalidAssociate;
    }

    public SaveAssociateRequest buildSaveAssociateWithEmptyPhone() {
        SaveAssociateRequest invalidAssociate = buildSaveAssociateRequest();
        invalidAssociate.setPhone(StringUtils.EMPTY);

        return invalidAssociate;
    }

    public SaveAssociateRequest buildSaveAssociateWithNullPhone() {
        SaveAssociateRequest invalidAssociate = buildSaveAssociateRequest();
        invalidAssociate.setPhone(null);

        return invalidAssociate;
    }

    public SaveAssociateRequest buildSaveAssociateWithEmptyProfession() {
        SaveAssociateRequest invalidAssociate = buildSaveAssociateRequest();
        invalidAssociate.setProfession(StringUtils.EMPTY);

        return invalidAssociate;
    }

    public SaveAssociateRequest buildSaveAssociateWithNullProfession() {
        SaveAssociateRequest invalidAssociate = buildSaveAssociateRequest();
        invalidAssociate.setProfession(null);

        return invalidAssociate;
    }

    public UpdateAssociatePaycheckRequest buildUpdateAssociateRequest() {
        return UpdateAssociatePaycheckRequest.builder()
                .profession("Profissão atualizada")
                .salary(BigDecimal.valueOf(5500))
                .build();
    }

    public UpdateAssociatePaycheckRequest buildUpdateAssociateWithInvalidSalary() {
        UpdateAssociatePaycheckRequest invalidAssociate = buildUpdateAssociateRequest();
        invalidAssociate.setSalary(BigDecimal.valueOf(-1));

        return invalidAssociate;
    }

    public UpdateAssociatePaycheckRequest buildUpdateAssociateWithEmptyProfession() {
        UpdateAssociatePaycheckRequest invalidAssociate = buildUpdateAssociateRequest();
        invalidAssociate.setProfession(StringUtils.EMPTY);

        return invalidAssociate;
    }

}
