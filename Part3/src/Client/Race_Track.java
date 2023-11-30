package Client;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Objects;
import javax.swing.*;

import static Client.TCPClient.*;


public class Race_Track extends JPanel implements ActionListener , KeyListener {
    private final Timer t;
    public static boolean running = true;

    String[]  ownCarArray= new String[17];
    String[] foreignCarArray = new String[17];



    String[] carBlue ={ "/carBlue0.png", // image is 50x50
            "/carBlue1.png","/carBlue2.png","/carBlue3.png","/carBlue4.png","/carBlue5.png","/carBlue6.png","/carBlue7.png"
            ,"/carBlue8.png","/carBlue9.png","/carBlue10.png","/carBlue11.png","/carBlue12.png","/carBlue13.png","/carBlue14.png","/carBlue15.png","/Fire.png"};
    String[] carGreen     = { "/carGreen0.png", // image is 50x50
            "/carGreen1.png","/carGreen2.png","/carGreen3.png","/carGreen4.png","/carGreen5.png","/carGreen6.png","/carGreen7.png"
            ,"/carGreen8.png","/carGreen9.png","/carGreen10.png","/carGreen11.png","/carGreen12.png","/carGreen13.png","/carGreen14.png","/carGreen15.png","/Fire.png"};



    private void GetCarColor(){

        //Assign the color of the kart to each player according to the player number
        if(PlayerNumber.equals("1"))  {

            for(int i =0;i<carBlue.length;i++)
            {
                ownCarArray[i]=carBlue[i];
                foreignCarArray[i]=carGreen[i];
            }
        }else {
            for(int i =0;i<carBlue.length;i++)
            {
                ownCarArray[i]=carGreen[i];
                foreignCarArray[i]=carBlue[i];
            }
        }

    }

    public Race_Track() {
        setLayout(null);                       // Suppress panel layout features
        //Define the timer to refresh the screen
        GetCarColor();
        t = new Timer(100, this);
        t.start();
        }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Font currentFont = g.getFont();
        Font newFont = currentFont.deriveFont(currentFont.getSize() * 2F);
        g.setFont(newFont);


        Rectangle outer= new Rectangle(50, 100, 950, 550 );
        Rectangle inner= new Rectangle(250, 300, 550, 150  );
        Rectangle StartLine= new Rectangle(425, 450, 50, 200  );

        if (running)g.drawString("Lap number= "+ ownKart.getLap() +"/3",525,20);

        Color c1 = Color.green;
        g.setColor( c1 );
        g.fillRect( inner.x, inner.y, inner.width, inner.height  ); // grass


        Color c2 = Color.black;
        g.setColor( c2 );
        g.drawRect( outer.x, outer.y, outer.width, outer.height );  // outer edge
        g.drawRect( inner.x, inner.y, inner.width, inner.height ); // inner edge

        Color c3 = Color.yellow;
        g.setColor( c3 );
        g.drawRect( 150, 200, 750, 350 ); // mid-lane marker

        Color c4 = Color.pink;
        g.setColor( c4 );
        g.drawRect(StartLine.x,StartLine.y,StartLine.width,StartLine.height); // start line
        g.fillRect(StartLine.x,StartLine.y,StartLine.width,StartLine.height);


            //If the race has started, start repainting the images
            ImageIcon ownCarImage = new ImageIcon(Objects.requireNonNull(getClass().getResource(ownCarArray[ownKart.getDirection()])));
            ownCarImage.paintIcon(this,g,ownKart.getPosition().x,ownKart.getPosition().y);

            ImageIcon foreign_Car_Image = new ImageIcon(Objects.requireNonNull(getClass().getResource(foreignCarArray[foreignKart.getDirection()])));
            foreign_Car_Image.paintIcon(this, g, foreignKart.getPosition().x, foreignKart.getPosition().y);

