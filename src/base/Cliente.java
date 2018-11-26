package base;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import base.Cliente.paqueteEnvio;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;

public class Cliente extends JFrame implements Runnable {

	private JPanel contentPane;
	private JTextField textFieldMensaje ;
	private int puerto;
	private JTextArea textArea;
	private JLabel label1,Nick,labelIP;
	private JButton boton1;
	private JLabel labelMostrarNick;
	private JComboBox ip;
	
	public Cliente() {

		String nick_usuario= JOptionPane.showInputDialog("Nick: ");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		label1 = new JLabel("CHAT");
		label1.setBounds(153, 21, 67, 23);
		contentPane.add(label1);

		textArea = new JTextArea();
		textArea.setBounds(29, 70, 285, 195);
		contentPane.add(textArea);

		textFieldMensaje = new JTextField();
		textFieldMensaje.setBounds(39, 280, 275, 20);
		contentPane.add(textFieldMensaje);
		textFieldMensaje.setColumns(10);

		Nick = new JLabel("Nick: ");
		Nick.setBounds(26, 22, 86, 20);
		contentPane.add(Nick);
		

		boton1 = new JButton("Enviar");
		boton1.setBounds(77, 311, 89, 23);
		contentPane.add(boton1);

		
		labelIP = new JLabel("ONLINE: ");
		labelIP.setBounds(200,22,80,21);
		contentPane.add(labelIP);
		
		ip = new JComboBox();
		ip.setBounds(254, 22, 86, 21);
		ip.addItem("Usuario 1");
		ip.addItem("Usuario 2");
		ip.addItem("Usuario 3");

		contentPane.add(ip);
		
		
		labelMostrarNick = new JLabel();
		labelMostrarNick.setText(nick_usuario);
		labelMostrarNick.setBounds(64, 25, 46, 14);
		contentPane.add(labelMostrarNick);

		boton1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				
				textArea.append("\n"+textFieldMensaje.getText());
				try {
					Socket cliente = new Socket("192.168.0.7",9999);

					paqueteEnvio datos = new paqueteEnvio();

					datos.setMensaje(textFieldMensaje.getText());
					datos.setNick(Nick.getText());
					datos.setIp(ip.getSelectedItem().toString());

					ObjectOutputStream paqueteDatos = new ObjectOutputStream(cliente.getOutputStream());
					paqueteDatos.writeObject(datos);
					cliente.close();

				} catch (UnknownHostException ex) {
					ex.printStackTrace();
				} catch (IOException ex2) {
					System.out.println(ex2.getMessage());
				}

			}
		});

		setVisible(true);
		Thread hilo = new Thread(this);
		hilo.start();
	}

	class paqueteEnvio implements Serializable {

		private String nick, mensaje, ip;

		public String getIp() {
			return ip;
		}

		public void setIp(String ip) {
			this.ip = ip;
		}

		public String getNick() {
			return nick;
		}

		public void setNick(String nick) {
			this.nick = nick;
		}

		public String getMensaje() {
			return mensaje;
		}

		public void setMensaje(String mensaje) {
			this.mensaje = mensaje;
		}

	}

	public static void main(String[] args) {

		Cliente frame = new Cliente();
	}

	@Override
	public void run() {

		try {

			paqueteEnvio paqueteRecibido;
			Socket cliente;
			ServerSocket servidor = new ServerSocket(9090);

			while (true) {

				cliente = servidor.accept();
				ObjectInputStream flujoEntrada = new ObjectInputStream(cliente.getInputStream());
				paqueteRecibido = (paqueteEnvio) flujoEntrada.readObject();

				textArea.append("\n" + paqueteRecibido.getNick() + ": " + paqueteRecibido.getMensaje());
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}
}
