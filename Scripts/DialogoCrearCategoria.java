import java.awt.*;
import javax.swing.*;

public class DialogoCrearCategoria extends JDialog {
    private CMSController controller;
    private JTextField txtNombre;
    private JTextArea txtDescripcion;
    private boolean categoriaCreada = false;

    public DialogoCrearCategoria(JFrame parent, CMSController controller) {
        super(parent, "Crear Nueva Categoría", true);
        this.controller = controller;
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setSize(450, 300);
        setLocationRelativeTo(getParent());
        setResizable(false);

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelPrincipal.setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel("Nueva Categoría");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        JLabel lblNombre = new JLabel("Nombre:*");
        lblNombre.setFont(new Font("Arial", Font.BOLD, 14));
        panelFormulario.add(lblNombre, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        txtNombre = new JTextField(20);
        txtNombre.setFont(new Font("Arial", Font.PLAIN, 14));
        panelFormulario.add(txtNombre, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        gbc.anchor = GridBagConstraints.NORTH;
        JLabel lblDescripcion = new JLabel("Descripción:");
        lblDescripcion.setFont(new Font("Arial", Font.BOLD, 14));
        panelFormulario.add(lblDescripcion, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        txtDescripcion = new JTextArea(5, 20);
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
            categoriaCreada = false;
            dispose();
        });
        panelBotones.add(btnCancelar);

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setFont(new Font("Arial", Font.BOLD, 14));
        btnGuardar.setBackground(new Color(40, 167, 69));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGuardar.addActionListener(e -> guardarCategoria());
        panelBotones.add(btnGuardar);

        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        add(panelPrincipal);
    }

    private void guardarCategoria() {
        try {
            String nombre = txtNombre.getText().trim();
            String descripcion = txtDescripcion.getText().trim();

            if (nombre.isEmpty()) {
                throw new CategoriaException("El nombre de la categoría es obligatorio");
            }

            Category categoria = controller.addCategory(nombre, descripcion);

            categoriaCreada = true;
            JOptionPane.showMessageDialog(
                this,
                "Categoría creada exitosamente: " + categoria.getName(),
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

    public boolean isCategoriaCreada() {
        return categoriaCreada;
    }
}