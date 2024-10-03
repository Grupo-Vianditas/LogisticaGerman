package ar.edu.utn.dds.k3003.app;


import ar.edu.utn.dds.k3003.clients.ViandasProxy;
import ar.edu.utn.dds.k3003.controller.RutaController;
import ar.edu.utn.dds.k3003.controller.TrasladoController;
import ar.edu.utn.dds.k3003.facades.dtos.Constants;

import ar.edu.utn.dds.k3003.metrics.MetricsConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;
import io.javalin.micrometer.MicrometerPlugin;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;


import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;





public class WebApp {

    private static final String TOKEN = "token123";

    public static void main(String[] args) {

        var env = System.getenv();
        var objectMapper = createObjectMapper();
        var fachada = new Fachada();

        MetricsConfig metricsConfig = new MetricsConfig();
        PrometheusMeterRegistry registry = metricsConfig.getRegistry();
        final var micrometerPlugin = new MicrometerPlugin(config -> config.registry = registry);

        fachada.setViandasProxy(new ViandasProxy(objectMapper));

        var port = Integer.parseInt(env.getOrDefault("PORT", "8080"));


        var app = Javalin.create(config -> {
            config.jsonMapper(new JavalinJackson().updateMapper(mapper -> {
                configureObjectMapper(mapper);
            }));

            // Registra el plugin de métricas
            config.registerPlugin(micrometerPlugin);
        }).start(port);


        var rutaController = new RutaController(fachada, metricsConfig);
        var trasladosController = new TrasladoController(fachada);

        app.post("/rutas", rutaController::agregar);
        app.post("/traslados", trasladosController::asignar);
        app.get("/traslados/{id}", trasladosController::obtener);
        app.get("/traslados/search/findByColaboradorId",trasladosController::obtenerTrasladosPorColaboradorId);
        app.patch("/traslados/{id}",trasladosController::actualizarEstadoTraslado);

        // Controller metricas
        app.get("/metrics", ctx -> {
            var auth = ctx.header("Authorization");

            if (auth != null && auth.equals("Bearer " + TOKEN)) {
                ctx.contentType("text/plain; version=0.0.4")
                        .result(registry.scrape());
            } else {
                ctx.status(401).json("unauthorized access");
            }
        });
    }


    public static ObjectMapper createObjectMapper() {
        var objectMapper = new ObjectMapper();
        configureObjectMapper(objectMapper);
        return objectMapper;
    }

    public static void configureObjectMapper(ObjectMapper objectMapper) {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        var sdf = new SimpleDateFormat(Constants.DEFAULT_SERIALIZATION_FORMAT, Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        objectMapper.setDateFormat(sdf);
    }
}

            /*
            Integer port = Integer.parseInt(
                   System.getProperty("port","8080"));

            Javalin app = Javalin.create().start(port);
                app.get("/",ctx -> ctx.result("Hola Mundo"));


            RutaRepository rutaRepository = new RutaRepository();
                app.post("/rutas",
                        new AltaRutaController(rutaRepository));

            TrasladoRepository trasladoRepository = new TrasladoRepository();
            app.post("/traslados",
                    new AltaTrasladoController(trasladoRepository));

*/


// cuando alguien mande un request a / que ejecute el Hola mundo como resultado.
