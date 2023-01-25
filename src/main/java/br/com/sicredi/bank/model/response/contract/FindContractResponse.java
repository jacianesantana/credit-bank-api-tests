package br.com.sicredi.bank.model.response.contract;

import br.com.sicredi.bank.model.entity.AssociateEntity;
import br.com.sicredi.bank.model.entity.ProductEntity;
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
public class FindContractResponse {

    private Long id;
    private AssociateEntity associate;
    private ProductEntity product;
    private BigDecimal value;
    private Boolean paidOff;
    private String hireDate;
    private String expirationDate;
    private Integer installmentsPaid;
    private Integer installmentsRemaining;
    private String firstPaymentDate;

}
