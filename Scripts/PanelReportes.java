import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.List;

public class PanelReportes extends JPanel {
    private CMSController controller;
    private MainFrame mainFrame;
    
    private JTextArea txtReporte;
    private JLabel lblTotalContenidos;
    private JLabel lblTotalArticles;
    private JLabel lblTotalVideos;
    private JLabel lblTotalImages;
    private JLabel lblPublicados;
    private JLabel lblBorradores;

    public PanelReportes(CMSController controller, MainFrame mainFrame) {
        this.controller = controller;
        this.mainFrame = mainFrame;
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel("Reportes y Estadísticas");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        add(lblTitulo, BorderLayout.NORTH);

        JPanel panelEstadisticas = new JPanel(new GridLayout(3, 2, 15, 15));
        panelEstadisticas.setBackground(Color.WHITE);
        panelEstadisticas.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        panelEstadisticas.add(crearPanelEstadistica("Total Contenidos", "0", new Color(0, 123, 255)));
        panelEstadisticas.add(crearPanelEstadistica("Publicados", "0", new Color(40, 167, 69)));
        panelEstadisticas.add(crearPanelEstadistica("Artículos", "0", new Color(255, 193, 7)));
        panelEstadisticas.add(crearPanelEstadistica("Videos", "0", new Color(220, 53, 69)));
        panelEstadisticas.add(crearPanelEstadistica("Imágenes", "0", new Color(23, 162, 184)));
        panelEstadisticas.add(crearPanelEstadistica("Borradores", "0", new Color(108, 117, 125)));

        lblTotalContenidos = (JLabel) ((JPanel) panelEstadisticas.getComponent(0)).getComponent(1);
        lblPublicados = (JLabel) ((JPanel) panelEstadisticas.getComponent(1)).getComponent(1);
        lblTotalArticles = (JLabel) ((JPanel) panelEstadisticas.getComponent(2)).getComponent(1);
        lblTotalVideos = (JLabel) ((JPanel) panelEstadisticas.getComponent(3)).getComponent(1);
        lblTotalImages = (JLabel) ((JPanel) panelEstadisticas.getComponent(4)).getComponent(1);
        lblBorradores = (JLabel) ((JPanel) panelEstadisticas.getComponent(5)).getComponent(1);

        add(panelEstadisticas, BorderLayout.NORTH);

        JPanel panelReporte = new JPanel(new BorderLayout(5, 5));
        panelReporte.setBackground(Color.WHITE);

        JLabel lblTituloReporte = new JLabel("Reporte Detallado");
        lblTituloReporte.setFont(new Font("Arial", Font.BOLD, 16));
        panelReporte.add(lblTituloReporte, BorderLayout.NORTH);

        txtReporte = new JTextArea();
        txtReporte.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtReporte.setEditable(false);
        txtReporte.setLineWrap(true);
        txtReporte.setWrapStyleWord(true);
        txtReporte.setBackground(new Color(248, 249, 250));
        txtReporte.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(txtReporte);
        panelReporte.add(scrollPane, BorderLayout.CENTER);

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBoton.setBackground(Color.WHITE);

        JButton btnActualizar = crearBoton("Actualizar Reporte", new Color(0, 123, 255));
        btnActualizar.addActionListener(e -> actualizarReporte());
        panelBoton.add(btnActualizar);

        panelReporte.add(panelBoton, BorderLayout.SOUTH);

        add(panelReporte, BorderLayout.CENTER);

        actualizarReporte();
    }

    private JPanel crearPanelEstadistica(String titulo, String valor, Color color) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(color);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Arial", Font.BOLD, 36));
        lblValor.setForeground(Color.WHITE);
        lblValor.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(lblValor, BorderLayout.CENTER);

