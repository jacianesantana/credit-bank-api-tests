package br.com.sicredi.bank.dto.entity.product;

import br.com.sicredi.bank.dto.entity.contract.ContractEntity;
import br.com.sicredi.bank.dto.enums.ProductType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties
public class ProductEntity {

    private Long id;
    private ProductType type;
    private Integer taxes;
    private Set<ContractEntity> contractSet;

}
