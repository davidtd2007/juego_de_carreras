package Server;
import Shared.Kart;

import java.awt.*;
import java.lang.*;
import java.io.*;
import java.net.*;




class TCPClientHandler implements Runnable {

    private static boolean close=false;
    private Socket server = null;
    // Declare an input stream and String to store message from client
    private BufferedReader inputStream;
    private String line;
    // Declare an output stream to client
    private DataOutputStream outputStream;

    private ObjectInput objectInput = null;
    private ObjectOutput objectOutput = null;

    private static Kart kart2 = null;
    private static Kart kart1 = null;

    // "1" / "2"
    private String kartType;

    private boolean alive = true;

    //Rectangles of used in the track to check collisions
    Rectangle outer= new Rectangle(50, 100, 950, 550 );
    Rectangle inner= new Rectangle(250, 300, 550, 150  );
    Rectangle StartLine= new Rectangle(425, 450, 50, 200  );
    Rectangle TrackBottom= new Rectangle(50, 450, 950, 200   );
    Rectangle TrackTop= new Rectangle(50, 100, 950, 200  );
    Rectangle TrackLeft= new Rectangle(50, 100, 200, 550 );
    Rectangle TrackRight= new Rectangle(800, 100, 200, 550   );

    private static boolean running = false;

    private static String final_message;



    public TCPClientHandler(Socket server) {
        this.server = server;
    }

    public void run() {
        try {
            inputStream = new BufferedReader(
                    new InputStreamReader(
                            server.getInputStream()
                    )
            );

            outputStream = new DataOutputStream(
                    server.getOutputStream()
            );

            objectInput = new ObjectInputStream(
                    server.getInputStream()
            );

            objectOutput = new ObjectOutputStream(
                    server.getOutputStream()
            );

            do {
                line = receiveMessage();

                if (line.equals("CLOSE")) {
                    break;

                }
                if (line != null) {
                    handleClientResponse(line);
                }

                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                }
            } while (true);

            // Comment out/remove the outputStream and server close statements if server should remain live
            outputStream.close();
            inputStream.close();
            objectOutput.close();
            objectInput.close();
            server.close();
            System.exit(0);
        } catch (Exception e) {
            System.out.println("TCPClientHandler Exception: " + e.getMessage());
        }