        return panel;
    }

    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return btn;
    }

    public void actualizarReporte() {
        List<Content> contenidos = controller.getContenidos();
        Map<String, Integer> reportePorTipo = controller.generarReportePorTipo();

        int totalContenidos = contenidos.size();
        int totalArticles = reportePorTipo.getOrDefault("Article", 0);
        int totalVideos = reportePorTipo.getOrDefault("Video", 0);
        int totalImages = reportePorTipo.getOrDefault("Image", 0);

        int publicados = 0;
        int borradores = 0;
        int archivados = 0;
        int eliminados = 0;

        for (Content c : contenidos) {
            switch (c.getState()) {
                case PUBLICADO:
                    publicados++;
                    break;
                case BORRADOR:
                    borradores++;
                    break;
                case ARCHIVADO:
                    archivados++;
                    break;
                case ELIMINADO:
                    eliminados++;
                    break;
            }
        }

        lblTotalContenidos.setText(String.valueOf(totalContenidos));
        lblTotalArticles.setText(String.valueOf(totalArticles));
        lblTotalVideos.setText(String.valueOf(totalVideos));
        lblTotalImages.setText(String.valueOf(totalImages));
        lblPublicados.setText(String.valueOf(publicados));
        lblBorradores.setText(String.valueOf(borradores));

        String reporte = controller.generarReporte();
        
        StringBuilder reporteCompleto = new StringBuilder();
        reporteCompleto.append("═══════════════════════════════════════════════════\n");
        reporteCompleto.append("       REPORTE GENERAL DEL SISTEMA CMS\n");
        reporteCompleto.append("═══════════════════════════════════════════════════\n\n");
        reporteCompleto.append(reporte);
        reporteCompleto.append("\n\n");
        reporteCompleto.append("───────────────────────────────────────────────────\n");
        reporteCompleto.append(" ESTADÍSTICAS POR TIPO\n");
        reporteCompleto.append("───────────────────────────────────────────────────\n");
        reporteCompleto.append(String.format("  • Artículos:  %d\n", totalArticles));
        reporteCompleto.append(String.format("  • Videos:     %d\n", totalVideos));
        reporteCompleto.append(String.format("  • Imágenes:   %d\n", totalImages));
        reporteCompleto.append("\n");
        reporteCompleto.append("───────────────────────────────────────────────────\n");
        reporteCompleto.append(" ESTADÍSTICAS POR ESTADO\n");
        reporteCompleto.append("───────────────────────────────────────────────────\n");
        reporteCompleto.append(String.format("  • Publicados: %d\n", publicados));
        reporteCompleto.append(String.format("  • Borradores: %d\n", borradores));
        reporteCompleto.append(String.format("  • Archivados: %d\n", archivados));
        reporteCompleto.append(String.format("  • Eliminados: %d\n", eliminados));
        reporteCompleto.append("\n");
        reporteCompleto.append("───────────────────────────────────────────────────\n");
        reporteCompleto.append(" ESTADÍSTICAS POR CATEGORÍA\n");
        reporteCompleto.append("───────────────────────────────────────────────────\n");
        
        for (Category cat : controller.getCategorias()) {
            List<Content> porCategoria = controller.filtrarPorCategoria(cat);
            reporteCompleto.append(String.format("  • %s: %d contenidos\n", 
                cat.getName(), porCategoria.size()));
        }
        
        reporteCompleto.append("\n");
        reporteCompleto.append("───────────────────────────────────────────────────\n");
        reporteCompleto.append(" USUARIOS DEL SISTEMA\n");
        reporteCompleto.append("───────────────────────────────────────────────────\n");
        reporteCompleto.append(String.format("  Total usuarios: %d\n", controller.getUsuarios().size()));
        
        User usuarioActual = controller.getCurrentUser();
        if (usuarioActual != null) {
            reporteCompleto.append(String.format("  Usuario actual: %s\n", usuarioActual.getName()));
            reporteCompleto.append(String.format("  Tipo: %s\n", usuarioActual.getClass().getSimpleName()));
        }
        
        reporteCompleto.append("\n");
        reporteCompleto.append("═══════════════════════════════════════════════════\n");

        txtReporte.setText(reporteCompleto.toString());
        txtReporte.setCaretPosition(0);
    }
}