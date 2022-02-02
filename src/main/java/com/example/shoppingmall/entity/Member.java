package com.example.shoppingmall.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@ToString(of = {"id", "username", "password"}, callSuper = true)
public class Member extends BaseTimeEntity{
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true)
    private String username;
    private int password;

    @Embedded
    private Address address;

    public Member(String username, int password) {
        this(username, password, null);
    }

    public Member(String username, int password, Address address) {
        this.username = username;
        this.password = password;
        this.address = address;
    }
}
