package com.example.domain;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Min;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@Entity
@NoArgsConstructor
@DiscriminatorValue(value = "regular_user")
public class RegularClient extends Client {
    @Column(name = "loyalty_counter")
    @Min(0)
    private Integer loyaltyCounter;

    public RegularClient(String firstName, String lastName, String email) {
        super(firstName, lastName, email);
        this.loyaltyCounter = 0;
    }

    public RegularClient(String firstName, String lastName, String email, Integer loyaltyCounter) {
        super(firstName, lastName, email);
        this.loyaltyCounter = loyaltyCounter;
    }

    @Override
    public double getDiscount() {
        double discount = loyaltyCounter * 0.01;
        return loyaltyCounter <= 20 ? discount : 0.2;
    }

    @Override
    public String getTypeName() {
        return "RegularUser";
    }

    public void incrementLoyalty() {
        loyaltyCounter++;
    }

    public void decrementLoyalty() {
        loyaltyCounter--;
    }

}
