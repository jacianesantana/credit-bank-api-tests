package br.com.sicredi.bank.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties
public class AssociateEntity {

    private Long id;
    private String name;
    private String cpf;
    private String birthDate;
    private String phone;
    private String email;
    private String profession;
    private BigDecimal salary;
    private String lastPaycheck;
    private Set<AccountEntity> accountSet;
    private Set<ContractEntity> contractSet;

}
