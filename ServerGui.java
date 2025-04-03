package chatTwo;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;

public class ServerGui {

	private JFrame frame;
	private JTextField tfIP;
	private JTextField tfPort;
	JTextField tfMessage;
	JTextArea taBoard;
	ServerGui gui;
	int send_flag;
	ServerSocket svs;
	Socket s;
	public ServerGui() {
       gui = this;
	}

	void setVisible(boolean visiable) {
		frame.setVisible(visiable);
	}

	void initialize() {
		frame = new JFrame();
		frame.setTitle("Two Talkers: Server");
		frame.setBounds(100, 100, 506, 411);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblNewLabel_1 = new JLabel("Server Port");
		lblNewLabel_1.setBounds(10, 21, 65, 30);
		frame.getContentPane().add(lblNewLabel_1);

		tfPort = new JTextField();
		tfPort.setText("3001");
		tfPort.setBounds(74, 18, 105, 21);
		frame.getContentPane().add(tfPort);
		tfPort.setColumns(10);

		JButton btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				try {
					svs = new ServerSocket(3001);
					
				} catch (IOException e2) {
					e2.printStackTrace();
				}
				new Server(gui).start();;
				
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
					gui.s.close();
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

		tfMessage.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					send_flag = 1;
			}
		});
	}
}
