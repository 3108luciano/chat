package base;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;

public class Cliente extends JFrame {

	private JPanel contentPane;
	private JTextField textField, textfield2;
	private String ip;
	private int puerto;

	public Cliente(int puerto, String ip) {

		this.ip = ip;
		this.puerto = puerto;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel label1 = new JLabel("CHAT");
		label1.setBounds(187, 22, 54, 23);
		contentPane.add(label1);

		JTextArea textArea = new JTextArea();
		textArea.setBounds(29, 70, 285, 195);
		contentPane.add(textArea);

		textField = new JTextField();
		textField.setBounds(39, 280, 275, 20);
		contentPane.add(textField);
		textField.setColumns(10);

		textfield2 = new JTextField();
		textfield2.setBounds(48, 23, 86, 20);
		contentPane.add(textfield2);
		textfield2.setColumns(10);

		JButton boton1 = new JButton("Enviar");
		boton1.setBounds(77, 311, 89, 23);
		contentPane.add(boton1);

		boton1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Socket cliente = new Socket(ip, puerto);

					paqueteEnvio datos = new paqueteEnvio();
					datos.setMensaje(textField.getText());
					datos.setNick(textfield2.getText());

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

	}

	class paqueteEnvio implements Serializable {

		private String nick, mensaje;

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

		Cliente frame = new Cliente(10000, "localhost");
	}

}
