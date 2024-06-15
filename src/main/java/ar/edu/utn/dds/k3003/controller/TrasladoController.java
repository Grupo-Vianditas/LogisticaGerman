package ar.edu.utn.dds.k3003.controller;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.facades.dtos.TrasladoDTO;
import ar.edu.utn.dds.k3003.facades.exceptions.TrasladoNoAsignableException;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.util.List;
import java.util.NoSuchElementException;

public class TrasladoController {

    private final Fachada fachada;

    public TrasladoController(Fachada fachada) {
        this.fachada = fachada;
    }

    public void asignar(Context context) {
        try {
            var trasladoDTO = this.fachada.asignarTraslado(context.bodyAsClass(TrasladoDTO.class));
            context.json(trasladoDTO);
            context.result("Traslado asignado correctamente");
        } catch (TrasladoNoAsignableException | NoSuchElementException e) {
            context.result(e.getLocalizedMessage());
            context.status(HttpStatus.BAD_REQUEST);
        }
    }

    public void obtener(Context context) {
        var id = context.pathParamAsClass("id", Long.class).get();
        try {
            var trasladoDTO = this.fachada.buscarXId(id);
            context.json(trasladoDTO);
            context.result("Traslado solicitado");
        } catch (NoSuchElementException ex) {
            context.result(ex.getLocalizedMessage());
            context.status(HttpStatus.NOT_FOUND);
        }
    }
    public void obtenerTrasladosPorColaboradorId(Context context) {
        try {
            Long colaboradorId = context.queryParamAsClass("id", Long.class).get();

            List<TrasladoDTO> traslados = fachada.trasladosDeColaboradorPorId(colaboradorId);
            context.json(traslados);
            context.status(200);
        } catch (NoSuchElementException e) {
            context.status(400).result("Colaborador no encontrado o no hay traslados para el colaborador especificado");
        } catch (Exception e) {
            context.status(500).result("Error interno del servidor: " + e.getMessage());
        }
    }

}