import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

public class CMSController 
{
    // Listas para almacenar los objetos del sistema
    private List<User> usuarios;
    private List<Content> contenidos;
    private List<Category> categorias;
    private List<Tags> etiquetas;
    
    // Mapas para búsqueda rápida por ID
    private Map<Integer, User> usuariosPorId;
    private Map<Integer, Content> contenidosPorId;
    private Map<Integer, Category> categoriasPorId;
    private Map<Integer, Tags> etiquetasPorId;
    
    // Usuario actualmente autenticado
    private User usuarioActual;
    
    // Contadores para IDs autoincrementales
    private int nextUserId;
    private int nextContentId;
    private int nextCategoryId;
    private int nextTagId;
    
    // Referencia a la vista
    private CMSView vista;

    public CMSController() 
    {
        this.usuarios = new ArrayList<>();
        this.contenidos = new ArrayList<>();
        this.categorias = new ArrayList<>();
        this.etiquetas = new ArrayList<>();
        
        this.usuariosPorId = new HashMap<>();
        this.contenidosPorId = new HashMap<>();
        this.categoriasPorId = new HashMap<>();
        this.etiquetasPorId = new HashMap<>();
        
        this.usuarioActual = null;
        
        this.nextUserId = 1;
        this.nextContentId = 1;
        this.nextCategoryId = 1;
        this.nextTagId = 1;
    }

    public void setVista(CMSView vista) 
    {
        this.vista = vista;
    }
}