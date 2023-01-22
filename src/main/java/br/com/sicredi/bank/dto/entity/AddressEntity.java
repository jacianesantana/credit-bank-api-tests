package br.com.sicredi.bank.dto.entity;

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
public class AddressEntity {

    private Long id;
    private AssociateEntity associate;
    private String zipCode;
    private String streetName;
    private String number;
    private String complement;
    private String city;
    private String state;
    private String country;

}
