import java.util.*;

/**
 * Controlador central del sistema audiovisual
 * - Autenticación
 * - Gestiona permisos
 * - CRUD
 * - Gestión de categoría y etiquetas
 * - Búsquedas y listados
 *
 */
public class CMSController {

    // Repositorios de contenido en memoria, no DB como tal
    private final List<Content> contents = new ArrayList<>();      // lista polimorfica
    private final Map<String, User> usersByEmail = new HashMap<>();
    private final Map<String, Category> categoriesByName = new HashMap<>();
    private int contentSeq = 1;
    private int userSeq = 1;

    private User currentUser;

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

    public void login(String email, String password) {
        User u = usersByEmail.get(email.toLowerCase());
        if (u == null || !u.authenticate(password)) throw new IllegalStateException("Credenciales inválidas");
        currentUser = u;
    }

    public void logout() { currentUser = null; }

    public Optional<User> getCurrentUser() { return Optional.ofNullable(currentUser); }

    private void requireSession() {
        if (currentUser == null) throw new IllegalStateException("Debe iniciar sesión");
    }

    private void require(boolean permission, String message) {
        if (!permission) throw new SecurityException(message);
    }

    // ======= Categorías =======
    public Category addCategory(String name, String description) {
        Category c = new Category(name, description);
        categoriesByName.put(name.toLowerCase(), c);
        return c;
    }

    public Optional<Category> findCategory(String name) {
        return Optional.ofNullable(categoriesByName.get(name.toLowerCase()));
    }

    public Collection<Category> listCategories() {
        return Collections.unmodifiableCollection(categoriesByName.values());
    }

    // ======= CRUD =======
    public Article createArticle(String title, String author, String body, String categoryName, List<String> tags) {
        requireSession();
        require(currentUser.canCreate(), "No tiene permiso para crear");

        Article a = new Article(nextId(), title, author, body);
        applyCategoryAndTags(a, categoryName, tags);
        contents.add(a);
        return a;
    }

    public Video createVideo(String title, String author, String body, String url, int durationSec,
                             String categoryName, List<String> tags) {
        requireSession();
        require(currentUser.canCreate(), "No tiene permiso para crear");

        Video v = new Video(nextId(), title, author, body, url, durationSec);
        applyCategoryAndTags(v, categoryName, tags);
        contents.add(v);
        return v;
    }

    public Image createImage(String title, String author, String body, String format, String dimensions,
                             String categoryName, List<String> tags) {
        requireSession();
        require(currentUser.canCreate(), "No tiene permiso para crear");

        Image img = new Image(nextId(), title, author, body, format, dimensions);
        applyCategoryAndTags(img, categoryName, tags);
        contents.add(img);
        return img;
    }

    private int nextId() { return contentSeq++; }

    private void applyCategoryAndTags(Content c, String categoryName, List<String> tags) {
        if (categoryName != null && !categoryName.isBlank()) {
            Category cat = categoriesByName.get(categoryName.toLowerCase());
            if (cat == null) throw new IllegalArgumentException("Categoría inexistente: " + categoryName);
            c.assignCategory(cat);
        }
        if (tags != null) {
            for (String t : tags) if (t != null && !t.isBlank()) c.addTag(new Tags(t));
        }
    }

    // ======= Edicion =======
    public void editTitle(int id, String newTitle) {
        requireSession();
        require(currentUser.canEdit(), "No tiene permiso para editar");
        findByIdOrThrow(id).editTitle(newTitle);
    }

    public void editBody(int id, String newBody) {
        requireSession();
        require(currentUser.canEdit(), "No tiene permiso para editar");
        findByIdOrThrow(id).editBody(newBody);
    }

    public void assignCategory(int id, String categoryName) {
        requireSession();
        require(currentUser.canEdit(), "No tiene permiso para editar");
        Content c = findByIdOrThrow(id);
        Category cat = categoriesByName.get(categoryName.toLowerCase());
        if (cat == null) throw new IllegalArgumentException("Categoría inexistente: " + categoryName);
        c.assignCategory(cat);
    }

    public void addTag(int id, String tag) {
        requireSession();
        require(currentUser.canEdit(), "No tiene permiso para editar");
        findByIdOrThrow(id).addTag(new Tags(tag));
    }

    public void removeTag(int id, String tag) {
        requireSession();
        require(currentUser.canEdit(), "No tiene permiso para editar");
        findByIdOrThrow(id).removeTag(tag);
    }

    // ======= Publicación =======
    public boolean publish(int id) {
        requireSession();
        require(currentUser.canPublish(), "No tiene permiso para publicar");
        return findByIdOrThrow(id).publish();
    }

    public void delete(int id) {
        requireSession();
        require(currentUser.canDelete(), "No tiene permiso para eliminar");
        // Marcado lógico como ELIMINADO. NO REMUEVE FISICAMENTE. EDITAR DESPUES 
        findByIdOrThrow(id).markDeleted();
    }

    // ======= Consultas =======
    public Optional<Content> findById(int id) {
        for (Content c : contents) if (c.getId() == id) return Optional.of(c);
        return Optional.empty();
    }

    public Content findByIdOrThrow(int id) {
        return findById(id).orElseThrow(() -> new NoSuchElementException("Contenido no encontrado #" + id));
    }

    public List<Content> listAll() {
        return Collections.unmodifiableList(contents);
    }

    public List<Content> listByState(State s) {
        List<Content> out = new ArrayList<>();
        for (Content c : contents) if (c.getState() == s) out.add(c);
        return out;
    }

    public List<Content> listByCategory(String categoryName) {
        List<Content> out = new ArrayList<>();
        for (Content c : contents) {
            if (c.getCategory() != null &&
                c.getCategory().getName().equalsIgnoreCase(categoryName)) out.add(c);
        }
        return out;
    }

    public List<Content> listPublished() { return listByState(State.PUBLICADO); }
    public List<Content> listDrafts()    { return listByState(State.BORRADOR); }
    public List<Content> listDeleted()   { return listByState(State.ELIMINADO); }

    // ======= PRUEBAS CON USUARIOS =======
    public void seedBasic() {
        addCategory("Noticias", "Comunicados y avisos");
        addCategory("Eventos", "Calendario institucional");
        addAdmin("Admin", "admin@ega.edu", "admin");
        addEditor("Editor", "editor@ega.edu", "editor");
    }
}
