package br.com.sicredi.bank.model.response.transaction;

import br.com.sicredi.bank.model.response.account.AccountResponse;
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
public class TransactionResponse {

    private AccountResponse account;
    private BigDecimal newBalance;

}
