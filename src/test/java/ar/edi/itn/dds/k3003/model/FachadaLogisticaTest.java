package ar.edi.itn.dds.k3003.model;


import ar.edu.utn.dds.k3003.facades.FachadaViandas;
import ar.edu.utn.dds.k3003.facades.dtos.RutaDTO;
import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.facades.dtos.TrasladoDTO;
import ar.edu.utn.dds.k3003.facades.exceptions.TrasladoNoAsignableException;
import ar.edu.utn.dds.k3003.repositories.RutaMapper;
import ar.edu.utn.dds.k3003.repositories.RutaRepository;
import ar.edu.utn.dds.k3003.repositories.TrasladoMapper;
import ar.edu.utn.dds.k3003.repositories.TrasladoRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void testBuscarXId_NoExisteTraslado() {

        TrasladoRepository trasladoRepository = new TrasladoRepository();
        TrasladoMapper trasladoMapper = new TrasladoMapper();
        RutaRepository rutaRepository = new RutaRepository();
        RutaMapper rutaMapper = new RutaMapper();

        Fachada fachada = new Fachada(trasladoMapper, trasladoRepository,rutaMapper,rutaRepository);

        assertThrows(NoSuchElementException.class, () -> fachada.buscarXId(1L));


    }

    @Test
    void testTrasladosDeColaborador() {
        Fachada fachada = new Fachada();
        Long colaboradorId = 1L;
        Integer heladeraOrigen = 100;
        Integer heladeraDestino = 200;

        List<TrasladoDTO> result = fachada.trasladosDeColaborador(colaboradorId, heladeraOrigen, heladeraDestino);

        assertNull(result);
    }


}