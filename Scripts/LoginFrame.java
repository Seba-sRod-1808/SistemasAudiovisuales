import java.awt.*;
import javax.swing.*;

public class LoginFrame extends JFrame {
    private CMSController controller;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JLabel lblError;

    public LoginFrame(CMSController controller) {
        this.controller = controller;
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setTitle("Sistema CMS - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        panelPrincipal.setBackground(new Color(240, 240, 240));

        JLabel lblTitulo = new JLabel("Sistema CMS - EGA", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(50, 50, 50));
        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(new Font("Arial", Font.PLAIN, 14));
        panelFormulario.add(lblEmail, gbc);

        gbc.gridx = 1;
        txtEmail = new JTextField(20);
        txtEmail.setFont(new Font("Arial", Font.PLAIN, 14));
        panelFormulario.add(txtEmail, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        panelFormulario.add(lblPassword, gbc);

        gbc.gridx = 1;
        txtPassword = new JPasswordField(20);
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        panelFormulario.add(txtPassword, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 5, 5, 5);
        btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogin.setBackground(new Color(0, 123, 255));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.addActionListener(e -> intentarLogin());
        panelFormulario.add(btnLogin, gbc);

        gbc.gridy = 3;
        gbc.insets = new Insets(10, 5, 5, 5);
        lblError = new JLabel("", SwingConstants.CENTER);
        lblError.setForeground(Color.RED);
        lblError.setFont(new Font("Arial", Font.PLAIN, 12));
        panelFormulario.add(lblError, gbc);

        panelPrincipal.add(panelFormulario, BorderLayout.CENTER);

        add(panelPrincipal);

        txtPassword.addActionListener(e -> intentarLogin());
    }

    private void intentarLogin() {
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            lblError.setText("Por favor complete todos los campos");
            return;
        }

        try {
            controller.login(email, password);
            MainFrame mainFrame = new MainFrame(controller);
            mainFrame.setVisible(true);
            this.dispose();
        } catch (AutenticacionException ex) {
            lblError.setText(ex.getMessage());
        }
    }

    public void mostrar() {
        setVisible(true);
    }
}