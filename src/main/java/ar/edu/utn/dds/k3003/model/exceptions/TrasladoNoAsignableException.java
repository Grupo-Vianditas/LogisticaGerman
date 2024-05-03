package ar.edu.utn.dds.k3003.model.exceptions;


public class TrasladoNoAsignableException extends Exception {

    public TrasladoNoAsignableException(String message) {
        super("No se pudo encontrar la vianda con el QR proporcionado.");
    }
}
