package com.druiz.fullstack.back.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "roles")
@Data
@NoArgsConstructor
public class Role {
    @Id
    private String id;
    private String name;

    // constructores, getters y setters
}
