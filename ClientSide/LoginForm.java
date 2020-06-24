package ClientSide;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;

class LoginFrame extends JFrame implements ActionListener {

    Container container = getContentPane();
    JLabel userLabel = new JLabel("USERNAME");
    JLabel passwordLabel = new JLabel("PASSWORD");
    JTextField userTextField = new JTextField();
    JPasswordField passwordField = new JPasswordField();
    JButton loginButton = new JButton("LOGIN");
    JButton registerButton = new JButton("REGISTER");
    JCheckBox showPassword = new JCheckBox("Show Password");

    ObjectInputStream GetfromServer;
    ObjectOutputStream SendtoServer;


    LoginFrame(ObjectOutputStream SendtoServer, ObjectInputStream GetfromServer) {
        setLayoutManager();
        setLocationAndSize();
        addComponentsToContainer();
        addActionEvent();
        this.GetfromServer = GetfromServer;
        this.SendtoServer = SendtoServer;

    }

    public void setLayoutManager() {
        container.setLayout(null);
    }

    public void setLocationAndSize() {
        userLabel.setBounds(50, 150, 100, 30);
        passwordLabel.setBounds(50, 220, 100, 30);
        userTextField.setBounds(150, 150, 150, 30);
        passwordField.setBounds(150, 220, 150, 30);
        showPassword.setBounds(150, 250, 150, 30);
        loginButton.setBounds(50, 300, 100, 30);
        registerButton.setBounds(200, 300, 100, 30);


    }

    public void addComponentsToContainer() {
        container.add(userLabel);
        container.add(passwordLabel);
        container.add(userTextField);
        container.add(passwordField);
        container.add(showPassword);
        container.add(loginButton);
        container.add(registerButton);
    }

    public void addActionEvent() {
        loginButton.addActionListener(this);
        registerButton.addActionListener(this);
        showPassword.addActionListener(this);
        userTextField.addActionListener(this);
        userTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { colorize(); }
            @Override
            public void removeUpdate(DocumentEvent e) { colorize(); }
            @Override
            public void changedUpdate(DocumentEvent e) { colorize(); }

            public void colorize(){
                if (userTextField.getText().equals("yyy"))
                    userTextField.setBackground(new Color(150, 255, 150));
                else
                    userTextField.setBackground(Color.WHITE);
            }
        });
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == loginButton) {

                String login = userTextField.getText();
                String password = passwordField.getText();
                SendtoServer.writeObject("login");
                SendtoServer.writeObject(login);
                byte[] a = MessageDigest.getInstance("SHA-384").digest(password.getBytes());
                SendtoServer.writeObject(new String(a));
                String response = (String) GetfromServer.readObject();
                if (response.equals("Ok")) {
                    System.out.println("Вы успешно вошли. Для получения справки по доступным командам введите help");
                    Client.login_button = true;
                    this.dispose();
                } else
                    JOptionPane.showMessageDialog(this, response);

            }

            if (e.getSource() == showPassword) {
                if (showPassword.isSelected()) {
                    passwordField.setEchoChar((char) 0);
                } else {
                    passwordField.setEchoChar('*');
                }
            }


                if (e.getSource() == registerButton) {
                String login = userTextField.getText();
                String password = passwordField.getText();

                SendtoServer.writeObject("reg");
                SendtoServer.writeObject(login);
                byte[] a = MessageDigest.getInstance("SHA-384").digest(password.getBytes());
                SendtoServer.writeObject(new String(a));
                String response = (String) GetfromServer.readObject();
                if (response.equals("Ok")) {
                    Client.login_button = true;
                    this.dispose();
                } else
                    JOptionPane.showMessageDialog(this, response);
            }
        } catch (Exception aaaa)
        {
            aaaa.printStackTrace();
        }

    }

}
public class LoginForm {
    public static void main(ObjectOutputStream SendtoServer, ObjectInputStream GetfromServer) {
        LoginFrame frame = new LoginFrame(SendtoServer, GetfromServer);
        frame.setTitle("Login Form");
        frame.setVisible(true);
        frame.setBounds(10, 10, 370, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

    }

}