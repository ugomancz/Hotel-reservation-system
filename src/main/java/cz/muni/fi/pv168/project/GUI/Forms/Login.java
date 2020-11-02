package cz.muni.fi.pv168.project.GUI.Forms;

import cz.muni.fi.pv168.project.Main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame implements ActionListener {
    JLabel userLabel = new JLabel("Username:");
    JLabel passwordLabel = new JLabel("Password:");
    JTextField username = new JTextField();
    JPasswordField password = new JPasswordField();
    JButton loginButton = new JButton("Login");

    public Login() {
        super("Login");
        this.setSize(235, 130);
        this.setLayout(null);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getRootPane().setDefaultButton(loginButton);
        this.setVisible(true);

        userLabel.setBounds(10, 10, 80, 20);
        passwordLabel.setBounds(10, 30, 80, 20);
        username.setBounds(90, 10, 120, 20);
        password.setBounds(90, 30, 120, 20);
        loginButton.setBounds(10, 60, 80, 25);

        loginButton.addActionListener(this);

        this.add(userLabel);
        this.add(passwordLabel);
        this.add(username);
        this.add(password);
        this.add(loginButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            if (loginSuccess(username.getText(), password.getText())) {
                Main.frame.setVisible(true);
            }
        }
    }

    private boolean loginSuccess(String username, String password) {
        System.out.println(username + ", " + password);
        return true;
    }
}
