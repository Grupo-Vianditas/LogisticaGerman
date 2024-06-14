package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.facades.dtos.EstadoTrasladoEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class Traslado {
    private Long id;
    private final String qrVianda;
    private final Ruta ruta;
    private EstadoTrasladoEnum status;
    private final LocalDateTime fechaCreacion;
    private final LocalDateTime fechaTraslado;
    private String colaboladorId;


    public Traslado() {
        this.qrVianda = null;
        this.ruta = null;
        this.status = null;
        this.fechaCreacion = null;
        this.fechaTraslado = null;
    }

    public Traslado(String qrVianda, Ruta ruta, EstadoTrasladoEnum status, LocalDateTime fechaTraslado) {
        this.qrVianda = qrVianda;
        this.ruta = ruta;
        this.status = status;
        this.fechaCreacion = LocalDateTime.now();
        this.fechaTraslado = fechaTraslado;
    }

}