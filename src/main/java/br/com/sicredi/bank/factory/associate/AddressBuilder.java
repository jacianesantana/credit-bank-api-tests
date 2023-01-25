package br.com.sicredi.bank.factory.associate;

import br.com.sicredi.bank.model.request.address.AddressRequest;
import net.datafaker.Faker;
import org.apache.commons.lang3.StringUtils;

public class AddressBuilder {

    Faker faker = new Faker();

    public AddressRequest buildAddressRequest() {
        return AddressRequest.builder()
                .zipCode(faker.address().zipCode())
                .streetName(faker.address().streetName())
                .number(faker.address().buildingNumber())
                .city(faker.address().city())
                .state(faker.address().state())
                .country(faker.address().country())
                .build();
    }

    public AddressRequest buildAddressWithEmptyFields() {
        AddressRequest invalidAddress = buildAddressRequest();
        invalidAddress.setZipCode(StringUtils.EMPTY);
        invalidAddress.setStreetName(StringUtils.EMPTY);
        invalidAddress.setNumber(StringUtils.EMPTY);
        invalidAddress.setCity(StringUtils.EMPTY);
        invalidAddress.setState(StringUtils.EMPTY);
        invalidAddress.setCountry(StringUtils.EMPTY);

        return invalidAddress;
    }

    public AddressRequest buildAddressWithNullFields() {
        AddressRequest invalidAddress = buildAddressRequest();
        invalidAddress.setZipCode(null);
        invalidAddress.setStreetName(null);
        invalidAddress.setNumber(null);
        invalidAddress.setCity(null);
        invalidAddress.setState(null);
        invalidAddress.setCountry(null);

        return invalidAddress;
    }

    public AddressRequest buildUpdateAddressRequest() {
        return AddressRequest.builder()
                .zipCode(faker.address().zipCode())
                .streetName(faker.address().streetName())
                .number(faker.address().buildingNumber())
                .city(faker.address().city())
                .state(faker.address().state())
                .country(faker.address().country())
                .build();
    }

    public AddressRequest buildUpdateAddressWithEmptyFields() {
        AddressRequest invalidAddress = buildAddressRequest();
        invalidAddress.setZipCode(StringUtils.EMPTY);
        invalidAddress.setStreetName(StringUtils.EMPTY);
        invalidAddress.setNumber(StringUtils.EMPTY);
        invalidAddress.setCity(StringUtils.EMPTY);
        invalidAddress.setState(StringUtils.EMPTY);
        invalidAddress.setCountry(StringUtils.EMPTY);

        return invalidAddress;
    }

    public AddressRequest buildUpdateAddressWithNullFields() {
        AddressRequest invalidAddress = buildUpdateAddressRequest();
        invalidAddress.setZipCode(null);
        invalidAddress.setStreetName(null);
        invalidAddress.setNumber(null);
        invalidAddress.setCity(null);
        invalidAddress.setState(null);
        invalidAddress.setCountry(null);

        return invalidAddress;
    }

}
