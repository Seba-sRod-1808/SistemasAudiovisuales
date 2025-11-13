public class PermisoException extends Exception {
    
    public PermisoException(String mensaje) {
        super(mensaje);
    }
    
    public PermisoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}