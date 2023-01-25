package br.com.sicredi.bank.model.response.address;

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
public class AddressResponse {

    private Long id;
    private String zipCode;
    private String streetName;
    private String number;
    private String complement;
    private String city;
    private String state;
    private String country;

}
