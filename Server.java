package chatTwo;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

import javax.swing.SwingUtilities;

class Server extends Thread {
    ServerGui gui;
    private static final List<Socket> clientSockets = new CopyOnWriteArrayList<>();

    Server(ServerGui gui) {
        this.gui = gui;
    }

    public void run() {
        try {
            while (true) {
                Socket clientSocket = gui.svs.accept();
                clientSockets.add(clientSocket); // 當新客戶端連接時，將它加到列表中

                SwingUtilities.invokeLater(() -> 
                    gui.taBoard.append("New client connected: /" + clientSocket.getInetAddress().getHostAddress() + "\n")
                );

                new ClientHandler(clientSocket, gui).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket clientSocket;
        private ServerGui gui;
        private BufferedReader in;
        private PrintWriter out;

        ClientHandler(Socket clientSocket, ServerGui gui) {
            this.clientSocket = clientSocket;
            this.gui = gui;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                System.out.println("b7");
                out = new PrintWriter(clientSocket.getOutputStream(), true); // 自動 flush
                System.out.println("b6");
                // 詢問名字
                out.println("Please enter your name:");
                System.out.println("b5");
                System.out.println("Sent to client: Please enter your name:");

                // 讀取客戶輸入的名字
                String clientName = in.readLine();
                if (clientName == null || clientName.trim().isEmpty()) {
                    clientName = "User" + new Random().nextInt(1000);
                }

                System.out.println("Received from client: " + clientName);
                System.out.println("b1");
                
                String response = "Hello, " + clientName + "!";
                System.out.println("b2");
                out.println(response+"\n"); 
                System.out.println("b3");
                gui.taBoard.append(clientName + " has joined the chat.\n");
                //out.println("You have joined the chat.");
                System.out.println("b4");
                
                String msg;
                System.out.println("b");
                while ((msg = in.readLine()) != null) {
                	System.out.println("b");
                    gui.taBoard.append(clientName + ": " + msg + "\n");
                    System.out.println("b");

                    // 將訊息轉發給所有其他客戶端
                    for (Socket s : clientSockets) {
                        if (s != clientSocket) {
                            try {
                                PrintWriter clientOut = new PrintWriter(s.getOutputStream(), true);
                                clientOut.println(clientName + ": " + msg);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                    clientSockets.remove(clientSocket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
