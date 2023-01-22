package br.com.sicredi.bank.dto.response.transaction;

import br.com.sicredi.bank.dto.enums.TransactionType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties
public class StatementTransactionResponse {

    private TransactionType type;
    private BigDecimal value;
    private LocalDateTime createdAt;

}