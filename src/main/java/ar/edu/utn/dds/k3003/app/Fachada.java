package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.facades.FachadaHeladeras;
import ar.edu.utn.dds.k3003.facades.FachadaViandas;
import ar.edu.utn.dds.k3003.facades.dtos.*;
import ar.edu.utn.dds.k3003.facades.exceptions.TrasladoNoAsignableException;
import ar.edu.utn.dds.k3003.model.Ruta;
import ar.edu.utn.dds.k3003.model.Traslado;
import ar.edu.utn.dds.k3003.repositories.RutaMapper;
import ar.edu.utn.dds.k3003.repositories.RutaRepository;
import ar.edu.utn.dds.k3003.repositories.TrasladoMapper;
import ar.edu.utn.dds.k3003.repositories.TrasladoRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;


public class Fachada implements ar.edu.utn.dds.k3003.facades.FachadaLogistica {

    private final RutaRepository rutaRepository;
    private final RutaMapper rutaMapper;
    private final TrasladoRepository trasladoRepository;
    private final TrasladoMapper trasladoMapper;
    private FachadaViandas fachadaViandas;
    private FachadaHeladeras fachadaHeladeras;



    public Fachada() {
        this.rutaRepository = new RutaRepository();
        this.rutaMapper = new RutaMapper();
        this.trasladoMapper = new TrasladoMapper();
        this.trasladoRepository = new TrasladoRepository();
    }

    public Fachada(TrasladoMapper trasladoMapper, TrasladoRepository trasladoRepository, RutaMapper rutaMapper, RutaRepository rutaRepository) {
        this.trasladoMapper = trasladoMapper;
        this.trasladoRepository = trasladoRepository;
        this.rutaRepository = rutaRepository;
        this.rutaMapper = rutaMapper;
    }

    @Override
    public RutaDTO agregar(RutaDTO rutaDTO) {
        Ruta ruta = new Ruta(rutaDTO.getColaboradorId(), rutaDTO.getHeladeraIdOrigen(), rutaDTO.getHeladeraIdDestino());
        ruta = this.rutaRepository.save(ruta);
        return rutaMapper.map(ruta);
    }

    @Override
    public TrasladoDTO buscarXId(Long aLong) throws NoSuchElementException {
        TrasladoDTO trasladoDTO = trasladoMapper.map(trasladoRepository.findById(aLong));
        if (trasladoDTO == null) {
            throw new NoSuchElementException("Traslado no encontrado");
        }
        return trasladoDTO;
    }

    @Override
    public TrasladoDTO asignarTraslado(TrasladoDTO trasladoDTO) throws TrasladoNoAsignableException {
        ViandaDTO viandaDTO = fachadaViandas.buscarXQR(trasladoDTO.getQrVianda());

        List<Ruta> rutasPosibles = this.rutaRepository.findByHeladeras(trasladoDTO.getHeladeraOrigen(),
                trasladoDTO.getHeladeraDestino());

        if (rutasPosibles.isEmpty()) {
            throw new TrasladoNoAsignableException("ERROR");
            // TODO : PUSE ERROR PORQUE CRESHEABA , CORREGIR EL COMENTARIO EN EL FUTURO A UN NOMBRE MAS REPRESENTATIVO
        }

        Collections.shuffle(rutasPosibles);
        Ruta ruta = rutasPosibles.get(0);

        Traslado traslado = trasladoRepository.save(new Traslado(viandaDTO.getCodigoQR(), ruta,
                EstadoTrasladoEnum.ASIGNADO, trasladoDTO.getFechaTraslado()));

        return this.trasladoMapper.map(traslado);
    }


    @Override
    public List<TrasladoDTO> trasladosDeColaborador(Long aLong, Integer integer, Integer integer1) {
        return null;
    }

    @Override
    public void setHeladerasProxy(FachadaHeladeras fachadaHeladeras) {
        this.fachadaHeladeras = fachadaHeladeras;
    }

    @Override
    public void setViandasProxy(FachadaViandas fachadaViandas) {
        this.fachadaViandas = fachadaViandas;
    }


    @Override
    public void trasladoRetirado(Long trasladoId) {
        Traslado traslado = trasladoRepository.findById(trasladoId);
        traslado.setStatus(EstadoTrasladoEnum.EN_VIAJE);
        trasladoRepository.save(traslado);

        String qrVianda = traslado.getQrVianda();
        Ruta ruta = traslado.getRuta();
        Integer heladeraOrigen = ruta.getHeladeraIdOrigen();
        LocalDateTime fechaActual = LocalDateTime.now();

        RetiroDTO retiroDTO = new RetiroDTO(qrVianda,null,heladeraOrigen);
        fachadaViandas.modificarEstado(qrVianda, EstadoViandaEnum.EN_TRASLADO);
        fachadaViandas.modificarHeladera(qrVianda,2);
        fachadaHeladeras.retirar(retiroDTO);

}

    @Override
    public void trasladoDepositado(Long trasladoId) {
        Traslado traslado = trasladoRepository.findById(trasladoId);
        traslado.setStatus(EstadoTrasladoEnum.ENTREGADO);
        trasladoRepository.save(traslado);
        fachadaViandas.modificarEstado(traslado.getQrVianda(), EstadoViandaEnum.DEPOSITADA);
    }



}