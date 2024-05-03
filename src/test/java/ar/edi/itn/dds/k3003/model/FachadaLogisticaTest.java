package ar.edi.itn.dds.k3003.model;

import ar.edu.utn.dds.k3003.facades.dtos.RutaDTO;
import ar.edu.utn.dds.k3003.app.Fachada;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;

public class FachadaLogisticaTest {

    @Test
    void testAgregar() {

        Fachada fachada = new Fachada();
        RutaDTO rutaDTO = new RutaDTO(1L, 100, 200);
        RutaDTO resultado = fachada.agregar(rutaDTO);

        assertEquals(rutaDTO.getColaboradorId(), resultado.getColaboradorId());
        assertEquals(rutaDTO.getHeladeraIdOrigen(), resultado.getHeladeraIdOrigen());
        assertEquals(rutaDTO.getHeladeraIdDestino(), resultado.getHeladeraIdDestino());
}
}