        alive = false;
    }

    public boolean isAlive() {
        return alive;
    }

    private void sendMessage(String message) {
        try {
            outputStream.writeBytes(message + "\n");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private String receiveMessage() {
        try {
            return inputStream.readLine();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private void sendKart(Kart kartToSend) {
        try {
            objectOutput.writeObject(kartToSend);
            objectOutput.flush();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    private void sendForeignKart() {
        Kart kartToSend = null;
    synchronized (this) {
    sendMessage("foreign_kart_update");
    try {
        Thread.sleep(10);
    } catch (InterruptedException e) {
    }
    switch (kartType) {
        case "1":
            kartToSend = kart2;
            break;

        case "2":
            kartToSend = kart1;
            break;
        }
    }
        sendKart(kartToSend);
    }

    private void sendOwnKart() {
        Kart kartToSend = null;
synchronized (this) {
    sendMessage("own_kart_update");
    try {
        Thread.sleep(10);
    } catch (InterruptedException e) {
    }
    switch (kartType) {
        case "2":
            kartToSend = kart2;
            break;

        case "1":
            kartToSend = kart1;
            break;
    }
}
        sendKart(kartToSend);
    }

    private void receiveKart() {
        Kart inputKart = null;

        try {
            inputKart = (Kart) objectInput.readObject();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        switch (kartType) {
            case "2":
                kart2 = inputKart;
                break;

            case "1":
                kart1 = inputKart;
                break;
        }
    }


                private void checkCrush() {
                    Kart kart;
                    if(kartType.equals("2")) {
                        kart = kart2;
                    }else {kart= kart1;}

                    Rectangle car = new Rectangle(kart.getPosition().x, kart.getPosition().y, 50, 50);

                    //Check if the kart is going in the wrong direction
                    if (TrackTop.intersects(car) && (kart.getDirection() == 0 || kart.getDirection() == 1 || kart.getDirection() == 2 || kart.getDirection() == 14 || kart.getDirection() == 15)) {
                        kart.setDirection(16);
                        kart.setSpeed(0);

                    }

                    if (TrackBottom.intersects(car) && (kart.getDirection() == 7 || kart.getDirection() == 8 || kart.getDirection() == 9)) {
                        kart.setDirection(0);
                        kart.setSpeed(0);

                    }
                    if (TrackRight.intersects(car) && (kart.getDirection() == 11 || kart.getDirection() == 12 || kart.getDirection() == 13)) {
                        kart.setDirection(16);
                        kart.setSpeed(0);

                    }
                    if (TrackLeft.intersects(car) && (kart.getDirection() == 3 || kart.getDirection() == 4 || kart.getDirection() == 5 || kart.getDirection() == 6)) {
                        kart.setDirection(16);
                        kart.setSpeed(0);

                    }



                    if(!outer.contains(car)){
                        kart.setDirection(16);
                        kart.setSpeed(0);
                        final_message="crash "+kartType.toString();
                        running =false;
                    }
                    //If a kart hits the grass put it back in the road
                    if(inner.intersects(car)){
                        if(TrackLeft.intersects(car)){
                            kart.setDirection(12);
                            kart.setSpeed(0);
                            kart.setPosition(new Point(125,kart.getPosition().y) );
                        }

                        if(TrackBottom.intersects(car)){
                            kart.setDirection(0);
                            kart.setSpeed(0);
                            kart.setPosition(new Point(kart.getPosition().x,550) );
                        }

                        if(TrackRight.intersects(car)){
                            kart.setDirection(4);
                            kart.setSpeed(0);
                            kart.setPosition(new Point(900,kart.getPosition().y) );
                        }

                        if(TrackTop.intersects(car)){
                            kart.setDirection(8);
                            kart.setSpeed(0);
                            kart.setPosition(new Point(kart.getPosition().x,150) );
                        }
                    }


                    Rectangle kart1_Rectangle = new Rectangle(kart1.getPosition().x, kart1.getPosition().y, 50, 50);
                    Rectangle kart2_Rectangle = new Rectangle(kart2.getPosition().x, kart2.getPosition().y, 50, 50);
                    if(kart1_Rectangle.intersects(kart2_Rectangle)){
                        kart1.setSpeed(0);
                        kart1.setDirection(16);

                        kart2.setSpeed(0);
                        kart2.setDirection(16);

                        final_message="doubleCrash";
                        running =false;
                    }


                    //If the car pass through the Start Line add one lap as completed and avoid it adds m{ore until the car has abandoned the line
                    if (kart.getLap_bool() && StartLine.intersects(car) ){
                        kart.setLap(kart.getLap()+1);
                        kart.setLap_bool(false);

                    }

                    if(!kart.getLap_bool() && !StartLine.intersects(car)){
                        kart.setLap_bool(true);
                    }



                    switch (kartType) {
                        case "2":
                            kart2 = kart;
                            break;

                        case "1":
                            kart1 = kart;
                            break;
                    }
                    if (kart.getLap() ==4 ){
                        final_message="win "+kartType.toString();
                        running =false;
                        kart1.setSpeed(0);
                        kart2.setSpeed(0);

                    }
                }



                private void handleClientResponse (String response){


                    String[] responseParts = response.split(" ");

                    switch (responseParts[0]) {
                        case "ping":

                            try {
                                Thread.sleep(1000);
                            } catch (Exception e) {
                            }

                            if(TCPServer.activeClients==TCPServer.maxClients){
                                running =true;
                                sendMessage("start");
                            }else{ sendMessage("pong");}

                            break;

                        case"Continue":
                            if(close){
                                sendMessage("CLOSE");
                            }else {
                                sendMessage("pong");
                            }


                            break;

                        case "identify":

                            kartType = responseParts[1];

                            receiveKart();


                            break;

                        case "own_kart_update":

                            synchronized (this) {
                                receiveKart();
                                checkCrush();
                                if(!running){
                                    sendMessage(final_message);
                                    sendOwnKart();
                                    sendForeignKart();

                                }
                                if(running){
                                    sendOwnKart();
                                    sendForeignKart();
                                }

                            }


                            break;
                    }
                }



            }