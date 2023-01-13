package br.com.sicredi.bank.dto.request.associate;

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
public class SaveAssociateRequest {

    private String name;
    private String cpf;
    private LocalDate birthDate;
    private String profession;
    private BigDecimal salary;

}
