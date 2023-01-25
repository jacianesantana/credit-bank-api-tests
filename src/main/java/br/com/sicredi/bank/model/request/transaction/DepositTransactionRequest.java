package br.com.sicredi.bank.model.request.transaction;

import br.com.sicredi.bank.model.request.account.AccountRequest;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties
public class DepositTransactionRequest {

    private AccountRequest creditAccount;
    private BigDecimal value;

}
