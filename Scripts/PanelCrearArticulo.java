import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class PanelCrearArticulo extends JPanel {
    private CMSController controller;
    private MainFrame mainFrame;
    
    private JTextField txtTitulo;
    private JTextArea txtContenido;
    private JTextField txtPalabrasClave;
    private JTextArea txtDescripcion;
    private JComboBox<String> cbCategoria;
    private JButton btnGuardar, btnLimpiar;

    public PanelCrearArticulo(CMSController controller, MainFrame mainFrame) {
        this.controller = controller;
        this.mainFrame = mainFrame;
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel("Crear Nuevo Artículo");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        add(lblTitulo, BorderLayout.NORTH);

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        panelFormulario.add(crearLabel("Título:*"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.8;
        txtTitulo = new JTextField(30);
        txtTitulo.setFont(new Font("Arial", Font.PLAIN, 14));
        panelFormulario.add(txtTitulo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.2;
        panelFormulario.add(crearLabel("Categoría:*"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.8;
        cbCategoria = new JComboBox<>();
        cbCategoria.setFont(new Font("Arial", Font.PLAIN, 14));
        cargarCategorias();
        panelFormulario.add(cbCategoria, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.2;
        gbc.anchor = GridBagConstraints.NORTH;
        panelFormulario.add(crearLabel("Descripción:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.8;
        gbc.anchor = GridBagConstraints.CENTER;
        txtDescripcion = new JTextArea(3, 30);
        txtDescripcion.setFont(new Font("Arial", Font.PLAIN, 14));
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        txtDescripcion.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JScrollPane scrollDescripcion = new JScrollPane(txtDescripcion);
        panelFormulario.add(scrollDescripcion, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.2;
        gbc.anchor = GridBagConstraints.NORTH;
        panelFormulario.add(crearLabel("Contenido:*"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.8;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        txtContenido = new JTextArea(10, 30);
        txtContenido.setFont(new Font("Arial", Font.PLAIN, 14));
        txtContenido.setLineWrap(true);
        txtContenido.setWrapStyleWord(true);
        txtContenido.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JScrollPane scrollContenido = new JScrollPane(txtContenido);
        panelFormulario.add(scrollContenido, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.2;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        panelFormulario.add(crearLabel("Palabras clave:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.8;
        txtPalabrasClave = new JTextField(30);
        txtPalabrasClave.setFont(new Font("Arial", Font.PLAIN, 14));
        panelFormulario.add(txtPalabrasClave, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        JLabel lblNota = new JLabel("Separar palabras clave con comas (,)");
        lblNota.setFont(new Font("Arial", Font.ITALIC, 12));
        lblNota.setForeground(Color.GRAY);
        panelFormulario.add(lblNota, gbc);

        JScrollPane scrollFormulario = new JScrollPane(panelFormulario);
        scrollFormulario.setBorder(null);
        add(scrollFormulario, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setBackground(Color.WHITE);

        btnLimpiar = crearBoton("Limpiar", new Color(108, 117, 125));
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        panelBotones.add(btnLimpiar);

        btnGuardar = crearBoton("Guardar Artículo", new Color(40, 167, 69));
        btnGuardar.addActionListener(e -> guardarArticulo());
        panelBotones.add(btnGuardar);

        add(panelBotones, BorderLayout.SOUTH);
    }

    private JLabel crearLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        return label;
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

    private void cargarCategorias() {
        cbCategoria.removeAllItems();
        for (Category cat : controller.getCategorias()) {
            cbCategoria.addItem(cat.getName());
        }
    }

    private void guardarArticulo() {
        try {
            if (txtTitulo.getText().trim().isEmpty()) {
                throw new ContenidoException("El título es obligatorio");
            }
            if (txtContenido.getText().trim().isEmpty()) {
                throw new ContenidoException("El contenido es obligatorio");
            }
            if (cbCategoria.getSelectedItem() == null) {
                throw new ContenidoException("Debe seleccionar una categoría");
            }

            String titulo = txtTitulo.getText().trim();
            String autor = controller.getCurrentUser().getName();
            String contenido = txtContenido.getText().trim();
            String body = txtDescripcion.getText().trim().isEmpty() ? contenido : txtDescripcion.getText().trim();
            String categoriaName = (String) cbCategoria.getSelectedItem();

            List<String> palabrasClave = new ArrayList<>();
            if (!txtPalabrasClave.getText().trim().isEmpty()) {
                String[] palabras = txtPalabrasClave.getText().split(",");
                for (String palabra : palabras) {
                    palabrasClave.add(palabra.trim());
                }
            }

            Article articulo = controller.createArticle(titulo, autor, body, categoriaName, palabrasClave);

            JOptionPane.showMessageDialog(
                this,
                "Artículo creado exitosamente\nID: " + articulo.getId(),
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE
            );

            limpiarFormulario();
            mainFrame.actualizarLista();

        } catch (ContenidoException | PermisoException ex) {
            JOptionPane.showMessageDialog(
                this,
                "Error: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void limpiarFormulario() {
        txtTitulo.setText("");
        txtContenido.setText("");
        txtDescripcion.setText("");
        txtPalabrasClave.setText("");
        if (cbCategoria.getItemCount() > 0) {
            cbCategoria.setSelectedIndex(0);
        }
        txtTitulo.requestFocus();
    }
}