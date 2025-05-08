import javax.swing.*;
import java.awt.*;

public class RegisterPanel extends JPanel {
    public RegisterPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel registerBodyLeft = new JPanel(new BorderLayout());
        registerBodyLeft.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        registerBodyLeft.setBackground(new Color(0x64A48C));
        registerBodyLeft.setPreferredSize(new Dimension(550, registerBodyLeft.getPreferredSize().height));

        ImageIcon bgImage = new ImageIcon(getClass().getResource("register-bg2.png"));
        JLabel bgImageLabel = new JLabel(bgImage);
        registerBodyLeft.add(bgImageLabel, BorderLayout.NORTH);

        JPanel registerBodyRight = new JPanel(new GridBagLayout());
        registerBodyRight.setBackground(Color.WHITE);

        JPanel registerFormPanel = new JPanel();
        registerFormPanel.setLayout(new BoxLayout(registerFormPanel, BoxLayout.Y_AXIS));
        registerFormPanel.setBackground(Color.WHITE);
        registerFormPanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 20, 20));

        JLabel headTxt = new JLabel("WOODORA FURNITURE");
        headTxt.setFont(new Font("Arial", Font.BOLD, 25));
        headTxt.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerFormPanel.add(headTxt);
        registerFormPanel.add(Box.createVerticalStrut(20));

        JLabel welcomeTxt = new JLabel("Create your account");
        welcomeTxt.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeTxt.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerFormPanel.add(welcomeTxt);
        registerFormPanel.add(Box.createVerticalStrut(20));

        JTextField nameInput = new JTextField("Full Name");
        stylePlaceholderField(nameInput, "Full Name");
        registerFormPanel.add(nameInput);
        registerFormPanel.add(Box.createVerticalStrut(10));

        JTextField emailInput = new JTextField("Email");
        stylePlaceholderField(emailInput, "Email");
        registerFormPanel.add(emailInput);
        registerFormPanel.add(Box.createVerticalStrut(10));

        JPasswordField password = new JPasswordField("Password");
        stylePasswordPlaceholder(password, "Password");
        registerFormPanel.add(password);
        registerFormPanel.add(Box.createVerticalStrut(20));

        JPanel bottomForm = new JPanel(new GridLayout(1, 2, 10, 0));
        bottomForm.setBackground(Color.WHITE);

        JPanel agreementPan = new JPanel(new FlowLayout());
        agreementPan.setBackground(Color.WHITE);
        JCheckBox checkBox = new JCheckBox();
        checkBox.setBackground(Color.WHITE);
        JLabel agree = new JLabel("I agree with terms & privacy");
        agree.setFont(new Font("SansSerif", Font.BOLD, 16));
        agreementPan.add(checkBox);
        agreementPan.add(agree);
        bottomForm.add(agreementPan);

        JButton registerBtn = new JButton("REGISTER");
        registerBtn.setFont(new Font("SansSerif", Font.BOLD, 18));
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setBackground(new Color(0x64A48C));
        registerBtn.setFocusable(false);
        bottomForm.add(registerBtn);

        registerFormPanel.add(bottomForm);
        registerFormPanel.add(Box.createVerticalStrut(20));

        JPanel signinPanel = new JPanel();
        signinPanel.setLayout(new BoxLayout(signinPanel, BoxLayout.Y_AXIS));
        signinPanel.setBackground(new Color(0xF0F0F0));
        signinPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0x64A48C), 2, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel alreadyRegged = new JLabel("Already Registered?");
        alreadyRegged.setFont(new Font("SansSerif", Font.BOLD, 16));
        alreadyRegged.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton signinBtn = new JButton("SIGN IN");
        signinBtn.setFont(new Font("SansSerif", Font.BOLD, 18));
        signinBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        signinBtn.setBackground(new Color(0x64A48C));
        signinBtn.setForeground(Color.WHITE);
        signinBtn.setFocusable(false);

        signinPanel.add(alreadyRegged);
        signinPanel.add(Box.createVerticalStrut(10));
        signinPanel.add(signinBtn);

        registerFormPanel.add(Box.createVerticalStrut(20));
        registerFormPanel.add(signinPanel);

        registerBodyRight.add(registerFormPanel);

        add(registerBodyLeft, BorderLayout.WEST);
        add(registerBodyRight, BorderLayout.CENTER);

        registerBtn.addActionListener(e -> {
            MainFrame.cardLayout.show(MainFrame.cards, "home");
        });

        signinBtn.addActionListener(e -> {
            MainFrame.cardLayout.show(MainFrame.cards, "login");
        });
    }

    private void stylePlaceholderField(JTextField field, String placeholder) {
        field.setPreferredSize(new Dimension(300, 40));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setFont(new Font("SansSerif", Font.PLAIN, 16));
        field.setForeground(Color.GRAY);
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (field.getText().isEmpty()) {
                    field.setForeground(Color.GRAY);
                    field.setText(placeholder);
                }
            }
        });
    }

    private void stylePasswordPlaceholder(JPasswordField field, String placeholder) {
        field.setPreferredSize(new Dimension(300, 40));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setFont(new Font("SansSerif", Font.PLAIN, 16));
        field.setForeground(Color.GRAY);
        field.setEchoChar((char) 0);
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (String.valueOf(field.getPassword()).equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                    field.setEchoChar('•');
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (String.valueOf(field.getPassword()).isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);
                    field.setEchoChar((char) 0);
                }
            }
        });
        field.setText(placeholder);
    }
}
