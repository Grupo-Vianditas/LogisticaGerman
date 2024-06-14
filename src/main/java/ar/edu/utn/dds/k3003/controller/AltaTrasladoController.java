package ar.edu.utn.dds.k3003.controller;

import ar.edu.utn.dds.k3003.model.Traslado;
import ar.edu.utn.dds.k3003.repositories.TrasladoRepository;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import org.jetbrains.annotations.NotNull;

public class AltaTrasladoController implements Handler{

    private TrasladoRepository trasladoRepository;

    public AltaTrasladoController(TrasladoRepository trasladoRepository){
        super();
        this.trasladoRepository = trasladoRepository;
    }
    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        Traslado traslado = ctx.bodyAsClass(Traslado.class);
        this.trasladoRepository.addTraslado(traslado);
        ctx.status(HttpStatus.CREATED);
        ctx.result("Traslado agregado correctamente");
    }
}
