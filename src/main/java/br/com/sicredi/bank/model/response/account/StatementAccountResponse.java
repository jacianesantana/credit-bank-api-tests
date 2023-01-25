package br.com.sicredi.bank.model.response.account;

import br.com.sicredi.bank.model.enums.AccountType;
import br.com.sicredi.bank.model.response.transaction.StatementTransactionResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties
public class StatementAccountResponse {

    private AccountType type;
    private Integer agency;
    private Integer number;
    private BigDecimal balance;
    private List<StatementTransactionResponse> transactions;

}
