package ar.edi.itn.dds.k3003.model;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.facades.FachadaViandas;
import ar.edu.utn.dds.k3003.facades.dtos.EstadoTrasladoEnum;
import ar.edu.utn.dds.k3003.facades.dtos.EstadoViandaEnum;
import ar.edu.utn.dds.k3003.facades.dtos.TrasladoDTO;
import ar.edu.utn.dds.k3003.facades.dtos.ViandaDTO;
import ar.edu.utn.dds.k3003.facades.exceptions.TrasladoNoAsignableException;
import ar.edu.utn.dds.k3003.model.Ruta;
import ar.edu.utn.dds.k3003.repositories.RutaMapper;
import ar.edu.utn.dds.k3003.repositories.RutaRepository;
import ar.edu.utn.dds.k3003.repositories.TrasladoMapper;
import ar.edu.utn.dds.k3003.repositories.TrasladoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class FachadaTest2 {

    @Mock
    private FachadaViandas fachadaViandas;

    @Mock
    private RutaRepository rutaRepository;

    @Mock
    private TrasladoRepository trasladoRepository;

    @Mock
    private TrasladoMapper trasladoMapper;

    @Mock
    private RutaMapper rutaMapper;

    private Fachada fachada;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        fachada = new Fachada(trasladoMapper, trasladoRepository, rutaMapper, rutaRepository);
        fachada.setViandasProxy(fachadaViandas);
    }

    @Test
    void testAsignarTraslado() throws TrasladoNoAsignableException, ar.edu.utn.dds.k3003.facades.exceptions.TrasladoNoAsignableException {

        TrasladoDTO trasladoDTO = new TrasladoDTO("QR123", 1, 2);
        ViandaDTO viandaDTO = new ViandaDTO("QR123", LocalDateTime.now(), EstadoViandaEnum.EN_TRASLADO, 1L, 1);
        List<Ruta> rutasPosibles = new ArrayList<>();
        rutasPosibles.add(new Ruta(1L, 1, 2));


        when(fachadaViandas.buscarXQR(anyString())).thenReturn(viandaDTO);
        when(rutaRepository.findByHeladeras(anyInt(), anyInt())).thenReturn(rutasPosibles);


        TrasladoDTO resultado = fachada.asignarTraslado(trasladoDTO);


        assertNotNull(resultado);
        assertEquals("QR123", resultado.getQrVianda());
        assertEquals(1, resultado.getHeladeraOrigen());
        assertEquals(2, resultado.getHeladeraDestino());
        assertEquals(EstadoTrasladoEnum.ASIGNADO, resultado.getStatus());
    }

    @Test
    void testFachadaViandasInitialization() {
        assertNotNull(fachadaViandas);
    }
}
