package br.com.sicredi.bank.model.response.associate;

import br.com.sicredi.bank.model.response.account.AccountResponse;
import br.com.sicredi.bank.model.response.address.AddressResponse;
import br.com.sicredi.bank.model.response.contract.ListContractsResponse;
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
    private String phone;
    private String email;
    private String profession;
    private BigDecimal salary;
    private String lastPaycheck;
    private List<AddressResponse> addresses;
    private List<AccountResponse> accounts;
    private List<ListContractsResponse> contracts;

}
