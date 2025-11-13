import java.util.*;

/**
 * Controlador central del sistema audiovisual
 * - Autenticación
 * - Gestiona permisos
 * - CRUD
 * - Gestión de categoría y etiquetas
 * - Búsquedas y listados
 */
public class CMSController {

    private final List<Content> contents = new ArrayList<>();
    private final Map<String, User> usersByEmail = new HashMap<>();
    private final Map<String, Category> categoriesByName = new HashMap<>();
    private int contentSeq = 1;
    private int userSeq = 1;

    private User currentUser;

    // ======= INICIALIZACIÓN =======
    public void inicializar() {
        seedBasic();
    }

    // ======= Gestión de usuarios =======
    public Admin addAdmin(String name, String email, String password) {
        Admin u = new Admin(userSeq++, name, email, password);
        usersByEmail.put(email.toLowerCase(), u);
        return u;
    }

    public Editor addEditor(String name, String email, String password) {
        Editor u = new Editor(userSeq++, name, email, password);
        usersByEmail.put(email.toLowerCase(), u);
        return u;
    }

    public void login(String email, String password) throws AutenticacionException {
        User u = usersByEmail.get(email.toLowerCase());
        if (u == null || !u.authenticate(password)) {
            throw new AutenticacionException("Credenciales inválidas");
        }
        currentUser = u;
    }

    public void logout() { 
        currentUser = null; 
    }

    public User getCurrentUser() { 
        return currentUser; 
    }

    public List<User> getUsuarios() {
        return new ArrayList<>(usersByEmail.values());
    }

    private void requireSession() throws PermisoException {
        if (currentUser == null) {
            throw new PermisoException("Debe iniciar sesión");
        }
    }

    private void require(boolean permission, String message) throws PermisoException {
        if (!permission) {
            throw new PermisoException(message);
        }
    }

    public boolean validarPermisos(String accion) throws PermisoException {
        requireSession();
        
        switch (accion.toLowerCase()) {
            case "crear":
                return currentUser.canCreate();
            case "editar":
                return currentUser.canEdit();
            case "publicar":
                return currentUser.canPublish();
            case "eliminar":
                return currentUser.canDelete();
            default:
                return false;
        }
    }

    // ======= Categorías =======
    public Category addCategory(String name, String description) {
        Category c = new Category(name, description);
        categoriesByName.put(name.toLowerCase(), c);
        return c;
    }

    public Category findCategoryByName(String name) throws CategoriaException {
        Category cat = categoriesByName.get(name.toLowerCase());
        if (cat == null) {
            throw new CategoriaException("No se encontró la categoría: " + name);
        }
        return cat;
    }

    public List<Category> getCategorias() {
        return new ArrayList<>(categoriesByName.values());
    }

    // ======= CRUD =======
    public Article createArticle(String title, String author, String body, String categoryName, List<String> tags) 
            throws ContenidoException, PermisoException {
        requireSession();
        require(currentUser.canCreate(), "No tiene permiso para crear");

        Article a = new Article(nextId(), title, author, body);
        applyCategoryAndTags(a, categoryName, tags);
        contents.add(a);
        return a;
    }

    public Video createVideo(String title, String author, String body, String url, int durationSec,
                             String categoryName, List<String> tags) 
            throws ContenidoException, PermisoException {
        requireSession();
        require(currentUser.canCreate(), "No tiene permiso para crear");

        Video v = new Video(nextId(), title, author, body, url, durationSec);
        applyCategoryAndTags(v, categoryName, tags);
        contents.add(v);
        return v;
    }

    public Image createImage(String title, String author, String body, String format, String dimensions,
                             String categoryName, List<String> tags) 
            throws ContenidoException, PermisoException {
        requireSession();
        require(currentUser.canCreate(), "No tiene permiso para crear");

        Image img = new Image(nextId(), title, author, body, format, dimensions);
        applyCategoryAndTags(img, categoryName, tags);
        contents.add(img);
        return img;
    }

    private int nextId() { 
        return contentSeq++; 
    }

    private void applyCategoryAndTags(Content c, String categoryName, List<String> tags) 
            throws ContenidoException {
        if (categoryName != null && !categoryName.isBlank()) {
            Category cat = categoriesByName.get(categoryName.toLowerCase());
            if (cat == null) {
                throw new ContenidoException("Categoría inexistente: " + categoryName);
            }
            c.assignCategory(cat);
        }
        if (tags != null) {
            for (String t : tags) {
                if (t != null && !t.isBlank()) {
                    c.addTag(new Tags(t));
                }
            }
        }
    }

    // ======= Edicion =======
    public void editTitle(int id, String newTitle) throws ContenidoException, PermisoException {
        requireSession();
        require(currentUser.canEdit(), "No tiene permiso para editar");
        findByIdOrThrow(id).editTitle(newTitle);
    }

    public void editBody(int id, String newBody) throws ContenidoException, PermisoException {
        requireSession();
        require(currentUser.canEdit(), "No tiene permiso para editar");
        findByIdOrThrow(id).editBody(newBody);
    }

