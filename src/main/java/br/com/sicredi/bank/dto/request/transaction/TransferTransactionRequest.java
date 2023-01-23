package br.com.sicredi.bank.dto.request.transaction;

import br.com.sicredi.bank.dto.request.account.AccountRequest;
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
public class TransferTransactionRequest {

    private AccountRequest debitAccount;
    private AccountRequest creditAccount;
    private BigDecimal value;

}
