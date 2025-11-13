import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PanelListaContenidos extends JPanel {
    private CMSController controller;
    private MainFrame mainFrame;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JButton btnEliminar, btnPublicar, btnDetalle;

    public PanelListaContenidos(CMSController controller, MainFrame mainFrame) {
        this.controller = controller;
        this.mainFrame = mainFrame;
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel("Lista de Contenidos");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        add(lblTitulo, BorderLayout.NORTH);

        String[] columnas = {"ID", "Tipo", "Título", "Autor", "Categoría", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(modeloTabla);
        tabla.setFont(new Font("Arial", Font.PLAIN, 14));
        tabla.setRowHeight(30);
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tabla.getTableHeader().setBackground(new Color(52, 58, 64));
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(tabla);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setBackground(Color.WHITE);

        btnDetalle = crearBoton("Ver Detalle", new Color(23, 162, 184));
        btnDetalle.addActionListener(e -> verDetalle());
        panelBotones.add(btnDetalle);

        btnPublicar = crearBoton("Publicar", new Color(40, 167, 69));
        btnPublicar.addActionListener(e -> publicarContenido());
        panelBotones.add(btnPublicar);

        btnEliminar = crearBoton("Eliminar", new Color(220, 53, 69));
        btnEliminar.addActionListener(e -> eliminarContenido());
        panelBotones.add(btnEliminar);

        add(panelBotones, BorderLayout.SOUTH);

        actualizarTabla();
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

    public void actualizarTabla() {
        modeloTabla.setRowCount(0);
        List<Content> contenidos = controller.getContenidos();
        
        for (Content c : contenidos) {
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
    }

    private void verDetalle() {
        int filaSeleccionada = tabla.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un contenido", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) tabla.getValueAt(filaSeleccionada, 0);
        
        try {
            Content contenido = controller.findByIdOrThrow(id);
            DialogoDetalleContenido dialogo = new DialogoDetalleContenido(
                (JFrame) SwingUtilities.getWindowAncestor(this),
                contenido
            );
            dialogo.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void publicarContenido() {
        int filaSeleccionada = tabla.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un contenido", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) tabla.getValueAt(filaSeleccionada, 0);
        
        try {
            boolean publicado = controller.publish(id);
            if (publicado) {
                JOptionPane.showMessageDialog(this, "Contenido publicado exitosamente", "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);
                actualizarTabla();
            } else {
                JOptionPane.showMessageDialog(this, "El contenido ya estaba publicado", "Información", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarContenido() {
        int filaSeleccionada = tabla.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un contenido", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(
            this,
            "¿Está seguro que desea eliminar este contenido?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (respuesta == JOptionPane.YES_OPTION) {
            int id = (int) tabla.getValueAt(filaSeleccionada, 0);
            
            try {
                controller.delete(id);
                JOptionPane.showMessageDialog(this, "Contenido eliminado exitosamente", "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);
                actualizarTabla();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}