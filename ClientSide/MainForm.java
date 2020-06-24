package ClientSide;
import Communication.Command;
import Communication.Response;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

class MainFrame extends JFrame implements ActionListener, TableModelListener {
    ArrayList<HouseDrawer> houseDrawer = new ArrayList<HouseDrawer>();
    Random rand = new Random();

    boolean busy = false;

    Locale loc = new Locale("ru", "Ru");
    ResourceBundle bundle = ResourceBundle.getBundle("dict", loc);
    HouseDrawer hd;


    JButton add_button = new JButton("ADD");
    JButton add_if_min_button = new JButton("ADD_IF_MIN");
    JButton help_button = new JButton("HELP");
    JButton info_button = new JButton("INFO");
    JButton clear_button = new JButton("CLEAR");
    JButton remove_greater_button = new JButton("REMOVE_GREATER");
    JButton remove_lower_button = new JButton("REMOVE_LOWER");
    JButton count_greater_than_type_button = new JButton("COUNT_GREATER_THAN_TYPE");
    JButton log_out_button = new JButton("LOG OUT");
    Profile prof;

    String[] items = {"ru (RU)", "tr (TR)", "es (HN)", "it (IT)"};
    JComboBox comboBox = new JComboBox(items);

    ArrayList<String []> current_orgs = new ArrayList<String[]>();
    String last_Update = LocalDateTime.MIN.toString();

    JLabel user = new JLabel();

    Random randomGet = new Random();

    String[] col = {"name", "id",  "coords", "creation date", "annual turnover", "employees count", "type", "address", "Creator"};
    DefaultTableModel tableModel = new DefaultTableModel(col, 0);
    JTable table = new JTable(tableModel) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    JScrollPane scrollPane = new JScrollPane(table);

    Container container = getContentPane();

    ObjectInputStream GetfromServer;
    ObjectOutputStream SendtoServer;

    MainFrame(ObjectOutputStream SendtoServer, ObjectInputStream GetfromServer) throws IOException, ClassNotFoundException {

        this.GetfromServer = GetfromServer;
        this.SendtoServer = SendtoServer;

        SendtoServer.writeObject(new Command("getUser", false));
        HashMap<String, String> login_data = (HashMap<String, String>) GetfromServer.readObject();

        this.prof = new Profile(login_data.get("login"), Long.parseLong(login_data.get("id")));

        RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableModel);
        table.setRowSorter(sorter);

