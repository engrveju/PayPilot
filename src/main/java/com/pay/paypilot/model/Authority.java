package com.pay.paypilot.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "authorities")
public class Authority {
    @Id    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToMany(mappedBy = "authorities")
    private Collection<Role> roles;
    public Authority(String name) {
        this.name = name;
    }
}