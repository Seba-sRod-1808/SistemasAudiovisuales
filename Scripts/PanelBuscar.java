import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PanelBuscar extends JPanel {
    private CMSController controller;
    private MainFrame mainFrame;
    
    private JTextField txtBusqueda;
    private JComboBox<String> cbTipoBusqueda;
    private JComboBox<String> cbCategoria;
    private JComboBox<String> cbEstado;
    private JButton btnBuscar, btnLimpiar;
    private JTable tablaResultados;
    private DefaultTableModel modeloTabla;
    private JLabel lblResultados;

    public PanelBuscar(CMSController controller, MainFrame mainFrame) {
        this.controller = controller;
        this.mainFrame = mainFrame;
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel("Buscar Contenidos");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        add(lblTitulo, BorderLayout.NORTH);

        JPanel panelBusqueda = new JPanel(new GridBagLayout());
        panelBusqueda.setBackground(Color.WHITE);
        panelBusqueda.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            "Criterios de Búsqueda",
            0, 0,
            new Font("Arial", Font.BOLD, 14)
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        panelBusqueda.add(crearLabel("Buscar por:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.8;
        cbTipoBusqueda = new JComboBox<>(new String[]{"Texto libre", "Por Tipo", "Por Categoría", "Por Estado"});
        cbTipoBusqueda.setFont(new Font("Arial", Font.PLAIN, 14));
        cbTipoBusqueda.addActionListener(e -> actualizarCamposBusqueda());
        panelBusqueda.add(cbTipoBusqueda, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.2;
        panelBusqueda.add(crearLabel("Texto:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.8;
        txtBusqueda = new JTextField(30);
        txtBusqueda.setFont(new Font("Arial", Font.PLAIN, 14));
        panelBusqueda.add(txtBusqueda, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.2;
        panelBusqueda.add(crearLabel("Categoría:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.8;
        cbCategoria = new JComboBox<>();
        cbCategoria.setFont(new Font("Arial", Font.PLAIN, 14));
        cbCategoria.addItem("Todas");
        for (Category cat : controller.getCategorias()) {
            cbCategoria.addItem(cat.getName());
        }
        cbCategoria.setEnabled(false);
        panelBusqueda.add(cbCategoria, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.2;
        panelBusqueda.add(crearLabel("Estado:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.8;
        cbEstado = new JComboBox<>(new String[]{"Todos", "BORRADOR", "PUBLICADO", "ARCHIVADO", "ELIMINADO"});
        cbEstado.setFont(new Font("Arial", Font.PLAIN, 14));
        cbEstado.setEnabled(false);
        panelBusqueda.add(cbEstado, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        JPanel panelBotonesBusqueda = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotonesBusqueda.setBackground(Color.WHITE);

        btnLimpiar = crearBoton("Limpiar", new Color(108, 117, 125));
        btnLimpiar.addActionListener(e -> limpiarBusqueda());
        panelBotonesBusqueda.add(btnLimpiar);

        btnBuscar = crearBoton("Buscar", new Color(0, 123, 255));
        btnBuscar.addActionListener(e -> realizarBusqueda());
        panelBotonesBusqueda.add(btnBuscar);

        panelBusqueda.add(panelBotonesBusqueda, gbc);

        add(panelBusqueda, BorderLayout.NORTH);

        JPanel panelResultados = new JPanel(new BorderLayout(5, 5));
        panelResultados.setBackground(Color.WHITE);
        panelResultados.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        lblResultados = new JLabel("Resultados: 0 contenidos encontrados");
        lblResultados.setFont(new Font("Arial", Font.BOLD, 14));
        panelResultados.add(lblResultados, BorderLayout.NORTH);

        String[] columnas = {"ID", "Tipo", "Título", "Autor", "Categoría", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaResultados = new JTable(modeloTabla);
        tablaResultados.setFont(new Font("Arial", Font.PLAIN, 14));
        tablaResultados.setRowHeight(30);
        tablaResultados.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tablaResultados.getTableHeader().setBackground(new Color(52, 58, 64));
        tablaResultados.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(tablaResultados);
        panelResultados.add(scrollPane, BorderLayout.CENTER);

        add(panelResultados, BorderLayout.CENTER);

        txtBusqueda.addActionListener(e -> realizarBusqueda());
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

    private void actualizarCamposBusqueda() {
        String seleccion = (String) cbTipoBusqueda.getSelectedItem();
        
        switch (seleccion) {
            case "Texto libre":
                txtBusqueda.setEnabled(true);
                cbCategoria.setEnabled(false);
                cbEstado.setEnabled(false);
                break;
            case "Por Tipo":
                txtBusqueda.setEnabled(true);
                cbCategoria.setEnabled(false);
                cbEstado.setEnabled(false);
                break;
            case "Por Categoría":
                txtBusqueda.setEnabled(false);
                cbCategoria.setEnabled(true);
                cbEstado.setEnabled(false);
                break;
            case "Por Estado":
                txtBusqueda.setEnabled(false);
                cbCategoria.setEnabled(false);
                cbEstado.setEnabled(true);
                break;
        }
    }

    private void realizarBusqueda() {
        modeloTabla.setRowCount(0);
        List<Content> resultados = null;
        String tipoBusqueda = (String) cbTipoBusqueda.getSelectedItem();

        try {
            switch (tipoBusqueda) {
                case "Texto libre":
                    String criterio = txtBusqueda.getText().trim();
                    if (!criterio.isEmpty()) {
                        resultados = controller.buscarContenidos(criterio);
                    }
                    break;
                    
                case "Por Tipo":
                    String tipo = txtBusqueda.getText().trim();
                    if (!tipo.isEmpty()) {
                        resultados = controller.filtrarPorTipo(tipo);
                    }
                    break;
                    
                case "Por Categoría":
                    String nombreCat = (String) cbCategoria.getSelectedItem();
                    if (nombreCat != null && !nombreCat.equals("Todas")) {
                        Category categoria = controller.findCategoryByName(nombreCat);
                        resultados = controller.filtrarPorCategoria(categoria);
                    } else {
                        resultados = controller.getContenidos();
                    }
                    break;
                    
                case "Por Estado":
                    String estadoStr = (String) cbEstado.getSelectedItem();
                    if (!estadoStr.equals("Todos")) {
                        State estado = State.valueOf(estadoStr);
                        resultados = controller.filtrarPorEstado(estado);
                    } else {
                        resultados = controller.getContenidos();
                    }
                    break;
            }

            if (resultados != null) {
                for (Content c : resultados) {
                    Object[] fila = {
                        c.getId(),
                        c.getClass().getSimpleName(),
                        c.getTitle(),
                        c.getAuthor(),
                        c.getCategory() != null ? c.getCategory().getName() : "Sin categoría",
                        c.getState().toString()
                    };
                    modeloTabla.addRow(fila);
                }
                lblResultados.setText("Resultados: " + resultados.size() + " contenidos encontrados");
            } else {
                lblResultados.setText("Resultados: 0 contenidos encontrados");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                this,
                "Error al buscar: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void limpiarBusqueda() {
        txtBusqueda.setText("");
        cbCategoria.setSelectedIndex(0);
        cbEstado.setSelectedIndex(0);
        cbTipoBusqueda.setSelectedIndex(0);
        modeloTabla.setRowCount(0);
        lblResultados.setText("Resultados: 0 contenidos encontrados");
    }
}