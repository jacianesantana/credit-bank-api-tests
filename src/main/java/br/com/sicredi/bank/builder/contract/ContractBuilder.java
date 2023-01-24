package br.com.sicredi.bank.builder.contract;

import br.com.sicredi.bank.dto.enums.ProductType;
import br.com.sicredi.bank.dto.request.contract.ContractRequest;

import java.math.BigDecimal;

public class ContractBuilder {

    public ContractRequest buildContractRequest() {
        return ContractRequest.builder()
                .idAssociate(null)
                .productType(ProductType.PERSONAL)
                .numberOfInstallments(24)
                .value(BigDecimal.valueOf(10000).setScale(2))
                .build();
    }

    public ContractRequest buildContractWithInvalidNumberOfInstallments() {
        ContractRequest invalidContract = buildContractRequest();
        invalidContract.setNumberOfInstallments(0);

        return invalidContract;
    }

    public ContractRequest buildContractWithInvalidValue() {
        ContractRequest invalidContract = buildContractRequest();
        invalidContract.setValue(BigDecimal.ZERO);

        return invalidContract;
    }

    public ContractRequest buildContractWithNullFields() {
        ContractRequest invalidContract = buildContractRequest();
        invalidContract.setProductType(null);
        invalidContract.setNumberOfInstallments(null);
        invalidContract.setValue(null);

        return invalidContract;
    }

}
