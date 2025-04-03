package chatTwo;

import java.io.*;
import java.net.*;

class Client extends Thread {
    private ClientGui gui;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    Client(ClientGui gui, Socket socket) {
        this.gui = gui;
        this.socket = socket;
    }

    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true); 

            // 讀取"請輸入名字" 訊息
            String serverMessage = in.readLine();
            System.out.println("Received from server: " + serverMessage);

            // 取得姓名
            String clientName = gui.getClientName();
            out.println(clientName); 
            out.flush();  
            
            System.out.println("Sent to server: " + clientName);

           
            String welcomeMessage = in.readLine();
            System.out.println("Received from server: " + welcomeMessage);
            gui.getTaBoard().append(welcomeMessage);

            // 循環接收 Server 訊息並顯示
            while (true) {
                String msg = in.readLine();  
                if (msg == null) break; 
                gui.getTaBoard().append(msg.trim() + "\n");
                //gui.getTaBoard().append(msg + "\n");  
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // **新增發送訊息方法**
    public void sendMessage(String msg) {
        if (out != null) {
            out.println(msg);
            out.flush();
            gui.getTaBoard().append("Me: " + msg + "\n");
            System.out.println("Sent to server: " + msg); 
        }
    }
}
