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
import java.util.stream.Collectors;


public class Fachada implements ar.edu.utn.dds.k3003.facades.FachadaLogistica {

    private final RutaRepository rutaRepository;
    private final RutaMapper rutaMapper;

    private final TrasladoRepository trasladoRepository;
    private final TrasladoMapper trasladoMapper;

    //TODO:parte de implementacion de persistencia hacer clase RutaRepositoryImpl
    //private final RutaRepositoryImpl heladeraRepositoryImpl;




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

        var viandaDTO = fachadaViandas.buscarXQR(trasladoDTO.getQrVianda());

        var rutasPosibles =
                this.rutaRepository.findByHeladeras(
                        trasladoDTO.getHeladeraOrigen(), trasladoDTO.getHeladeraDestino());

        if (rutasPosibles.isEmpty()) {
            throw new TrasladoNoAsignableException(
                    String.format(
                            "No hay rutas para ir de %s a %s ",
                            trasladoDTO.getHeladeraOrigen(), trasladoDTO.getHeladeraDestino()));
        }

        Collections.shuffle(rutasPosibles);
        var ruta = rutasPosibles.get(0);

        var traslado =
                trasladoRepository.save(
                        new Traslado(
                                viandaDTO.getCodigoQR(),
                                ruta,
                                EstadoTrasladoEnum.ASIGNADO,
                                trasladoDTO.getFechaTraslado()));

        return this.trasladoMapper.map(traslado);
    }

/*
    @Override
    public List<TrasladoDTO> trasladosDeColaborador(Long colaboradorId, Integer mes, Integer anio) {
        List<Traslado> traslados = trasladoRepository.findByColaboradorIdAndFecha(colaboradorId, mes, anio);
        if (traslados.isEmpty()) {
            throw new NoSuchElementException("Colaborador no encontrado o no hay traslados en la fecha especificada");
        }
        return traslados.stream()
                .map(trasladoMapper::map)
                .collect(Collectors.toList());
    }
*/
    public List<TrasladoDTO> trasladosDeColaborador(Long colaboradorId, Integer mes, Integer anio) {
        List<Traslado> traslados;
        if (mes != null && anio != null) {
            traslados = trasladoRepository.findByColaboradorIdAndFecha(colaboradorId, mes, anio);
        } else {
            traslados = trasladoRepository.findByColaboradorId(colaboradorId);
        }
        if (traslados.isEmpty()) {
            throw new NoSuchElementException("Colaborador no encontrado o no hay traslados en la fecha especificada");
        }
        return traslados.stream()
                .map(trasladoMapper::map)
                .collect(Collectors.toList());
    }

    public List<TrasladoDTO> trasladosDeColaboradorPorId(Long colaboradorId) {
        List<Traslado> traslados = trasladoRepository.findByColaboradorId(colaboradorId);
        if (traslados.isEmpty()) {
            throw new NoSuchElementException("Colaborador no encontrado o no hay traslados para el colaborador especificado");
        }
        return traslados.stream()
                .map(trasladoMapper::map)
                .collect(Collectors.toList());
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
        traslado.setEstado(EstadoTrasladoEnum.EN_VIAJE);
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
        traslado.setEstado(EstadoTrasladoEnum.ENTREGADO);
        trasladoRepository.save(traslado);
        fachadaViandas.modificarEstado(traslado.getQrVianda(), EstadoViandaEnum.DEPOSITADA);
    }

    public TrasladoDTO actualizarEstadoTraslado(Long id, EstadoTrasladoEnum nuevoEstado) {
        Traslado traslado = trasladoRepository.findById(id);
        traslado.setEstado(nuevoEstado);
        trasladoRepository.save(traslado);
        return trasladoMapper.map(traslado);
    }
/*
    public void purgarTodo(){
        this.rutasRepository.clear();
    }
*/

}