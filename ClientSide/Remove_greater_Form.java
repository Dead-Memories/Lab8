package ClientSide;

import Communication.Command;
import Communication.Response;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

class RemoveGreaterFrame extends JFrame implements ActionListener {

    Container container = getContentPane();
    JLabel nameLabel = new JLabel("Name");
    JTextField nameField = new JTextField();
    JButton RemoveGreaterButton = new JButton("REMOVE GREATER");

    ObjectInputStream GetfromServer;
    ObjectOutputStream SendtoServer;

    RemoveGreaterFrame(ObjectOutputStream SendtoServer, ObjectInputStream GetfromServer) {
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

        RemoveGreaterButton.setBounds(150, 150, 100, 50);
        RemoveGreaterButton.setBackground(new Color(255,99,71));
    }

    public void addComponentsToContainer() {
        container.add(nameLabel);
        container.add(nameField);
        container.add(RemoveGreaterButton);
    }

    public void addActionEvent() {
        RemoveGreaterButton.addActionListener(this);

        nameField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {colorize();}
            public void removeUpdate(DocumentEvent e) {colorize();}
            public void changedUpdate(DocumentEvent e) {colorize();}

            void colorize(){
                if (nameField.getText().isEmpty())
                {
                    nameField.setBackground(new Color(255,99,71));
                    nameField.setToolTipText("Enter name");
                    RemoveGreaterButton.setBackground(new Color(255,99,71));
                }
                else
                {
//                    String name = nameField.getText();
                    nameField.setBackground(new Color(150, 255, 150));
                    RemoveGreaterButton.setBackground(new Color(150, 255, 150));
                }
            }

        }); }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == RemoveGreaterButton) {
                String name = nameField.getText();
                SendtoServer.writeObject(new Command("remove_greater "+name, false));
                JOptionPane.showMessageDialog(this, ((Response) GetfromServer.readObject()).content);
                this.dispose();
            }
        } catch (Exception aaaa) {
            aaaa.printStackTrace();
        }
    }
}
class RemoveGreaterForm {
    public static void main(ObjectOutputStream SendtoServer, ObjectInputStream GetfromServer) {
        RemoveGreaterFrame frame = new RemoveGreaterFrame(SendtoServer, GetfromServer);
        frame.setTitle("Remove Greater Window");
        frame.setVisible(true);
        frame.setBounds(10, 10, 480, 360);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
    }
}