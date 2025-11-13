import java.awt.*;
import javax.swing.*;

public class MainFrame extends JFrame {
    private CMSController controller;
    private JPanel panelContenido;
    private JLabel lblUsuario;
    private CardLayout cardLayout;

    private PanelListaContenidos panelLista;
    private PanelCrearArticulo panelArticulo;
    private PanelCrearVideo panelVideo;
    private PanelCrearImagen panelImagen;
    private PanelBuscar panelBuscar;
    private PanelReportes panelReportes;

    public MainFrame(CMSController controller) {
        this.controller = controller;
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setTitle("Sistema CMS - EGA");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        add(crearBarraSuperior(), BorderLayout.NORTH);
        add(crearPanelLateral(), BorderLayout.WEST);

        cardLayout = new CardLayout();
        panelContenido = new JPanel(cardLayout);
        panelContenido.setBackground(Color.WHITE);

        panelLista = new PanelListaContenidos(controller, this);
        panelArticulo = new PanelCrearArticulo(controller, this);
        panelVideo = new PanelCrearVideo(controller, this);
        panelImagen = new PanelCrearImagen(controller, this);
        panelBuscar = new PanelBuscar(controller, this);
        panelReportes = new PanelReportes(controller, this);

        panelContenido.add(panelLista, "LISTA");
        panelContenido.add(panelArticulo, "ARTICULO");
        panelContenido.add(panelVideo, "VIDEO");
        panelContenido.add(panelImagen, "IMAGEN");
        panelContenido.add(panelBuscar, "BUSCAR");
        panelContenido.add(panelReportes, "REPORTES");

        add(panelContenido, BorderLayout.CENTER);
        add(crearBarraInferior(), BorderLayout.SOUTH);

        mostrarPanel("LISTA");
    }

    private JPanel crearBarraSuperior() {
        JPanel barra = new JPanel(new BorderLayout());
        barra.setBackground(new Color(52, 58, 64));
        barra.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel lblTitulo = new JLabel("Sistema de Gestión de Contenidos");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        barra.add(lblTitulo, BorderLayout.WEST);

        JPanel panelUsuario = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelUsuario.setOpaque(false);

        User usuario = controller.getCurrentUser();
        lblUsuario = new JLabel("Usuario: " + (usuario != null ? usuario.getName() : "Desconocido"));
        lblUsuario.setForeground(Color.WHITE);
        lblUsuario.setFont(new Font("Arial", Font.PLAIN, 14));
        panelUsuario.add(lblUsuario);

        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setBackground(new Color(220, 53, 69));
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrarSesion.addActionListener(e -> cerrarSesion());
        panelUsuario.add(btnCerrarSesion);

        barra.add(panelUsuario, BorderLayout.EAST);

        return barra;
    }

    private JPanel crearPanelLateral() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(248, 249, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        panel.setPreferredSize(new Dimension(200, 0));

        panel.add(crearBotonMenu("📋 Ver Contenidos", "LISTA"));
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(crearBotonMenu("📝 Crear Artículo", "ARTICULO"));
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(crearBotonMenu("🎥 Crear Video", "VIDEO"));
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(crearBotonMenu("🖼️ Crear Imagen", "IMAGEN"));
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(crearBotonMenu("🔍 Buscar", "BUSCAR"));
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(crearBotonMenu("📊 Reportes", "REPORTES"));

        return panel;
    }

    private JButton crearBotonMenu(String texto, String panel) {
        JButton btn = new JButton(texto);
        btn.setMaximumSize(new Dimension(180, 40));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setFont(new Font("Arial", Font.PLAIN, 14));
        btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        btn.addActionListener(e -> mostrarPanel(panel));
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(0, 123, 255));
                btn.setForeground(Color.WHITE);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(Color.WHITE);
                btn.setForeground(Color.BLACK);
            }
        });

        return btn;
    }

    private JPanel crearBarraInferior() {
        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT));
        barra.setBackground(new Color(248, 249, 250));
        barra.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));

        JLabel lblEstado = new JLabel("Sistema CMS v1.0 | Total contenidos: " + controller.getContenidos().size());
        lblEstado.setFont(new Font("Arial", Font.PLAIN, 12));
        barra.add(lblEstado);

        return barra;
    }

    public void mostrarPanel(String nombrePanel) {
        if (nombrePanel.equals("LISTA")) {
            panelLista.actualizarTabla();
        } else if (nombrePanel.equals("REPORTES")) {
            panelReportes.actualizarReporte();
        }
        
        cardLayout.show(panelContenido, nombrePanel);
    }

    public void actualizarLista() {
        panelLista.actualizarTabla();
    }

    private void cerrarSesion() {
        int respuesta = JOptionPane.showConfirmDialog(
            this,
            "¿Está seguro que desea cerrar sesión?",
            "Confirmar",
            JOptionPane.YES_NO_OPTION
        );

        if (respuesta == JOptionPane.YES_OPTION) {
            controller.logout();
            LoginFrame loginFrame = new LoginFrame(controller);
            loginFrame.mostrar();
            this.dispose();
        }
    }
}