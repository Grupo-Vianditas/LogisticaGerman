package ar.edu.utn.dds.k3003.repositories;

import ar.edu.utn.dds.k3003.model.Traslado;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class TrasladoRepository {

    private static AtomicLong seqId = new AtomicLong();
    private Collection<Traslado> traslados;

    public TrasladoRepository(){
        this.traslados = new ArrayList<>();
    }

    public Traslado save(Traslado traslado) {
        if (Objects.isNull(traslado.getId())) {
            traslado.setId(seqId.getAndIncrement());
            this.traslados.add(traslado);
        }
        return traslado;
    }

    public Traslado findById(Long id) {
        Optional<Traslado> first = this.traslados.stream().filter(x -> x.getId().equals(id)).findFirst();
        return first.orElseThrow(() -> new NoSuchElementException(
                String.format("No hay un traslado de id: %s", id)
        ));
    }

    //TODO: AGREGUE TRASLADO


    public List<Traslado> findByColaboradorId(Long colaboradorId) {
        return this.traslados.stream()
                .filter(traslado -> traslado.getRuta().getColaboradorId().equals(colaboradorId))
                .collect(Collectors.toList());
    }

    public List<Traslado> findByColaboradorIdAndFecha(Long colaboradorId, Integer mes, Integer anio) {
        return this.traslados.stream()
                .filter(traslado -> traslado.getRuta().getColaboradorId().equals(colaboradorId) &&
                        traslado.getFechaTraslado().getMonthValue() == mes &&
                        traslado.getFechaTraslado().getYear() == anio)
                .collect(Collectors.toList());
    }

}