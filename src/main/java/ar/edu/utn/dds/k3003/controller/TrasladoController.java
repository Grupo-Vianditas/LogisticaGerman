package ar.edu.utn.dds.k3003.controller;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.facades.dtos.EstadoTrasladoEnum;
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

    public void actualizarEstadoTraslado(Context context) {
        try {
            Long id = context.pathParamAsClass("id", Long.class).get();
            String nuevoEstadoStr = context.bodyAsClass(PatchRequest.class).getStatus();
            EstadoTrasladoEnum nuevoEstado = EstadoTrasladoEnum.valueOf(nuevoEstadoStr);

            TrasladoDTO trasladoActualizado = fachada.actualizarEstadoTraslado(id, nuevoEstado);
            context.json(trasladoActualizado);
            context.status(200);
        } catch (NoSuchElementException e) {
            context.status(404).result("Traslado no encontrado");
        } catch (IllegalArgumentException e) {
            context.status(400).result("Estado de traslado no válido");
        } catch (Exception e) {
            context.status(500).result("Error interno del servidor: " + e.getMessage());
        }
    }

    private static class PatchRequest {
        private String status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}