package br.com.sicredi.bank.dto.response.contract;

import br.com.sicredi.bank.dto.enums.ProductType;
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
public class SaveContractResponse {

    private Long id;
    private ProductType productType;
    private BigDecimal value;
    private Boolean paidOff;
    private String hireDate;
    private String expirationDate;
    private Integer installmentsPaid;
    private Integer installmentsRemaining;
    private String firstPaymentDate;

}
