public class ContenidoException extends Exception {
    
    public ContenidoException(String mensaje) {
        super(mensaje);
    }
    
    public ContenidoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}