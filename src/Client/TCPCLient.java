package Client;
import Shared.Kart;
import java.io.*;
import java.net.*;
import java.awt.*;


class TCPClient {
    // Declare client socket
    private static Socket clientSocket = null;

    // Declare output stream and string to send to server
    private static DataOutputStream outputStream = null;

    // Declare input stream from server and string to store input received from server
    private static BufferedReader inputStream = null;

    private static ObjectOutput objectOutput = null;
    private static ObjectInput objectInput = null;

    public static Kart ownKart = null;
    public static Kart foreignKart = null;
    public static String message;
    public static  String affected_Player;
    // "1" / "2"
    public static String PlayerNumber;

    public static boolean restart=false;


    public static void main(String[] args) {

        String errorMessage = "Kart designation need to be provided as either '1' or '2'.";

        if (args.length == 1) {
            PlayerNumber = args[0];

            if (!PlayerNumber.equals("1") && !PlayerNumber.equals("2")) {
                System.err.println(errorMessage);
                return;
            }
        } else {
            System.err.println(errorMessage);
            return;
        }

        // Create a socket on port 5000 and open input and output streams on that socket
        // replace "localhost" with the remote server address, if needed
        // 5000 is the server port
        String serverHost = "localhost";
        try {
            clientSocket = new Socket(serverHost, 5000);

            outputStream = new DataOutputStream(
                    clientSocket.getOutputStream()
            );

            inputStream = new BufferedReader(
                    new InputStreamReader(
                            clientSocket.getInputStream()
                    )
            );

            objectOutput = new ObjectOutputStream(
                    clientSocket.getOutputStream()
            );

            objectInput = new ObjectInputStream(
                    clientSocket.getInputStream()
            );
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + serverHost);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: " + serverHost);
        }

        // Write data to the socket
        if (
                clientSocket != null &&
                        outputStream != null &&
                        inputStream != null &&
                        objectOutput != null &&
                        objectInput != null
        ) {
            try {
                initialise();
                //Start the racetrack
                GameFrame testRun = new GameFrame();
                testRun.setVisible(true);

                do {
                    String responseLine = receiveMessage();

                    if (responseLine != null) {


                        handleServerResponse(responseLine);
                    }

                    if (responseLine.equals("CLOSE")) {
                        shutdownClient();
                        break;
                    }
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                    }
                } while (true);

                // close the input/output streams and socket
                outputStream.close();
                inputStream.close();
                objectOutput.close();
                objectInput.close();
                clientSocket.close();
            } catch (UnknownHostException e) {
                System.err.println("Trying to connect to unknown host: " + e);
            } catch (IOException e) {
                System.err.println("IOException:  " + e);
            }
        }
    }

    private static void initialise() {
        // initialise our client's own kart object
        Point ownPosition = new Point();
        Point foreignPosition = new Point();

        if(PlayerNumber.equals("1")){
            ownPosition.x=355;
            ownPosition.y=485;

            foreignPosition.x=355;
            foreignPosition.y=585;

        } else {
            ownPosition.x=355;
            ownPosition.y=585;

            foreignPosition.x=355;
            foreignPosition.y=485;
        }
        ownKart = new Kart(0,ownPosition,0,0,true);
        foreignKart= new Kart(0,foreignPosition,0,0,true);

        sendMessage("identify " + PlayerNumber);
        sendKart();
        sendMessage("ping");
    }

    private static void sendKart() {
        try {
            objectOutput.writeObject(ownKart);
            objectOutput.flush();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void sendOwnKart() {
        sendMessage("own_kart_update");
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
        }
        sendKart();
    }

    private static void receiveOwnKart() {
        try {
            ownKart = (Kart) objectInput.readObject();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void receiveForeignKart() {
        try {
            foreignKart = (Kart) objectInput.readObject();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static String receiveMessage() {
        try {
            return inputStream.readLine();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static void sendMessage(String message) {
        try {
            outputStream.writeBytes(message + "\n");
        } catch (Exception e) {
            shutdownClient();
            System.out.println(e.getMessage());
        }
    }

    private static void handleServerResponse(String response) {
        String[] responseParts = response.split(" ");
        switch (responseParts[0]) {
            case "pong":

                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }

                sendMessage("ping");

                break;

            case "start":
                Race_Track.running=true;

                sendOwnKart();
                break;

            case "own_kart_update":

                receiveOwnKart();
                break;
            case "foreign_kart_update":

                receiveForeignKart();
                if(Race_Track.running){
                    sendOwnKart();
                }


                break;
            case "win":
                Race_Track.running=false;
                affected_Player =responseParts[1];
                message =responseParts[0];
                restart = true;

                break;
            case "doubleCrash":
                Race_Track.running=false;
                message =responseParts[0];
                restart = true;

                break;
            case "crash":
                Race_Track.running=false;
                affected_Player =responseParts[1];
                message =responseParts[0];
                restart = true;
                break;
        }

    }

    public static void shutdownClient() {
        // shutdown script
        // close the input/output streams and socket
        try{
        outputStream.close();
        inputStream.close();
        objectOutput.close();
        objectInput.close();
        clientSocket.close();
        System.exit(0);
    } catch (IOException e) {
        System.err.println("IOException:  " + e);

    }




}}