            g.setColor(Color.BLACK);
            if(!running){
            switch (message){
                case"doubleCrash":
                    g.drawString("Both karts crashed... It's a tie!",525,20);
                    g.drawString("Press a key to start again or e to exit",525,40);
                    ownKart.setDirection(16);
                    foreignKart.setDirection(16);
                    break;
                case "crash":
                    String winner;
                    if(affected_Player.equals("1")){
                        winner="2";
                    }else {
                        winner="1";
                    }
                    g.drawString("Kart "+affected_Player+" crashed!. Kart "+winner+" wins.",525,20);
                    g.drawString("Press a key to start again or e to exit",525,40);
                    break;
                case "win":
                    g.drawString("Race over! Kart "+affected_Player+" wins!.",525,20);
                    g.drawString("Press a key to start again or e to exit",525,40);
                    break;
                default:

                    break;
            }

        }

    }


    private void GetPosition() {
        if(running){
            Point position= new Point(0,0);
            switch (ownKart .getDirection()){
                case 0:
                    position.y=ownKart.getPosition().y;
                    position.x=ownKart.getPosition().x+2*ownKart.getSpeed();
                    ownKart.setPosition(position);

                    break;
                case 1:
                    position.x= ownKart.getPosition().x+2*ownKart.getSpeed();
                    position.y= ownKart.getPosition().y-ownKart.getSpeed();
                    ownKart.setPosition(position);
                    break;
                case 2:
                    position.x= ownKart.getPosition().x+2*ownKart.getSpeed();
                    position.y= ownKart.getPosition().y-2*ownKart.getSpeed();
                    ownKart.setPosition(position);
                    break;
                case 3:
                    position.x= ownKart.getPosition().x+ownKart.getSpeed();
                    position.y= ownKart.getPosition().y-2*ownKart.getSpeed();
                    ownKart.setPosition(position);
                    break;
                case 4:
                    position.x= ownKart.getPosition().x;
                    position.y= ownKart.getPosition().y-2*ownKart.getSpeed();
                    ownKart.setPosition(position);
                    break;
                case 5:
                    position.x= ownKart.getPosition().x-ownKart.getSpeed();
                    position.y= ownKart.getPosition().y-2*ownKart.getSpeed();
                    ownKart.setPosition(position);
                    break;
                case 6:
                    position.x= ownKart.getPosition().x-2*ownKart.getSpeed();
                    position.y= ownKart.getPosition().y-2*ownKart.getSpeed();
                    ownKart.setPosition(position);
                    break;
                case 7:
                    position.x= ownKart.getPosition().x-2*ownKart.getSpeed();
                    position.y= ownKart.getPosition().y-ownKart.getSpeed();
                    ownKart.setPosition(position);
                    break;
                case 8:
                    position.y=ownKart.getPosition().y;
                    position.x= ownKart.getPosition().x-2*ownKart.getSpeed();
                    ownKart.setPosition(position);
                    break;
                case 9:
                    position.x= ownKart.getPosition().x-2*ownKart.getSpeed();
                    position.y= ownKart.getPosition().y+ownKart.getSpeed();
                    ownKart.setPosition(position);
                    break;
                case 10:
                    position.x= ownKart.getPosition().x-2*ownKart.getSpeed();
                    position.y= ownKart.getPosition().y+2*ownKart.getSpeed();
                    ownKart.setPosition(position);
                    break;
                case 11:
                    position.x= ownKart.getPosition().x-ownKart.getSpeed();
                    position.y= ownKart.getPosition().y+2*ownKart.getSpeed();
                    ownKart.setPosition(position);
                    break;
                case 12:
                    position.x=ownKart.getPosition().x;
                    position.y= ownKart.getPosition().y+2*ownKart.getSpeed();
                    ownKart.setPosition(position);
                    break;
                case 13:
                    position.x= ownKart.getPosition().x+ownKart.getSpeed();
                    position.y= ownKart.getPosition().y+2*ownKart.getSpeed();
                    ownKart.setPosition(position);
                    break;
                case 14:
                    position.x= ownKart.getPosition().x+2*ownKart.getSpeed();
                    position.y= ownKart.getPosition().y+2*ownKart.getSpeed();
                    ownKart.setPosition(position);
                    break;
                case 15:
                    position.x= ownKart.getPosition().x+2*ownKart.getSpeed();
                    position.y= ownKart.getPosition().y+ownKart.getSpeed();
                    ownKart.setPosition(position);
                    break;
            }
        }

    }


    @Override
    public void actionPerformed(ActionEvent e)
    {
         if (e.getSource() == t){
             GetPosition();
             repaint();
         }

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (running) {


            if (e.getKeyChar() == 'w') {
                if (ownKart.getDirection() == 15) {
                    ownKart.setDirection(0);
                } else {
                    ownKart.setDirection(ownKart.getDirection()+1);
                }


            }
            if (e.getKeyChar() == 's') {
                if (ownKart.getDirection() == 0) {
                    ownKart.setDirection(15);
                } else {
                    ownKart.setDirection(ownKart.getDirection()-1);
                }
            }
            if (e.getKeyChar() == 'd') {
                if (ownKart.getSpeed() != 100) {
                    ownKart.setSpeed(ownKart.getSpeed()+10);
                }
            }

            if (e.getKeyChar() == 'a') {
                if (ownKart.getSpeed() != 0) {
                    ownKart.setSpeed(ownKart.getSpeed()-10);
                }
            }

        }else if (restart)
        {  if (e.getKeyChar() == 'e') {
            TCPClient.sendMessage("CLOSE");
            TCPClient.shutdownClient();
        }else {
            if(PlayerNumber.equals("1")){
                ownKart.setPosition(new Point(355,485));
                foreignKart.setPosition(new Point(355,585));
                ownKart.setLap(0);
                ownKart.setDirection(0);
               foreignKart.setDirection(0);


            } else {

                ownKart.setPosition(new Point(355,585));
                foreignKart.setPosition(new Point(355,485));
                ownKart.setLap(0);
                ownKart.setDirection(0);
                foreignKart.setDirection(0);

            }
            restart=false;
            TCPClient.sendMessage("Continue");

        }

        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}