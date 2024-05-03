package ar.edu.utn.dds.k3003.facades;

import ar.edu.utn.dds.k3003.facades.dtos.RutaDTO;
import ar.edu.utn.dds.k3003.facades.dtos.TrasladoDTO;
import ar.edu.utn.dds.k3003.facades.exceptions.TrasladoNoAsignableException;

import java.util.List;

public interface FachadaLogistica {

    RutaDTO agregar(RutaDTO rutaDTO);

    TrasladoDTO buscarXId(Long trasladoId);

    TrasladoDTO asignarTraslado(TrasladoDTO trasladoDTO) throws TrasladoNoAsignableException;

    List<TrasladoDTO> trasladosDeColaborador(Long colaboradorId, Integer mes, Integer anio);

    void setHeladerasProxy(FachadaHeladeras fachadaHeladeras);

    void setViandasProxy(FachadaViandas fachadaViandas);

    void trasladoRetirado(Long trasladoId);

    void trasladoDepositado(Long trasladoId);


}