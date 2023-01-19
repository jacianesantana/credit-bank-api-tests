package br.com.sicredi.bank.dto.entity.associate;

import br.com.sicredi.bank.dto.entity.account.AccountEntity;
import br.com.sicredi.bank.dto.entity.contract.ContractEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
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
    private LocalDate birthDate;
    private String profession;
    private BigDecimal salary;
    private LocalDate lastPaycheck;
    private Set<AccountEntity> accountSet;
    private Set<ContractEntity> contractSet;

}
