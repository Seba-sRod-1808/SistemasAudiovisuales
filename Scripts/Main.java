public class Main {
    public static void main(String[] args) {
        try {
            CMSController controller = new CMSController();
            controller.inicializar();
            
            CMSView vista = new CMSView(controller);
            vista.iniciar();
            
        } catch (Exception e) {
            System.err.println("Error al iniciar el sistema: " + e.getMessage());
            e.printStackTrace();
        }
    }
}