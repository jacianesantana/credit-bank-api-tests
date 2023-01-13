package br.com.sicredi.bank.dto.response.associate;

import br.com.sicredi.bank.dto.response.account.AccountResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties
public class SaveAssociateResponse {

    private Long id;
    private String name;
    private String cpf;
    private List<AccountResponse> accounts;

}
