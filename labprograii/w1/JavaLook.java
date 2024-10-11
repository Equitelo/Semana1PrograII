package labprograii.w1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class JavaLook extends JFrame {
    private EmailAccount[] accounts = new EmailAccount[50];
    private EmailAccount accountActual = null;
    private JPanel cards;
    private CardLayout cardLayout;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JavaLook().setVisible(true));
    }

    public JavaLook() {
        setTitle("Sistema de Correos");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        // Panel de inicio
        JPanel loginPanel = createLoginPanel();
        cards.add(loginPanel, "LOGIN");

        // Panel principal
        JPanel mainPanel = createMainPanel();
        cards.add(mainPanel, "MAIN");

        add(cards);
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JButton loginButton = new JButton("LOGIN");
        JButton createButton = new JButton("CREAR ACCOUNT");

        loginButton.addActionListener(e -> mostrarLoginDialog());
        createButton.addActionListener(e -> mostrarCreateAccountDialog());

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(loginButton, gbc);

        gbc.gridy = 1;
        panel.add(createButton, gbc);

        return panel;
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        String[] options = {
            "VER MI INBOX",
            "MANDAR CORREO",
            "LEER UN CORREO",
            "LIMPIAR MI INBOX",
            "CERRAR SESION"
        };

        for (int i = 0; i < options.length; i++) {
            JButton button = new JButton(options[i]);
            final int option = i;
            button.addActionListener(e -> handleMainMenuOption(option));
            gbc.gridy = i;
            panel.add(button, gbc);
        }

        return panel;
    }

    private void handleMainMenuOption(int option) {
        switch (option) {
            case 0:
                verInbox();
                break;
            case 1:
                mandarCorreo();
                break;
            case 2:
                leerCorreo();
                break;
            case 3:
                limpiarInbox();
                break;
            case 4:
                cerrarSesion();
                break;
        }
    }

    private void mostrarLoginDialog() {
        JTextField emailField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);

        Object[] message = {
            "Email:", emailField,
            "Password:", passwordField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Login", 
                                                 JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            login(emailField.getText(), new String(passwordField.getPassword()));
        }
    }

    private void login(String email, String password) {
        for (EmailAccount account : accounts) {
            if (account != null && 
                account.getEmailAddress().equals(email) && 
                account.getPassword().equals(password)) {
                accountActual = account;
                cardLayout.show(cards, "MAIN");
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Credenciales inválidas");
    }

    private void mostrarCreateAccountDialog() {
        for (int i = 0; i < accounts.length; i++) {
            if (accounts[i] == null) {
                JTextField emailField = new JTextField(20);
                JTextField nameField = new JTextField(20);
                JPasswordField passwordField = new JPasswordField(20);

                Object[] message = {
                    "Email:", emailField,
                    "Nombre completo:", nameField,
                    "Password:", passwordField
                };

                int option = JOptionPane.showConfirmDialog(this, message, "Crear Cuenta", 
                                                         JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    if (createAccount(emailField.getText(), 
                                    new String(passwordField.getPassword()),
                                    nameField.getText(), i)) {
                        cardLayout.show(cards, "MAIN");
                    }
                }
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "No hay espacio para nuevas cuentas");
    }

    private boolean createAccount(String email, String password, String name, int index) {
        // Verificar si el email ya existe
        for (EmailAccount account : accounts) {
            if (account != null && account.getEmailAddress().equals(email)) {
                JOptionPane.showMessageDialog(this, "El email ya existe");
                return false;
            }
        }

        accounts[index] = new EmailAccount(email, password, name);
        accountActual = accounts[index];
        return true;
    }

    private void verInbox() {
        if (accountActual != null) {
            accountActual.printInbox();
        }
    }

    private void mandarCorreo() {
        if (accountActual == null) return;

        JTextField destinatarioField = new JTextField(20);
        JTextField asuntoField = new JTextField(20);
        JTextArea contenidoArea = new JTextArea(5, 20);

        Object[] message = {
            "Destinatario:", destinatarioField,
            "Asunto:", asuntoField,
            "Contenido:", new JScrollPane(contenidoArea)
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Enviar Correo", 
                                                 JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String destinatario = destinatarioField.getText();
            for (EmailAccount account : accounts) {
                if (account != null && account.getEmailAddress().equals(destinatario)) {
                    Email email = new Email(accountActual.getEmailAddress(),
                                          asuntoField.getText(),
                                          contenidoArea.getText());
                    boolean entregado = account.recibirEmail(email);
                    JOptionPane.showMessageDialog(this, 
                        entregado ? "Correo enviado" : "No se pudo entregar el correo");
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Destinatario no encontrado");
        }
    }

    private void leerCorreo() {
        if (accountActual == null) return;

        String posStr = JOptionPane.showInputDialog(this, 
            "Ingrese la posición del correo a leer:");
        try {
            int pos = Integer.parseInt(posStr);
            accountActual.leerEmail(pos);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Posición inválida");
        }
    }

    private void limpiarInbox() {
        if (accountActual != null) {
            accountActual.borrarLeidos();
            JOptionPane.showMessageDialog(this, "Inbox limpiado");
        }
    }

    private void cerrarSesion() {
        accountActual = null;
        cardLayout.show(cards, "LOGIN");
    }
       }