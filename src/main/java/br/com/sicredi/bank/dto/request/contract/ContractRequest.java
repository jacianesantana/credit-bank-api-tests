package br.com.sicredi.bank.dto.request.contract;

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
public class ContractRequest {

    private Long idAssociate;
    private ProductType productType;
    private Integer numberOfInstallments;
    private BigDecimal value;

}
