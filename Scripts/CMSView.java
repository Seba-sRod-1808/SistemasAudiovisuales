import java.util.List;
import java.util.Scanner;

public class CMSView 
{
    // Scanner para entrada de usuario
    private Scanner scanner;
    
    // Referencia al controlador
    private CMSController controlador;

    public CMSView() 
    {
        this.scanner = new Scanner(System.in);
    }

    public void setControlador(CMSController controlador) 
    {
        this.controlador = controlador;
    }

}