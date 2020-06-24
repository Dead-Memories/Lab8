package ClientSide;
//required libraries:
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import javax.swing.*;

public class HouseDrawer extends JPanel { // inherit JPanel
    Image houseImage; // Reserve memory for Image ballImage;

    int x;
    int y;
    String name;
    int drag = -40;
    Color backColor;
    String [] info;



    // Constructor:-----------------------------------------------------
    public HouseDrawer(String name, int x, int y, Color backColor, String [] info) {

        this.backColor = backColor;
        this.x=x;
        this.y=y;
        this.name = name;
        this.info = info;
        this.setBackground(backColor);

        houseImage = new ImageIcon("house.png").getImage();
        prepareImage(houseImage, this);
        setDoubleBuffered(true);


        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showInfo();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        new Timer(200, paintTimer).start();
    }

    // ----------------------------------------------------------------

    public void showInfo(){
        String inforamation = "";

        for (String o:
             info) {
            inforamation+=o+"\n";
        }

        JOptionPane.showMessageDialog(this, inforamation);
    }

    public void paint(Graphics g) {

        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(houseImage, x, y, this); //Draws the ball Image at the correct X and Y co-ordinates.
        drag+=-drag/2;
        Toolkit.getDefaultToolkit().sync(); // necessary for linux users to draw  and animate image correctly
        g.dispose();

    }

    Action paintTimer = new AbstractAction() { // functionality of our timer:
        public void actionPerformed(ActionEvent e) {
            x+=drag;
            drag=-drag/2;
            repaint();
        }
    };
}
