package com.kndiy.erp.entities.companyCluster;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_address")
    private Integer idAddress;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_company", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Company company;
    @NotBlank(message = "Address Name must not be blank!")
    @Column(name = "address_name")
    private String addressName;
    @NotBlank(message = "Address Type must not be blank!")
    @Column(name = "address_type")
    private String addressType;
    @NotBlank(message = "Address in English must not be blank!")
    @Column(name = "address_en")
    private String addressEn;
    @NotBlank(message = "Address in Vietnamese must not be blank!")
    @Column(name = "address_vn")
    private String addressVn;
    @Column(name = "tax_code")
    private String taxCode;
    @Column(name = "representative")
    private String representative;
    @Column(name = "distance")
    private Float distance;
    @Column(name = "outside_city")
    private Boolean outsideCity;

    @Override
    public String toString() {
        return idAddress.toString();
    }
}
