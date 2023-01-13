package br.com.sicredi.bank.dto.response.account;

import br.com.sicredi.bank.dto.enums.AccountType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties
public class AccountResponse {

    private AccountType type;
    private Integer agency;
    private Integer number;

}
