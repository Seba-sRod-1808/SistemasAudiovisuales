import java.time.LocalDateTime;
import java.util.List;


public class Editor extends User
{
    private String specialization;

    Editor(int id, String name, String email, String password, LocalDateTime registerDate, String specialization)
    {
        super(id, name, email, password, registerDate);
        this.specialization = specialization;
    }

    @Override
    public List<String> getPermissions()
    {
        return List.of("CREATE_CONTENT", "EDIT_CONTENT", "VIEW_ANALYTICS");
    }

    @Override
    public boolean canMakeActions()
    {
        return false;
    }

    @Override
    public String toString()
    {
        return super.toString() + ", Specialization: " + specialization;
    }

    public void solicitarPublicacion(Content content)
    {
        // Lógica para solicitar publicación de contenido
    }
    
}