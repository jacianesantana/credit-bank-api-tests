package br.com.sicredi.bank.model.response.transaction;

import br.com.sicredi.bank.model.enums.TransactionType;
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
public class StatementTransactionResponse {

    private TransactionType type;
    private BigDecimal value;
    private String createdAt;

}
