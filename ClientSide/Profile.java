package ClientSide;

import javax.swing.*;
import java.awt.*;

public class Profile extends JPanel {
    String login;
    int id;
    JLabel loginmark;
    TextField box = new TextField();

    public Profile(String login, long vid) {

        this.id = (int) vid;
        this.login = login;
        loginmark= new JLabel(login+" ("+String.valueOf(id)+")", SwingConstants.CENTER);
        this.setLayout(new GridLayout(2, 0));
        box.setBackground(new Color(id*200%255, id*150%255, id*44%255));
        loginmark.setBounds(0, 0, 130, 35);

        box.setBounds(0, 35, 130, 51);
        box.setVisible(true);
        this.box.setEditable(false);
        this.add(loginmark);
        this.add(box);
        this.setVisible(true);
    }





}
