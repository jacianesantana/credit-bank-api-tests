package br.com.sicredi.bank.dto.response.associate;

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
public class UpdateAssociatePaycheckResponse {

    private Long id;
    private String profession;
    private BigDecimal salary;
    private String lastPaycheck;

}
