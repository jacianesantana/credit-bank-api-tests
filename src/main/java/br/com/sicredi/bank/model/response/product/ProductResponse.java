package br.com.sicredi.bank.model.response.product;

import br.com.sicredi.bank.model.enums.ProductType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties
public class ProductResponse {

    private ProductType type;
    private Integer taxes;
    private String firstPaymentDate;
    private List<Integer> plotsAvailable;
    private BigDecimal minValue;
    private BigDecimal maxValue;

}
