import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.awt.event.*;

public class Servertalk {

	private JFrame frame;
	private JTextField inputfield;
	JScrollPane scrollPane = new JScrollPane();
	JTextArea chat = new JTextArea();
	ServerSocket serverSocket;
	Socket socket;
	DataInputStream dis;
	DataOutputStream dos;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Servertalk window = new Servertalk();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Servertalk() {
		initialize();
	}

	private void initialize() {
		
		//프레임 생성
		frame = new JFrame();
		frame.getContentPane().setBackground(SystemColor.activeCaption);
		frame.setBounds(100, 100, 500, 677);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setTitle("서버톡");

		//서버톡 제목
		JLabel lblNewLabel = new JLabel("Server TALK");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("타이포_쌍문동 B", Font.PLAIN, 28));
		lblNewLabel.setBounds(123, 22, 237, 65);
		frame.getContentPane().add(lblNewLabel);

		//패널 생성
		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 255, 255));
		panel.setBounds(0, 499, 482, 131);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		// 전송 버튼
		JButton sendbutton = new JButton("전송");
		sendbutton.setBounds(393, 12, 75, 70);
		panel.add(sendbutton);
		sendbutton.setFont(new Font("-윤고딕310", Font.PLAIN, 15));
		sendbutton.setForeground(new Color(0, 0, 0));
		sendbutton.setBackground(new Color(255, 255, 0));

		
		// 채팅 입력 받을 필드
		inputfield = new JTextField();
		inputfield.setHorizontalAlignment(SwingConstants.LEFT);
		inputfield.setFont(new Font("-윤고딕310", Font.PLAIN, 15));
		inputfield.setBounds(14, 12, 365, 107);
		panel.add(inputfield);
		inputfield.setColumns(10);
		
		// 노란 화면(채팅 내용)
		chat.setForeground(SystemColor.desktop);
		chat.setBackground(new Color(255, 255, 102));
		scrollPane.setBounds(14, 5, 454, 404);
		scrollPane.setViewportView(chat);
		chat.setFont(new Font("-윤고딕310", Font.PLAIN, 15));
		
		//패널 2 생성
		JPanel panel2 = new JPanel();
		panel2.setBackground(SystemColor.activeCaption);
		panel2.setBounds(0, 78, 482, 421);
		panel2.setLayout(null);
		panel2.add(scrollPane);
		frame.getContentPane().add(panel2);
		
		//나가기 버튼
		JButton exit = new JButton("나가기");
		exit.setBackground(new Color(255, 255, 102));
		exit.setForeground(new Color(0, 0, 0));
		exit.setFont(new Font("-윤고딕310", Font.PLAIN, 15));
		exit.setBounds(374, 12, 94, 27);
		frame.getContentPane().add(exit);

		// 전송 버튼 클릭할 때 반응하는 리스너
		sendbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendMessage();
			}
		});

		// 엔터키 눌렀을 때 반응하는 리스너
		inputfield.addKeyListener(new KeyAdapter() {
			// 키보드에서 키 하나를 눌렀을때 자동으로 실행되는 메소드
            public void keyPressed(KeyEvent e) {
			super.keyPressed(e);
			
				// 입력받은 키가 엔터인지 알아내기
				int keyCode = e.getKeyCode();
				switch (keyCode) {
				case KeyEvent.VK_ENTER: // 키 이벤트가 키에 대한 정보 가짐.
					sendMessage(); //누른 키가 엔터라면 메시지 보내기
					break;
				}
			}
		});
		
		
		//나가기 버튼 눌렀을 때 반응하는 리스너
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				try {
					if(dos != null) dos.close();
					if(dis != null) dis.close();
					if(socket != null) socket.close();
					if(serverSocket != null) serverSocket.close();
				} catch (IOException e1) {					
					e1.printStackTrace();
				}
			}

		});
		
		inputfield.requestFocus();
		
		ServerThread serverThread = new ServerThread();
		serverThread.start();
		
	}
	
	class ServerThread extends Thread {

		public void run() {			
			try { //서버 소켓 생성 작업
				serverSocket = new ServerSocket(9001);
				chat.append("서버소켓 준비 끝\n");
				chat.append("클라이언트 접속을 기다리는 중입니다.\n");				
				socket = serverSocket.accept();//클라이언트가 접속할때까지 대기
				chat.append("ip주소 : "+ socket.getInetAddress().getHostAddress() + "님이 접속하셨습니다.\n"); //ip 주소 출력해주기
				
				//통신을 위한 스트림 생성
				dis = new DataInputStream(socket.getInputStream());
				dos = new DataOutputStream(socket.getOutputStream());

				//클라이언트 닉네임 받아오기
				String client_name = dis.readUTF();
				chat.append("클라이언트 이름은 " + client_name + " 입니다. \n");
				
				while(true) {
					//클라이언트가 보내온 내용 읽기
					String msg = dis.readUTF();//클라이언트가 보낼때까지 대기
					chat.append( client_name + " : " + msg + "\n");
					chat.setCaretPosition(chat.getText().length());
				}
			} catch (IOException e) { //클라이언트가 나가면
				chat.append("클라이언트가 나갔습니다.\n");
			}
		}
	}
	
	void sendMessage() {	
		String msg = inputfield.getText(); //inputfield에 써있는 글씨를 얻어오기
		inputfield.setText(""); //입력 후 빈칸으로 비우기
		chat.append(" SERVER : " + msg + "\n");//chat에 내용 표시하기.
		chat.setCaretPosition(chat.getText().length());

		//클라이언트에게 메시지 전송하기
		Thread t = new Thread() {
			public void run() {
				try {
					dos.writeUTF(msg);
					dos.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};		
		t.start();
	}	
}
