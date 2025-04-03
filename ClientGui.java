package chatTwo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.Socket;

public class ClientGui {

    private JFrame frame;
    private JTextField tfIP;
    private JTextField tfPort;
    JTextField tfMessage;
    JTextArea taBoard;
    Socket s;
    Client client; // Declare the client instance variable

    public ClientGui() {
        // Constructor does not initialize gui variable
    }

    public void setVisible(boolean visible) {
        frame.setVisible(visible);
    }

    public void initialize() {
        frame = new JFrame();
        frame.setTitle("Two Talkers: CLIENT");
        frame.setBounds(100, 100, 506, 411);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblNewLabel = new JLabel("Server IP");
        lblNewLabel.setBounds(10, 21, 65, 15);
        frame.getContentPane().add(lblNewLabel);

        tfIP = new JTextField();
        tfIP.setText("127.0.0.1");
        tfIP.setBounds(74, 18, 105, 21);
        frame.getContentPane().add(tfIP);
        tfIP.setColumns(10);

        JLabel lblNewLabel_1 = new JLabel("Server Port");
        lblNewLabel_1.setBounds(211, 21, 72, 15);
        frame.getContentPane().add(lblNewLabel_1);

        tfPort = new JTextField();
        tfPort.setText("3001");
        tfPort.setBounds(282, 18, 51, 21);
        frame.getContentPane().add(tfPort);
        tfPort.setColumns(10);

        JButton btnConnect = new JButton("Connect");
        btnConnect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                try {
                    s = new Socket(tfIP.getText(), Integer.parseInt(tfPort.getText()));
                    System.out.println("Connected to server");

                    // Create the Client object and pass the gui reference
                    client = new Client(ClientGui.this, s);  // Pass the reference of ClientGui
                    client.start();  // Start the Client thread
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        btnConnect.setBounds(369, 17, 87, 23);
        frame.getContentPane().add(btnConnect);

        JButton btnClose = new JButton("Close");
        btnClose.setBounds(369, 47, 87, 23);
        frame.getContentPane().add(btnClose);
        btnClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                try {
                    s.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.exit(0);
            }
        });

        taBoard = new JTextArea();
        taBoard.setBounds(22, 71, 446, 247);
        frame.getContentPane().add(taBoard);

        tfMessage = new JTextField();
        tfMessage.setBounds(22, 342, 446, 21);
        frame.getContentPane().add(tfMessage);
        tfMessage.setColumns(10);

        // When "Enter" key is pressed, send the message
        tfMessage.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessage();  // Call sendMessage to send the message
                }
            }
        });
    }

    // Send the message to the server
    public void sendMessage() {
        String msg = tfMessage.getText();
        if (!msg.isEmpty()) {
            client.sendMessage(msg);  // Pass the message to Client
            tfMessage.setText("");  // Clear the text input after sending
        }
    }

    // Prompt user for their name, or generate a random one
    public String getClientName() {
        String name = JOptionPane.showInputDialog(frame, "Enter your name:");
        if (name == null || name.trim().isEmpty()) {
            name = "User" + (int) (Math.random() * 1000); // Generate a random name if empty
        }
        return name;
    }

    // Return the taBoard reference
    public JTextArea getTaBoard() {
        return taBoard;
    }
}
