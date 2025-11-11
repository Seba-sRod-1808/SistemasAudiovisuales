public class Admin extends User {
    public Admin(int id, String name, String email, String password) {
        super(id, name, email, password);
    }

    // devuelve true siempre, simplifica la logica ya que admin siempre tiene estos permisos
    @Override public boolean canCreate()  { return true; }
    @Override public boolean canEdit()    { return true; }
    @Override public boolean canPublish() { return true; }
    @Override public boolean canDelete()  { return true; }
}
