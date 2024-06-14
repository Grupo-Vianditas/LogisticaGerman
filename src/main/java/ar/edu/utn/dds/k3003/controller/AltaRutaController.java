package ar.edu.utn.dds.k3003.controller;


import ar.edu.utn.dds.k3003.model.Ruta;
import ar.edu.utn.dds.k3003.repositories.RutaRepository;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import org.jetbrains.annotations.NotNull;

public class AltaRutaController implements Handler{

    private RutaRepository rutaRepository;

        public AltaRutaController(RutaRepository rutaRepository){
            super();
            this.rutaRepository = rutaRepository;
        }
    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        Ruta ruta = ctx.bodyAsClass(Ruta.class);
        this.rutaRepository.addRuta(ruta);
        ctx.status(HttpStatus.CREATED);
        ctx.result("Ruta agregada correctamente");
    }
}