        setLayoutManager();
        setLocationAndSize();
        addComponentsToContainer();
        addActionEvent();
        addEventListeners();


//        hd = new HouseDrawer(5, 5);
//        hd.setBounds(400, 700, 100, 100);
//        this.add(hd);

    }

    public void checkUpdate() throws IOException, ClassNotFoundException {
        SendtoServer.writeObject(new Command("getUpdate", false));

        String check_update = (String) GetfromServer.readObject();
        if (!last_Update.equals(check_update)) {
            updateTable();
            last_Update = check_update;
        }

    }

    public void updateTable() throws IOException, ClassNotFoundException {

        SendtoServer.writeObject(new Command("getData", false));
        ArrayList<String []> orgs = (ArrayList<String[]>) GetfromServer.readObject();

        ArrayList<String []> update_list = (ArrayList<String []>) orgs.stream().filter(o -> current_orgs.stream().noneMatch(t -> t[0].equals(o[0])
        &&t[2].equals(o[2])
        &&t[4].equals(o[4])
        &&t[5].equals(o[5])
        &&t[6].equals(o[6])
        &&t[7].equals(o[7]))).collect(Collectors.toList());
        ArrayList<String []> delete_list = (ArrayList<String []>) current_orgs.stream().filter(o -> orgs.stream().noneMatch(t -> t[0].equals(o[0])
                &&t[2].equals(o[2])
                &&t[4].equals(o[4])
                &&t[5].equals(o[5])
                &&t[6].equals(o[6])
                &&t[7].equals(o[7]))).collect(Collectors.toList());

//        System.out.println("DELETE LIST:");
//        delete_list.forEach(o -> System.out.println(o[0]));
//        System.out.println("UPDATE LIST:");
//        update_list.forEach(o -> System.out.println(o[0]));

        current_orgs = orgs;

        for (int i=tableModel.getRowCount()-1; i >= 0 ; i--) {

            int finalI = i;
            if (update_list.stream().anyMatch(o -> o[0].equals((String)tableModel.getValueAt(finalI, 0)))
            ||delete_list.stream().anyMatch(o -> o[0].equals((String)tableModel.getValueAt(finalI, 0))))
                tableModel.removeRow(i);
        }


        for (String [] o: update_list)
        {
            tableModel.addRow(o);
        }

        ArrayList<HouseDrawer> temp_array = new ArrayList<HouseDrawer>();
        for (HouseDrawer o:
             houseDrawer) {
            if (delete_list.stream().anyMatch(el -> el[0].equals(o.name)))
            {
                this.remove(o);
                temp_array.add(o);
            }
        }

        temp_array.forEach(o -> houseDrawer.remove(o));

        for (String[] o:
             update_list) {
            int user_id = Integer.parseInt(o[8]);
            houseDrawer.add(new HouseDrawer(o[0], 26, 10, new Color(user_id*200%255, user_id*150%255, user_id*44%255), o));
            houseDrawer.get(houseDrawer.size()-1).setBounds(60+Math.abs(Integer.parseInt(o[2].split(" ")[0]))%920, 450+Integer.parseInt(o[2].split(" ")[1])%300, 50, 50);
            this.add(houseDrawer.get(houseDrawer.size()-1));
        }

        this.repaint();

    }


    public void setLayoutManager() {
        container.setLayout(null);
    }

    public void setLocationAndSize() {

        add_button.setBounds(40, 20, 100,33);
        add_if_min_button.setBounds(160, 20, 150,33);
        help_button.setBounds(330,20,100,33);
        info_button.setBounds(450,20,100,33);
        clear_button.setBounds(570,20,100,33);
        remove_greater_button.setBounds(40,73, 170,33);
        remove_lower_button.setBounds(230, 73, 170,33);
        count_greater_than_type_button.setBounds(420,73,250,33);
        log_out_button.setBounds(840, 20, 100,33);
        scrollPane.setBounds(60, 200, 900, 200);
        prof.setBounds(690, 20, 130, 86);
        comboBox.setBounds(850, 73, 80, 33);
    }

    public void language_update(Locale nloc){
        bundle = ResourceBundle.getBundle("dict", nloc);

        add_button.setText(bundle.getString("ADD"));
        add_if_min_button.setText(bundle.getString("ADD_IF_MIN"));
        help_button.setText(bundle.getString("HELP"));
        info_button.setText(bundle.getString("INFO"));
        clear_button.setText(bundle.getString("CLEAR"));
        remove_greater_button.setText(bundle.getString("remove_greater"));
        remove_lower_button.setText(bundle.getString("remove_lower"));
        count_greater_than_type_button.setText(bundle.getString("count_greater_than_type"));
        log_out_button.setText(bundle.getString("LOG_OUT"));

        String[] col = {bundle.getString("Name"), "id",  bundle.getString("Coords"), bundle.getString("creation_date"),
                bundle.getString("Annual_Turnover"), bundle.getString("Employees_Count"), bundle.getString("Type"),
                bundle.getString("Official_Address"), bundle.getString("Creator")};

        tableModel.setColumnIdentifiers(col);
    }

    public void addComponentsToContainer() {
        container.add(add_button);
        container.add(add_if_min_button);
        container.add(help_button);
        container.add(info_button);
        container.add(clear_button);
        container.add(remove_greater_button);
        container.add(remove_lower_button);
        container.add(count_greater_than_type_button);
        container.add(log_out_button);
        container.add(scrollPane);
        container.add(prof);
        container.add(comboBox);
//        container.add(tablePanel);

    }

    public void addActionEvent() {
        add_button.addActionListener(this);
        add_if_min_button.addActionListener(this);
        help_button.addActionListener(this);
        info_button.addActionListener(this);
        clear_button.addActionListener(this);
        remove_greater_button.addActionListener(this);
        remove_lower_button.addActionListener(this);
        count_greater_than_type_button.addActionListener(this);
        log_out_button.addActionListener(this);
        comboBox.addActionListener(this);
    }

    void addEventListeners() {
        table.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {

                int r = table.rowAtPoint(e.getPoint());
                if (r >= 0 && r < table.getRowCount()) {
                    table.setRowSelectionInterval(r, r);
                } else {
                    table.clearSelection();
                }

                JMenuItem edit = new JMenuItem("Edit");
                JMenuItem delete = new JMenuItem("Delete");

                edit.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            EditorForm.main(SendtoServer, GetfromServer, Integer.parseInt((String) tableModel.getValueAt(r, 1)));
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        } catch (ClassNotFoundException ex) {
                            ex.printStackTrace();
                        }
                    }
                });

                delete.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String id = (String) tableModel.getValueAt(r, 1);
                        try {
                            SendtoServer.writeObject(new Command("remove_by_id "+ id, false));
                            JOptionPane.showMessageDialog(new Frame(), ((Response) GetfromServer.readObject()).content);
                        } catch (IOException | ClassNotFoundException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
                int rowindex = table.getSelectedRow();
                if (rowindex < 0) {
                    return;
                }
                if (e.getButton() == 3) {
                    JPopupMenu popup = new JPopupMenu();
                    popup.add(edit);
                    popup.add(delete);
                    popup.setVisible(true);
                    popup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        try{
            if (e.getSource()==comboBox)
            {
                switch ((String)comboBox.getSelectedItem())
                {
                    case "ru (RU)":
                        language_update(new Locale("ru", "RU"));
                        break;
                    case "it (IT)":
                        language_update(new Locale("it", "IT"));
                        break;
                    case "es (HN)":
                        language_update(new Locale("es", "HN"));
                        break;
                    case "tr (TR)":
                        language_update(new Locale("tr", "TR"));
                        break;
                }
            }

            if (e.getSource()==add_button)
            {
                this.busy = true;
                AddForm.main(SendtoServer, GetfromServer);
                this.busy = false;
                return;
            }

            if (e.getSource()==add_if_min_button)
            {
                this.busy = true;
                AddifminForm.main(SendtoServer, GetfromServer);
                this.busy = false;
                return;
            }

            if (e.getSource()==help_button)
            {
                this.busy = true;
                SendtoServer.writeObject(new Command("help", false));
                JOptionPane.showMessageDialog(this, ((Response) GetfromServer.readObject()).content);
                this.busy = false;
                return;
            }

            if (e.getSource()==info_button)
            {
                this.busy = true;
                SendtoServer.writeObject(new Command("info", false));
                JOptionPane.showMessageDialog(this, ((Response) GetfromServer.readObject()).content);
                this.busy = false;
                return;
            }

            if (e.getSource()==remove_lower_button)
            {
                this.busy = true;
                RemoveLowerForm.main(SendtoServer, GetfromServer);
                this.busy = false;
                return;
            }

            if (e.getSource()==remove_greater_button)
            {
                this.busy = true;
                RemoveGreaterForm.main(SendtoServer, GetfromServer);
                this.busy = false;
                return;
            }

            if (e.getSource()==log_out_button)
            {
                Client.logout_flag = false;
                System.exit(0);
                this.dispose();
                return;
            }

            if (e.getSource()==clear_button)
            {
                this.busy = true;
                SendtoServer.writeObject(new Command("clear", false));
                JOptionPane.showMessageDialog(this, ((Response) GetfromServer.readObject()).content);
                this.busy = false;
                return;
            }

            if (e.getSource()==count_greater_than_type_button)
            {
                this.busy = true;
                Count_Greater_Than_Type_Form.main(SendtoServer,GetfromServer);
                this.busy = false;
                return;
            }

        } catch (Exception vseploha)
        {
            vseploha.printStackTrace();
        }
    }

    @Override
    public void tableChanged(TableModelEvent e) {

    }
}
public class MainForm {
    public static void main(ObjectOutputStream SendtoServer, ObjectInputStream GetfromServer) throws InterruptedException, IOException, ClassNotFoundException {
        MainFrame frame = new MainFrame(SendtoServer, GetfromServer);
        frame.setTitle("Main window");
        frame.setVisible(true);
        frame.setBounds(10, 10, 1020, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        int c=0;
        while (true)
        {

            Thread.sleep(3500);
            if (!frame.busy) {
                frame.checkUpdate();
                System.out.println("check"+c);
                c++;
            }

        }
    }
}