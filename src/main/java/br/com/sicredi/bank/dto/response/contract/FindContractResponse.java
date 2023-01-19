package br.com.sicredi.bank.dto.response.contract;

import br.com.sicredi.bank.dto.entity.associate.AssociateEntity;
import br.com.sicredi.bank.dto.entity.product.ProductEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

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
    private LocalDate hireDate;
    private LocalDate expirationDate;
    private Integer installmentsPaid;
    private Integer installmentsRemaining;
    private LocalDate firstPaymentDate;

}
