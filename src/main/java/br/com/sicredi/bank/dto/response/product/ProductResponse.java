package br.com.sicredi.bank.dto.response.product;

import br.com.sicredi.bank.dto.enums.ProductType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties
public class ProductResponse {

    private ProductType type;
    private Integer taxes;
    private LocalDate firstPaymentDate;
    private List<Integer> plotsAvailable;
    private BigDecimal minValue;
    private BigDecimal maxValue;

}
