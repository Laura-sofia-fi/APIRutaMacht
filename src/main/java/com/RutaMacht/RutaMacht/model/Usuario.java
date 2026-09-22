package com.RutaMacht.RutaMacht.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString


public abstract class Usuario {

    private int id;
    private String nombre;
    private String Date;
    private String nose;

}