    public void assignCategory(int id, String categoryName) throws ContenidoException, PermisoException {
        requireSession();
        require(currentUser.canEdit(), "No tiene permiso para editar");
        Content c = findByIdOrThrow(id);
        Category cat = categoriesByName.get(categoryName.toLowerCase());
        if (cat == null) {
            throw new ContenidoException("Categoría inexistente: " + categoryName);
        }
        c.assignCategory(cat);
    }

    // ======= Publicación =======
    public boolean publish(int id) throws ContenidoException, PermisoException {
        requireSession();
        require(currentUser.canPublish(), "No tiene permiso para publicar");
        return findByIdOrThrow(id).publish();
    }

    public void delete(int id) throws ContenidoException, PermisoException {
        requireSession();
        require(currentUser.canDelete(), "No tiene permiso para eliminar");
        findByIdOrThrow(id).markDeleted();
    }

    // ======= Consultas =======
    public Content findByIdOrThrow(int id) throws ContenidoException {
        for (Content c : contents) {
            if (c.getId() == id) {
                return c;
            }
        }
        throw new ContenidoException("Contenido no encontrado #" + id);
    }

    public List<Content> getContenidos() {
        return new ArrayList<>(contents);
    }

    public List<Content> listByState(State s) {
        List<Content> out = new ArrayList<>();
        for (Content c : contents) {
            if (c.getState() == s) {
                out.add(c);
            }
        }
        return out;
    }

    public List<Content> listByCategory(String categoryName) {
        List<Content> out = new ArrayList<>();
        for (Content c : contents) {
            if (c.getCategory() != null &&
                c.getCategory().getName().equalsIgnoreCase(categoryName)) {
                out.add(c);
            }
        }
        return out;
    }

    public List<Content> buscarContenidos(String criterio) {
        List<Content> resultados = new ArrayList<>();
        
        if (criterio == null || criterio.trim().isEmpty()) {
            return resultados;
        }
        
        String criterioBusqueda = criterio.toLowerCase();
        
        for (Content c : contents) {
            if (c.getTitle().toLowerCase().contains(criterioBusqueda) ||
                c.getAuthor().toLowerCase().contains(criterioBusqueda) ||
                (c.getBody() != null && c.getBody().toLowerCase().contains(criterioBusqueda))) {
                resultados.add(c);
            }
        }
        
        return resultados;
    }

    public List<Content> filtrarPorTipo(String tipo) {
        List<Content> resultados = new ArrayList<>();
        
        if (tipo == null || tipo.trim().isEmpty()) {
            return new ArrayList<>(contents);
        }
        
        String tipoBusqueda = tipo.toLowerCase();
        
        for (Content c : contents) {
            String tipoContenido = c.getClass().getSimpleName().toLowerCase();
            if (tipoContenido.contains(tipoBusqueda)) {
                resultados.add(c);
            }
        }
        
        return resultados;
    }

    public List<Content> filtrarPorCategoria(Category categoria) {
        if (categoria == null) {
            return new ArrayList<>();
        }
        return listByCategory(categoria.getName());
    }

    public List<Content> filtrarPorEstado(State estado) {
        if (estado == null) {
            return new ArrayList<>();
        }
        return listByState(estado);
    }

    public String generarReporte() {
        StringBuilder reporte = new StringBuilder();
        
        reporte.append("Total de contenidos: ").append(contents.size()).append("\n");
        reporte.append("Publicados: ").append(listByState(State.PUBLICADO).size()).append("\n");
        reporte.append("Borradores: ").append(listByState(State.BORRADOR).size()).append("\n");
        reporte.append("Archivados: ").append(listByState(State.ARCHIVADO).size()).append("\n");
        reporte.append("Eliminados: ").append(listByState(State.ELIMINADO).size()).append("\n");
        
        return reporte.toString();
    }

    public Map<String, Integer> generarReportePorTipo() {
        Map<String, Integer> reporte = new HashMap<>();
        
        for (Content c : contents) {
            String tipo = c.getClass().getSimpleName();
            reporte.put(tipo, reporte.getOrDefault(tipo, 0) + 1);
        }
        
        return reporte;
    }

    public void ordenarContenidos() {
        Collections.sort(contents, (c1, c2) -> c2.getId() - c1.getId());
    }

    // ======= INICIALIZACIÓN =======
    public void seedBasic() {
        addCategory("Noticias", "Comunicados y avisos");
        addCategory("Eventos", "Calendario institucional");
        addCategory("Programación", "Contenidos sobre programación");
        addCategory("Diseño", "Contenidos sobre diseño gráfico");
        addCategory("Marketing", "Contenidos sobre marketing digital");
        addCategory("Multimedia", "Contenidos audiovisuales");
        addAdmin("Admin Principal", "admin@ega.edu", "admin123");
        addEditor("Editor Contenidos", "editor@ega.edu", "editor123");
    }
}