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

class AddifminFrame extends JFrame implements ActionListener {

    Container container = getContentPane();
    JLabel nameLabel = new JLabel("Name");
    JLabel cordsLabel = new JLabel("Coordinates");
    JLabel turnoverLabel = new JLabel("Annual Turnover");
    JLabel employees_countLabel = new JLabel("Employees Count");
    JLabel typeLabel = new JLabel("Type");
    JLabel addressLabel = new JLabel("Official Addddress");
    JTextField nameField = new JTextField();
    JTextField cordsField = new JTextField();
    private JTextField turnoverField = new JTextField();
    JTextField employees_countField = new JTextField();
    String[] items = {"COMMERCIAL", "PUBLIC", "TRUST"};
    JComboBox comboBox = new JComboBox(items);
    JTextField addressField = new JTextField();
    JButton addButton = new JButton("ADD_IF_MIN");

    ObjectInputStream GetfromServer;
    ObjectOutputStream SendtoServer;

    HashMap<String, Boolean> Validator = new HashMap<String, Boolean>();

    AddifminFrame(ObjectOutputStream SendtoServer, ObjectInputStream GetfromServer) {
        setLayoutManager();
        setLocationAndSize();
        addComponentsToContainer();
        addActionEvent();
        this.GetfromServer = GetfromServer;
        this.SendtoServer = SendtoServer;

        Validator.put("Name", false);
        Validator.put("Coords", false);
        Validator.put("Turnover", false);
        Validator.put("Employees", false);
        Validator.put("Type", false);
        Validator.put("Address", false);

    }

    public void setLayoutManager() {
        container.setLayout(null);
    }

    public void setLocationAndSize() {
        nameLabel.setBounds(30, 50, 290, 20);
        nameField.setBounds(30, 75, 290, 30);

        cordsLabel.setBounds(30, 120, 290, 20);
        cordsField.setBounds(30, 145, 290, 30);

        turnoverLabel.setBounds(30, 190, 290, 20);
        turnoverField.setBounds(30, 215, 290, 30);

        employees_countLabel.setBounds(390, 50, 290, 20);
        employees_countField.setBounds(390, 75, 290, 30);

        typeLabel.setBounds(390, 120, 290, 20);
        comboBox.setBounds(390,145,290,30);

        addressLabel.setBounds(390, 190, 290, 20);
        addressField.setBounds(390, 215, 290, 30);

        addButton.setBounds(255, 400, 210, 80);
        addButton.setBackground(new Color(255,99,71));
    }

    public void addComponentsToContainer() {
        container.add(nameLabel);
        container.add(cordsLabel);
        container.add(turnoverLabel);
        container.add(employees_countLabel);
        container.add(typeLabel);
        container.add(addressLabel);

        container.add(nameField);
        container.add(cordsField);
        container.add(turnoverField);
        container.add(employees_countField);
        container.add(comboBox);
        container.add(addressField);

        container.add(addButton);
    }

