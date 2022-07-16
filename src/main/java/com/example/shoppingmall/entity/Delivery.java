package com.example.shoppingmall.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery extends BaseTimeEntity{
    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @Embedded
    private Address address;

    public Delivery(Address address) {
        this.address = address;
    }

    public void changeAddress(Address address) {
        this.address = address;
    }
}
