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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;

public class Cliente extends JFrame implements Runnable {

	private JPanel contentPane;
	private JTextField textFieldMensaje, textfieldNick;
	private String ip;
	private int puerto;
	private JTextArea textArea;
	private JLabel label1;
	private JButton boton1;
	private JTextField textFieldIP;

	public Cliente() {

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

		textfieldNick = new JTextField();
		textfieldNick.setBounds(46, 22, 86, 20);
		contentPane.add(textfieldNick);
		textfieldNick.setColumns(10);

		boton1 = new JButton("Enviar");
		boton1.setBounds(77, 311, 89, 23);
		contentPane.add(boton1);

		textFieldIP = new JTextField();
		textFieldIP.setBounds(254, 22, 86, 21);
		contentPane.add(textFieldIP);
		textFieldIP.setColumns(10);

		boton1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Socket cliente = new Socket("192.168.31.104", 9999);

					paqueteEnvio datos = new paqueteEnvio();

					datos.setMensaje(textFieldMensaje.getText());
					datos.setNick(textfieldNick.getText());
					datos.setIp(textFieldIP.getText());

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
