package ClientSide;

import Communication.Command;
import Communication.Response;
import Organizations.Address;
import Organizations.Coordinates;
import Organizations.OrganizationType;
import Organizations.OrganizationWrap;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

class RemoveLowerFrame extends JFrame implements ActionListener {

    Container container = getContentPane();
    JLabel nameLabel = new JLabel("Name");
    JTextField nameField = new JTextField();
    JButton RemoveLowerButton = new JButton("REMOVE LOWER");

    ObjectInputStream GetfromServer;
    ObjectOutputStream SendtoServer;

    RemoveLowerFrame(ObjectOutputStream SendtoServer, ObjectInputStream GetfromServer) {
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
        nameLabel.setBounds(30, 50, 290, 20);
        nameField.setBounds(30, 75, 290, 30);

        RemoveLowerButton.setBounds(150, 150, 100, 50);
        RemoveLowerButton.setBackground(new Color(255,99,71));
    }

    public void addComponentsToContainer() {
        container.add(nameLabel);
        container.add(nameField);
        container.add(RemoveLowerButton);
    }

    public void addActionEvent() {
        RemoveLowerButton.addActionListener(this);

        nameField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {colorize();}
            public void removeUpdate(DocumentEvent e) {colorize();}
            public void changedUpdate(DocumentEvent e) {colorize();}

            void colorize(){
                if (nameField.getText().isEmpty())
                {
                    nameField.setBackground(new Color(255,99,71));
                    nameField.setToolTipText("Enter name");
                    RemoveLowerButton.setBackground(new Color(255,99,71));
                }
                else
                {
                    nameField.setBackground(new Color(150, 255, 150));
                    RemoveLowerButton.setBackground(new Color(150, 255, 150));
                }
            }

        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == RemoveLowerButton) {
                String name = nameField.getText();
                SendtoServer.writeObject(new Command("remove_lower "+name, false));
                JOptionPane.showMessageDialog(this, ((Response) GetfromServer.readObject()).content);
                this.dispose();
            }
        } catch (Exception aaaa) {
            aaaa.printStackTrace();
        }
    }
}
class RemoveLowerForm {
    public static void main(ObjectOutputStream SendtoServer, ObjectInputStream GetfromServer) {
        RemoveLowerFrame frame = new RemoveLowerFrame(SendtoServer, GetfromServer);
        frame.setTitle("Remove Lower Window");
        frame.setVisible(true);
        frame.setBounds(10, 10, 480, 360);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
    }
}