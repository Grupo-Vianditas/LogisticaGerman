package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.facades.dtos.EstadoTrasladoEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

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
@Table(name = "traslados")
public class Traslado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    //TODO : mapear esto
    private final String qrVianda;
    private final Ruta ruta;
    private EstadoTrasladoEnum estado;
    private final LocalDateTime fechaCreacion;
    private final LocalDateTime fechaTraslado;

    public Traslado(String qrVianda, Ruta ruta, EstadoTrasladoEnum estado, LocalDateTime fechaTraslado) {
        this.qrVianda = qrVianda;
        this.ruta = ruta;
        this.estado = estado;
        this.fechaCreacion = LocalDateTime.now();
        this.fechaTraslado = fechaTraslado;
    }


}