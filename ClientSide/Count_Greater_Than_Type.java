package ClientSide;

import Communication.Command;
import Communication.Response;
import Organizations.OrganizationType;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

class Count_Greater_Than_Type_Frame extends JFrame implements ActionListener {

    Container container = getContentPane();
    JLabel nameLabel = new JLabel("Type");
    String[] items = {"COMMERCIAL", "PUBLIC", "TRUST"};
    JComboBox comboBox = new JComboBox(items);
    JButton button = new JButton("COUNT GREATER THAN TYPE");

    ObjectInputStream GetfromServer;
    ObjectOutputStream SendtoServer;

    Count_Greater_Than_Type_Frame(ObjectOutputStream SendtoServer, ObjectInputStream GetfromServer) {
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
        comboBox.setBounds(30, 75, 290, 30);

        button.setBounds(150, 150, 200, 33);
//      button.setBackground(new Color(255,99,71));
    }

    public void addComponentsToContainer() {
        container.add(nameLabel);
        container.add(comboBox);
        container.add(button);
    }

    public void addActionEvent() {
        button.addActionListener(this);
        comboBox.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == comboBox)
            {
                comboBox.setBackground(new Color(150, 255, 150));
                button.setBackground(new Color(150, 255, 150));
            }
            if (e.getSource() == button) {
                OrganizationType type = OrganizationType.valueOf((String)comboBox.getSelectedItem());
                SendtoServer.writeObject(new Command("count_greater_than_type " + type, false));
                JOptionPane.showMessageDialog(this, ((Response) GetfromServer.readObject()).content);
                this.dispose();
            }
        } catch (Exception aaaa) {
            aaaa.printStackTrace();
        }
    }
}
class Count_Greater_Than_Type_Form {
    public static void main(ObjectOutputStream SendtoServer, ObjectInputStream GetfromServer) {
        Count_Greater_Than_Type_Frame frame = new Count_Greater_Than_Type_Frame(SendtoServer, GetfromServer);
        frame.setTitle("Count greater than type Window");
        frame.setVisible(true);
        frame.setBounds(10, 10, 480, 360);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
    }
}