import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class PanelCrearImagen extends JPanel {
    private CMSController controller;
    private MainFrame mainFrame;
    
    private JTextField txtTitulo;
    private JTextField txtDimensiones;
    private JComboBox<String> cbFormato;
    private JTextArea txtDescripcion;
    private JComboBox<String> cbCategoria;
    private JButton btnGuardar, btnLimpiar;

    public PanelCrearImagen(CMSController controller, MainFrame mainFrame) {
        this.controller = controller;
        this.mainFrame = mainFrame;
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel("Crear Nueva Imagen");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        add(lblTitulo, BorderLayout.NORTH);

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        int fila = 0;

        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0.2;
        panelFormulario.add(crearLabel("Título:*"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.8;
        txtTitulo = new JTextField(30);
        txtTitulo.setFont(new Font("Arial", Font.PLAIN, 14));
        panelFormulario.add(txtTitulo, gbc);

        fila++;

        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0.2;
        panelFormulario.add(crearLabel("Categoría:*"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.8;
        cbCategoria = new JComboBox<>();
        cbCategoria.setFont(new Font("Arial", Font.PLAIN, 14));
        cargarCategorias();
        panelFormulario.add(cbCategoria, gbc);

        fila++;

        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0.2;
        panelFormulario.add(crearLabel("Formato:*"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.8;
        cbFormato = new JComboBox<>(new String[]{"JPEG", "PNG", "GIF", "BMP", "SVG", "WEBP"});
        cbFormato.setFont(new Font("Arial", Font.PLAIN, 14));
        panelFormulario.add(cbFormato, gbc);

        fila++;

        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0.2;
        panelFormulario.add(crearLabel("Dimensiones:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.8;
        txtDimensiones = new JTextField(30);
        txtDimensiones.setFont(new Font("Arial", Font.PLAIN, 14));
        txtDimensiones.setText("1920x1080");
        panelFormulario.add(txtDimensiones, gbc);

        fila++;

        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0.2;
        gbc.anchor = GridBagConstraints.NORTH;
        panelFormulario.add(crearLabel("Descripción:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.8;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        txtDescripcion = new JTextArea(5, 30);
        txtDescripcion.setFont(new Font("Arial", Font.PLAIN, 14));
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        txtDescripcion.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JScrollPane scrollDescripcion = new JScrollPane(txtDescripcion);
        panelFormulario.add(scrollDescripcion, gbc);

        JScrollPane scrollFormulario = new JScrollPane(panelFormulario);
        scrollFormulario.setBorder(null);
        add(scrollFormulario, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setBackground(Color.WHITE);

        btnLimpiar = crearBoton("Limpiar", new Color(108, 117, 125));
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        panelBotones.add(btnLimpiar);

        btnGuardar = crearBoton("Guardar Imagen", new Color(40, 167, 69));
        btnGuardar.addActionListener(e -> guardarImagen());
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

    private void guardarImagen() {
        try {
            if (txtTitulo.getText().trim().isEmpty()) {
                throw new ContenidoException("El título es obligatorio");
            }
            if (cbCategoria.getSelectedItem() == null) {
                throw new ContenidoException("Debe seleccionar una categoría");
            }
            if (cbFormato.getSelectedItem() == null) {
                throw new ContenidoException("Debe seleccionar un formato");
            }

            String titulo = txtTitulo.getText().trim();
            String autor = controller.getCurrentUser().getName();
            String formato = (String) cbFormato.getSelectedItem();
            String dimensiones = txtDimensiones.getText().trim();
            String body = txtDescripcion.getText().trim().isEmpty() ? "Imagen" : txtDescripcion.getText().trim();
            String categoriaName = (String) cbCategoria.getSelectedItem();

            Image imagen = controller.createImage(titulo, autor, body, formato, dimensiones, categoriaName, new ArrayList<>());

            JOptionPane.showMessageDialog(
                this,
                "Imagen creada exitosamente\nID: " + imagen.getId(),
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
        txtDescripcion.setText("");
        txtDimensiones.setText("1920x1080");
        cbFormato.setSelectedIndex(0);
        if (cbCategoria.getItemCount() > 0) {
            cbCategoria.setSelectedIndex(0);
        }
        txtTitulo.requestFocus();
    }
}