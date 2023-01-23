package br.com.sicredi.bank.builder.account;

import br.com.sicredi.bank.dto.request.account.AccountRequest;

public class AccountBuilder {

    public AccountRequest buildAccountRequest() {
        return AccountRequest.builder()
                .agency(1)
                .number(1)
                .build();
    }

    public AccountRequest buildAccountWithNullFields() {
        AccountRequest invalidAccount = buildAccountRequest();
        invalidAccount.setAgency(null);
        invalidAccount.setNumber(null);

        return invalidAccount;
    }

}
