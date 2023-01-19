package br.com.sicredi.bank.dto.response.associate;

import br.com.sicredi.bank.dto.response.account.AccountResponse;
import br.com.sicredi.bank.dto.response.contract.ListContractsResponse;
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
public class FindAssociateResponse {

    private Long id;
    private String name;
    private String cpf;
    private String birthDate;
    private String profession;
    private BigDecimal salary;
    private String lastPaycheck;
    private List<AccountResponse> accounts;
    private List<ListContractsResponse> contracts;

}
