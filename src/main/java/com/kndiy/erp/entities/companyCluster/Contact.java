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
@Table(name = "contacts")
@NoArgsConstructor
@Data
@AllArgsConstructor
public class Contact implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_contact")
    private Integer idContact;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_address")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Address address;
    @NotBlank(message = "Contact Name is required!")
    @Column(name = "contact_name")
    private String contactName;
    @Column(name = "position")
    private String position;
    @NotBlank(message = "At least one phone number should be inputted")
    @Column(name = "phone1")
    private String phone1;
    @Column(name = "phone2")
    private String phone2;
    @Column(name = "email")
    private String email;
    @Column(name = "bank_account")
    private String bankAccount;
    @Override
    public String toString() {
        return idContact.toString();
    }
}

