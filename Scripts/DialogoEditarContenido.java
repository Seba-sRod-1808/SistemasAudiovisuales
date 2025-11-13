import java.awt.*;
import javax.swing.*;

public class DialogoEditarContenido extends JDialog {
    private CMSController controller;
    private Content contenido;
    private JTextField txtTitulo;
    private JTextArea txtDescripcion;
    private JComboBox<String> cbCategoria;
    private JComboBox<String> cbEstado;
    private boolean contenidoEditado = false;

    public DialogoEditarContenido(JFrame parent, CMSController controller, Content contenido) {
        super(parent, "Editar Contenido", true);
        this.controller = controller;
        this.contenido = contenido;
        inicializarComponentes();
        cargarDatos();
    }

    private void inicializarComponentes() {
        setSize(500, 450);
        setLocationRelativeTo(getParent());
        setResizable(false);

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelPrincipal.setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel("Editar: " + contenido.getClass().getSimpleName());
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        int fila = 0;

        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0.3;
        panelFormulario.add(crearLabel("ID:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JLabel lblId = new JLabel(String.valueOf(contenido.getId()));
        lblId.setFont(new Font("Arial", Font.PLAIN, 14));
        panelFormulario.add(lblId, gbc);

        fila++;

        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0.3;
        panelFormulario.add(crearLabel("Tipo:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JLabel lblTipo = new JLabel(contenido.getClass().getSimpleName());
        lblTipo.setFont(new Font("Arial", Font.PLAIN, 14));
        panelFormulario.add(lblTipo, gbc);

        fila++;

        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0.3;
        panelFormulario.add(crearLabel("Título:*"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        txtTitulo = new JTextField(25);
        txtTitulo.setFont(new Font("Arial", Font.PLAIN, 14));
        panelFormulario.add(txtTitulo, gbc);

        fila++;

        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0.3;
        panelFormulario.add(crearLabel("Categoría:*"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        cbCategoria = new JComboBox<>();
        cbCategoria.setFont(new Font("Arial", Font.PLAIN, 14));
        cargarCategorias();
        panelFormulario.add(cbCategoria, gbc);

        fila++;

        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0.3;
        panelFormulario.add(crearLabel("Estado:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        cbEstado = new JComboBox<>(new String[]{"BORRADOR", "PUBLICADO", "ELIMINADO"});
        cbEstado.setFont(new Font("Arial", Font.PLAIN, 14));
        panelFormulario.add(cbEstado, gbc);

        fila++;

        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0.3;
        gbc.anchor = GridBagConstraints.NORTH;
        panelFormulario.add(crearLabel("Descripción:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        txtDescripcion = new JTextArea(5, 25);
        txtDescripcion.setFont(new Font("Arial", Font.PLAIN, 14));
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        txtDescripcion.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JScrollPane scrollDescripcion = new JScrollPane(txtDescripcion);
        panelFormulario.add(scrollDescripcion, gbc);

        panelPrincipal.add(panelFormulario, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setBackground(Color.WHITE);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Arial", Font.PLAIN, 14));
        btnCancelar.setBackground(new Color(108, 117, 125));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.addActionListener(e -> {
            contenidoEditado = false;
            dispose();
        });
        panelBotones.add(btnCancelar);

        JButton btnGuardar = new JButton("Guardar Cambios");
        btnGuardar.setFont(new Font("Arial", Font.BOLD, 14));
        btnGuardar.setBackground(new Color(0, 123, 255));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGuardar.addActionListener(e -> guardarCambios());
        panelBotones.add(btnGuardar);

        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        add(panelPrincipal);
    }

    private JLabel crearLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        return label;
    }

    private void cargarCategorias() {
        cbCategoria.removeAllItems();
        for (Category cat : controller.getCategorias()) {
            cbCategoria.addItem(cat.getName());
        }
    }

    private void cargarDatos() {
        txtTitulo.setText(contenido.getTitle());
        txtDescripcion.setText(contenido.getBody() != null ? contenido.getBody() : "");
        
        if (contenido.getCategory() != null) {
            for (int i = 0; i < cbCategoria.getItemCount(); i++) {
                if (cbCategoria.getItemAt(i).equals(contenido.getCategory().getName())) {
                    cbCategoria.setSelectedIndex(i);
                    break;
                }
            }
        }

        cbEstado.setSelectedItem(contenido.getState().toString());
    }

    private void guardarCambios() {
        try {
            String nuevoTitulo = txtTitulo.getText().trim();
            if (nuevoTitulo.isEmpty()) {
                throw new ContenidoException("El título no puede estar vacío");
            }

            String nombreCategoria = (String) cbCategoria.getSelectedItem();
            if (nombreCategoria == null) {
                throw new ContenidoException("Debe seleccionar una categoría");
            }

            // Editar usando los métodos del controlador
            controller.editTitle(contenido.getId(), nuevoTitulo);
            controller.editBody(contenido.getId(), txtDescripcion.getText().trim());
            controller.assignCategory(contenido.getId(), nombreCategoria);

            contenidoEditado = true;
            JOptionPane.showMessageDialog(
                this,
                "Contenido actualizado exitosamente",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE
            );
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                this,
                "Error: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public boolean isContenidoEditado() {
        return contenidoEditado;
    }
}