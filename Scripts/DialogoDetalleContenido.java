import java.awt.*;
import javax.swing.*;

public class DialogoDetalleContenido extends JDialog {
    private Content contenido;

    public DialogoDetalleContenido(JFrame parent, Content contenido) {
        super(parent, "Detalle del Contenido", true);
        this.contenido = contenido;
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setSize(600, 500);
        setLocationRelativeTo(getParent());
        setResizable(false);

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelPrincipal.setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel(contenido.getTitle());
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(52, 58, 64));
        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        JPanel panelContenido = new JPanel(new GridBagLayout());
        panelContenido.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;

        int fila = 0;

        agregarCampo(panelContenido, gbc, fila++, "ID:", String.valueOf(contenido.getId()));
        agregarCampo(panelContenido, gbc, fila++, "Tipo:", contenido.getClass().getSimpleName());
        agregarCampo(panelContenido, gbc, fila++, "Autor:", contenido.getAuthor());
        agregarCampo(panelContenido, gbc, fila++, "Categoría:", 
            contenido.getCategory() != null ? contenido.getCategory().getName() : "Sin categoría");
        agregarCampo(panelContenido, gbc, fila++, "Estado:", contenido.getState().toString());

        if (contenido.getBody() != null && !contenido.getBody().isEmpty()) {
            gbc.gridx = 0;
            gbc.gridy = fila;
            gbc.gridwidth = 1;
            gbc.anchor = GridBagConstraints.NORTH;
            JLabel lblDescLabel = new JLabel("Descripción:");
            lblDescLabel.setFont(new Font("Arial", Font.BOLD, 14));
            panelContenido.add(lblDescLabel, gbc);

            gbc.gridx = 1;
            gbc.gridy = fila;
            gbc.fill = GridBagConstraints.BOTH;
            gbc.weighty = 1.0;
            JTextArea txtDesc = new JTextArea(contenido.getBody());
            txtDesc.setFont(new Font("Arial", Font.PLAIN, 14));
            txtDesc.setLineWrap(true);
            txtDesc.setWrapStyleWord(true);
            txtDesc.setEditable(false);
            txtDesc.setBackground(new Color(248, 249, 250));
            txtDesc.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
            ));
            JScrollPane scrollDesc = new JScrollPane(txtDesc);
            scrollDesc.setPreferredSize(new Dimension(350, 100));
            panelContenido.add(scrollDesc, gbc);
            fila++;
        }

        if (contenido instanceof Video) {
            Video video = (Video) contenido;
            agregarCampo(panelContenido, gbc, fila++, "URL:", video.getUrl());
            agregarCampo(panelContenido, gbc, fila++, "Duración:", formatearDuracion(video.getDurationSec()));
        } else if (contenido instanceof Image) {
            Image imagen = (Image) contenido;
            agregarCampo(panelContenido, gbc, fila++, "Formato:", imagen.getFormat());
            agregarCampo(panelContenido, gbc, fila++, "Dimensiones:", imagen.getDimensions());
        }

        if (!contenido.getTags().isEmpty()) {
            StringBuilder etiquetas = new StringBuilder();
            for (int i = 0; i < contenido.getTags().size(); i++) {
                etiquetas.append(contenido.getTags().get(i).getName());
                if (i < contenido.getTags().size() - 1) {
                    etiquetas.append(", ");
                }
            }
            agregarCampo(panelContenido, gbc, fila++, "Etiquetas:", etiquetas.toString());
        }

        JScrollPane scrollPanel = new JScrollPane(panelContenido);
        scrollPanel.setBorder(null);
        panelPrincipal.add(scrollPanel, BorderLayout.CENTER);

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBoton.setBackground(Color.WHITE);

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setFont(new Font("Arial", Font.BOLD, 14));
        btnCerrar.setBackground(new Color(108, 117, 125));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFocusPainted(false);
        btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrar.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        btnCerrar.addActionListener(e -> dispose());
        panelBoton.add(btnCerrar);

        panelPrincipal.add(panelBoton, BorderLayout.SOUTH);

        add(panelPrincipal);
    }

    private void agregarCampo(JPanel panel, GridBagConstraints gbc, int fila, String etiqueta, String valor) {
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 1;
        gbc.weightx = 0.3;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lblCampo = new JLabel(etiqueta);
        lblCampo.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(lblCampo, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(lblValor, gbc);
    }

    private String formatearDuracion(int segundos) {
        int horas = segundos / 3600;
        int minutos = (segundos % 3600) / 60;
        int segs = segundos % 60;
        
        if (horas > 0) {
            return String.format("%dh %dm %ds", horas, minutos, segs);
        } else if (minutos > 0) {
            return String.format("%dm %ds", minutos, segs);
        } else {
            return String.format("%ds", segs);
        }
    }
}