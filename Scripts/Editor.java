public class Editor extends User {
    public Editor(int id, String name, String email, String password) {
        super(id, name, email, password);
    }
    @Override public boolean canCreate()  { return true; }
    @Override public boolean canEdit()    { return true; }
    @Override public boolean canPublish() { return true; }
    @Override public boolean canDelete()  { return false; }
}