    public void addActionEvent() {
        addButton.addActionListener(this);

        nameField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {colorize();}
            public void removeUpdate(DocumentEvent e) {colorize();}
            public void changedUpdate(DocumentEvent e) {colorize();}

            void colorize(){
                if (nameField.getText().isEmpty())
                {
                    nameField.setBackground(new Color(255,99,71));
                    nameField.setToolTipText("Enter name");
                    Validator.replace("Name", false);
                    addButton.setBackground(new Color(150, 255, 150));
                }
                else
                {
                    nameField.setBackground(new Color(150, 255, 150));
                    Validator.replace("Name", true);

                    for (String a: Validator.keySet())
                    {
                        if (!Validator.get(a)) return;
                    }
                    addButton.setBackground(new Color(150, 255, 150));
                }
            }

        });

        cordsField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {colorize();}
            public void removeUpdate(DocumentEvent e) {colorize();}
            public void changedUpdate(DocumentEvent e) {colorize();}

            void colorize(){

                try{
                    String[] coords = cordsField.getText().split(" ");
                    long x;
                    int y;
                    x = Long.parseLong(coords[0]);
                    y = Integer.parseInt(coords[1]);
                    if (x <= -904 || y > 551 || x > 9.01E18 || y < -2147483647) x = 1 / 0;
                } catch (Exception e) {
                    cordsField.setBackground(new Color(255,99,71));
                    cordsField.setToolTipText("Enter coords");
                    Validator.replace("Coords", false);
                    addButton.setBackground(new Color(255,99,71));
                    return;
                }

                cordsField.setBackground(new Color(150, 255, 150));
                Validator.replace("Coords", true);

                for (String a: Validator.keySet())
                {
                    if (!Validator.get(a)) return;
                }
                addButton.setBackground(new Color(150, 255, 150));

            }

        });

        turnoverField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {colorize();}
            public void removeUpdate(DocumentEvent e) {colorize();}
            public void changedUpdate(DocumentEvent e) {colorize();}
            void colorize() {
                try{
                    float annualTurnover = Float.parseFloat(turnoverField.getText());
                    if ( annualTurnover < 0|| annualTurnover>= 3.301E38) annualTurnover = 1 / 0;
                } catch (Exception e) {
                    turnoverField.setBackground(new Color(255,99,71));
                    turnoverField.setToolTipText("Enter Turnover");
                    Validator.replace("Turnover", false);
                    addButton.setBackground(new Color(255,99,71));
                    return;
                }

                turnoverField.setBackground(new Color(150, 255, 150));
                Validator.replace("Turnover", true);

                for (String a: Validator.keySet())
                {
                    if (!Validator.get(a)) return;
                }
                addButton.setBackground(new Color(150, 255, 150));
            }
        });

        employees_countField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {colorize(); }
            public void removeUpdate(DocumentEvent e) {colorize(); }
            public void changedUpdate(DocumentEvent e) {colorize();}
            void colorize() {
                try {
                    Integer employeesCount = Integer.parseInt(employees_countField.getText());
                    if (employeesCount <= 0 || employeesCount >= 2147483647) employeesCount = 1 / 0;
                } catch (Exception e) {
                    employees_countField.setBackground(new Color(255,99,71));
                    employees_countField.setToolTipText("Enter employees");
                    Validator.replace("Employees", false);
                    addButton.setBackground(new Color(255,99,71));

                    return;
                }
                employees_countField.setBackground(new Color(150, 255, 150));
                Validator.replace("Employees", true);

                for (String a: Validator.keySet())
                {
                    if (!Validator.get(a)) return;
                }
                addButton.setBackground(new Color(150, 255, 150));
            }
        });
        comboBox.addActionListener(this);

        addressField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {colorize();}
            public void removeUpdate(DocumentEvent e) {colorize();}
            public void changedUpdate(DocumentEvent e) {colorize();}
            void colorize() {
                Address officialAddress = null;
                try {
                    String zipCode = addressField.getText();
                    if (zipCode.length() > 15) {zipCode=null;}
                    officialAddress = new Address(zipCode);
                } catch (Exception e) {
                    addressField.setBackground(new Color(255,99,71));
                    addressField.setToolTipText("");
                    Validator.replace("Address", false);
                    addButton.setBackground(new Color(255,99,71));
                    return;
                }
                addressField.setBackground(new Color(150, 255, 150));
                Validator.replace("Address", true);

                for (String a: Validator.keySet())
                {
                    if (!Validator.get(a)) return;
                }
                addButton.setBackground(new Color(150, 255, 150));

            }
        });
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        try {

            if (e.getSource() == comboBox)
            {
                String item = (String)comboBox.getSelectedItem();
                comboBox.setBackground(new Color(150, 255, 150));
                Validator.replace("Type", true);

                for (String a: Validator.keySet()) { if (!Validator.get(a)) return; }
                addButton.setBackground(new Color(150, 255, 150));
            }

            if (e.getSource() == addButton) {

                for (String a: Validator.keySet())
                {
                    if (!Validator.get(a)) return;
                }

                String name = nameField.getText();
                String cord= cordsField.getText();
                long x = 0;
                int y = 0;
                String[] coords;

                try {
                    coords = cord.split(" ", 2);
                    x = Long.parseLong(coords[0]);
                    y = Integer.parseInt(coords[1]);
                } catch (Exception aaaaaaa) {
                    ;
                }

                Coordinates newcoords = new Coordinates(x, y);

                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String date = now.format(formatter);

                float annualTurnover = Float.parseFloat(turnoverField.getText());

                Integer employeesCount = Integer.parseInt(employees_countField.getText());

                OrganizationType type = OrganizationType.valueOf((String)comboBox.getSelectedItem());

                Address officialAddress = new Address(addressField.getText());

                OrganizationWrap org = new OrganizationWrap(name, newcoords, date, annualTurnover, employeesCount, type, officialAddress);

                SendtoServer.writeObject(new Command("add_if_min", true));
                SendtoServer.writeObject(org);
                JOptionPane.showMessageDialog(this, ((Response) GetfromServer.readObject()).content);
                this.dispose();
            }

        } catch (Exception aaaa) {
            aaaa.printStackTrace();
        }

    }

}
public class AddifminForm {
    public static void main(ObjectOutputStream SendtoServer, ObjectInputStream GetfromServer) {
        AddifminFrame frame = new AddifminFrame(SendtoServer, GetfromServer);
        frame.setTitle("Add if min Window");
        frame.setVisible(true);
        frame.setBounds(10, 10, 720, 560);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
    }
}