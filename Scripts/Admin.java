import java.time.LocalDateTime;
import java.util.List;

public class Admin extends User 
{
    private int adminLevel;

    public Admin(int id, String name, String email, String password, LocalDateTime registerDate, int adminLevel) 
    {
        super(id, name, email, password, registerDate);
        this.adminLevel = adminLevel;
    }

    @Override
    public List<String> getPermissions() 
    {
        return List.of("CREATE_USER", "DELETE_USER", "MODIFY_SETTINGS", "DELETE_CONTENT", "VIEW_REPORTS");
    }

    @Override
    public boolean canMakeActions() 
    {
        return true;
    }

    public void eliminarContenido(Content content)
    {
        // Lógica para eliminar contenido
    }

    @Override
    public String toString() 
    {
        return super.toString() + ", Admin Level: " + adminLevel;
    }

    

    

}