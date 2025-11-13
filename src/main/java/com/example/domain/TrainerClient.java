package com.example.domain;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@Entity
@NoArgsConstructor
@DiscriminatorValue(value = "trainer_user")
public class TrainerClient extends Client {
    @NotNull
    @Column(name = "is_partner")
    private Boolean isPartner;

    public TrainerClient(String firstName, String lastName, String email, Boolean isPartner) {
        super(firstName, lastName, email);
        this.isPartner = isPartner;
    }

    @Override
    public double getDiscount() {
        return isPartner ? 0.5 : 0.1;
    }

    @Override
    public String getTypeName() {
        return "TrainerUser";
    }

    @Override
    public String toString() {
        return super.toString().substring(0, super.toString().length() - 1) +
                ", isPartner=" + isPartner +
                '}';
    }
}
