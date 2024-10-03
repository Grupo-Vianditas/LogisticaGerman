package ar.edu.utn.dds.k3003.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDateTime;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "rutas")
public class Ruta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
//TODO : mapear esto
    private Long colaboradorId;
    private Integer heladeraIdOrigen;
    private Integer heladeraIdDestino;

    private LocalDateTime fechaCreacion;
    private Boolean activo;

    public Ruta() {

    }

    public Ruta(Long colaboradorId, Integer heladeraIdOrigen, Integer heladeraIdDestino) {
        this.colaboradorId = colaboradorId;
        this.heladeraIdOrigen = heladeraIdOrigen;
        this.heladeraIdDestino = heladeraIdDestino;
        this.fechaCreacion = LocalDateTime.now();
        this.activo = true;

    }

}