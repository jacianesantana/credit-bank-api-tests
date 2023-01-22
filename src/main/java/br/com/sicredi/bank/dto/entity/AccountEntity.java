package br.com.sicredi.bank.dto.entity;

import br.com.sicredi.bank.dto.enums.AccountType;
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
public class AccountEntity {

    private Long id;
    private AssociateEntity associate;
    private AccountType type;
    private Integer agency;
    private Integer number;
    private BigDecimal balance;
    private Set<TransactionEntity> creditTransactionSet;
    private Set<TransactionEntity> debitTransactionSet;

}
