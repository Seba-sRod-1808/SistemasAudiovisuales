import javax.swing.*;

public class CMSView {
    private CMSController controller;
    private LoginFrame loginFrame;

    public CMSView(CMSController controller) {
        this.controller = controller;
        configurarLookAndFeel();
    }

    private void configurarLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("No se pudo configurar el Look and Feel");
        }
    }

    public void iniciar() {
        SwingUtilities.invokeLater(() -> {
            loginFrame = new LoginFrame(controller);
            loginFrame.mostrar();
        });
    }
}