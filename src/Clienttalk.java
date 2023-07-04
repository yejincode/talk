import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.awt.event.*;

public class Clienttalk {

	private JFrame frame;
	private JTextField inputfield;
	JScrollPane scrollPane = new JScrollPane();
	JTextArea chat = new JTextArea();
	Socket socket;
	DataInputStream dis;
	DataOutputStream dos;
	private JTextField name_field;
	private JTextField ip_field;
	private JTextField port_field;
	
	//접속할 ip,port,name
	String ip;
	int port;
	String name;
	
	

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Clienttalk window = new Clienttalk();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Clienttalk() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(SystemColor.activeCaption);
		frame.setBounds(100, 100, 500, 677);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setTitle("클라이언트톡");

		//제일 위 제목
		JLabel lblNewLabel = new JLabel("Client Talk");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("타이포_쌍문동 B", Font.PLAIN, 28));
		lblNewLabel.setBounds(123, 22, 237, 65);
		frame.getContentPane().add(lblNewLabel);

		// 버튼 담을 패널 생성
		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 255, 255));
		panel.setBounds(0, 499, 482, 131);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		//전송버튼
		JButton send = new JButton("전송");
		send.setBounds(393, 12, 75, 70);
		panel.add(send);
		send.setFont(new Font("-윤고딕310", Font.PLAIN, 15));
		send.setForeground(new Color(0, 0, 0));
		send.setBackground(new Color(255, 255, 0));

		//채팅 내용 담을 필드
		inputfield = new JTextField();
		inputfield.setHorizontalAlignment(SwingConstants.LEFT);
		inputfield.setFont(new Font("-윤고딕310", Font.PLAIN, 15));
		inputfield.setBounds(14, 12, 365, 107);
		panel.add(inputfield);
		inputfield.setColumns(10);
		
		//나가기 버튼
		JButton exit = new JButton("나가기");
		exit.setBackground(new Color(255, 255, 102));
		exit.setForeground(new Color(0, 0, 0));
		exit.setFont(new Font("-윤고딕310", Font.PLAIN, 15));
		exit.setBounds(374, 45, 94, 27);
		frame.getContentPane().add(exit);

		//접속하기 버튼
		JButton connect = new JButton("접속하기");
		connect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		connect.setBackground(new Color(255, 255, 102));
		connect.setFont(new Font("-윤고딕310", Font.PLAIN, 15));
		connect.setBounds(374, 12, 94, 27);
		frame.getContentPane().add(connect);
		
		//노란 화면 (채팅 내용)
		chat.setForeground(SystemColor.desktop);
		chat.setBackground(new Color(255, 255, 102));
		scrollPane.setBounds(14, 5, 454, 404);
		scrollPane.setViewportView(chat);
		chat.setFont(new Font("-윤고딕310", Font.PLAIN, 15));
		
		//패널2 생성 -> 스크롤 하기 위함. 
		JPanel panel2 = new JPanel();
		panel2.setBackground(SystemColor.activeCaption);
		panel2.setBounds(0, 78, 482, 421);
		panel2.setLayout(null);
		panel2.add(scrollPane);
		frame.getContentPane().add(panel2);
		
		// 전송 버튼 클릭에 반응하는 리스너
		send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendMessage();
			}
		});

		// 접속하기 버튼 클릭에 반응하는 리스너
		connect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { 
				
				//접속할 ip, 포트, 닉네임 입력할 프레임 생성하기
				final Frame fs = new Frame("닉네임 입력하기"); //닉네임 입력창 생성
				fs.setVisible(true);
				fs.setBounds(100, 100, 431, 277);
				
				fs.addWindowListener(new WindowAdapter( ) {
					public void windowClosing(WindowEvent e) {
						fs.setVisible(false);
						fs.dispose();
					}
				});
				
				fs.setBounds(100,100,413,266);
				fs.setLocation(200,200);
				
				JPanel panel2 = new JPanel();
				
				//클라이언트 닉네임 입력
				name_field = new JTextField();
				name_field.setBounds(179, 148, 116, 24);
				name_field.setColumns(10);
				//접속할 ip 입력
				ip_field = new JTextField();
				ip_field.setBounds(179, 66, 116, 24);
				ip_field.setColumns(10);
				//접속할 포트 입력
				port_field = new JTextField();
				port_field.setBounds(179, 106, 116, 24);
				port_field.setColumns(10);
				
				JLabel input_ip = new JLabel("접속할 IP");
				input_ip.setFont(new Font("-윤고딕310", Font.PLAIN, 15));
				input_ip.setBounds(90, 70, 59, 16);
				
				JLabel input_port = new JLabel("접속할 포트");
				input_port.setFont(new Font("-윤고딕310", Font.PLAIN, 15));
				input_port.setBounds(74, 110, 75, 16);
				
				JLabel input_name = new JLabel("사용할 닉네임");
				input_name.setFont(new Font("-윤고딕310", Font.PLAIN, 15));
				input_name.setBounds(61, 152, 89, 16);
				
				JButton connect_bt = new JButton("접속하기");
				connect_bt.setFont(new Font("-윤고딕310", Font.PLAIN, 13));
				connect_bt.setBounds(294, 200, 89, 27);
				
				panel2.setBounds(0, 0, 413, 230);
				panel2.setLayout(null);
				panel2.add(name_field);
				panel2.add(ip_field);
				panel2.add(port_field);
				panel2.add(input_ip);
				panel2.add(input_port);
				panel2.add(input_name);
				panel2.add(connect_bt);
				
				fs.add(panel2);
				
				//접속하기 버튼 눌렀을 때 반응하는 리스너
				connect_bt.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						//텍스트 필드에 입력한 값을 가져와서 String,int으로 변환
						ip = ip_field.getText();
						port = Integer.parseInt(port_field.getText()); //int 변환
						name = name_field.getText();
						
						// 서버와 연결하기
						ClientThread clientThread = new ClientThread();
						clientThread.start();
						
					}

				});
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
				case KeyEvent.VK_ENTER: // 키이벤트가 키에 대한 정보 가짐.
					sendMessage(); // 누른 키가 엔터라면 메시지 보내기
					break;
				}
			}
		});

		// 나가기 버튼 눌렀을 때 반응하는 리스너
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (dos != null)
						dos.close();
					if (dis != null)
						dis.close();
					if (socket != null)
						socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		inputfield.requestFocus();

	}

	class ClientThread extends Thread {
		public void run() {
			try {
				//socket = new Socket("127.0.0.1", 9001); -> 서버와 연결할 수 있는 ip, 포트
				socket = new Socket(ip, port);
				chat.append(ip + " 에 접속됐습니다.\n");
				
				// 데이터 전송을 위한 스트림 생성
				InputStream is = socket.getInputStream();
				OutputStream os = socket.getOutputStream();

				// 통신을 위한 스트림 생성
				dis = new DataInputStream(is);
				dos = new DataOutputStream(os);
				
				// 입력했던 닉네임을 서버에 보내기
				Thread t = new Thread() {
					public void run() {
						try { // UTF로 한글 깨지지 않게 해준다.
							dos.writeUTF(name);
							dos.flush(); // 계속 채팅 위해 close()하면 안됨
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				};
				t.start();
			
				while (true) {// 서버 메시지 받기
					String msg = dis.readUTF();
					chat.append(" SERVER : " + msg + "\n");
					chat.setCaretPosition(chat.getText().length());
				}
				
			} catch (UnknownHostException e) {
				chat.append("서버 주소가 이상합니다.\n");
			} catch (IOException e) {
				chat.append("서버와 연결이 끊겼습니다.\n");
			}
			
			
		}
	}

	// 메시지 전송하는 기능 메소드
	void sendMessage() {
		String msg = inputfield.getText(); // inputfield에 써있는 글씨를 얻어오기
		inputfield.setText(""); // 입력 후 빈칸으로
		chat.append( name + " : " + msg + "\n");// chat에 내용 표시하기
		chat.setCaretPosition(chat.getText().length());

		// 서버에게 메시지 전송하기
		Thread t = new Thread() {
			public void run() {
				try { // UTF로 한글 깨지지 않게 해준다.
					dos.writeUTF(msg);
					dos.flush(); // 계속 채팅 위해 close()하면 안됨
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		t.start();
	}
}
