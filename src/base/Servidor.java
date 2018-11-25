package base;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import base.Cliente.paqueteEnvio;

public class Servidor extends JFrame implements Runnable {

	private JPanel contentPane;
	private JTextArea textArea1;
	private int puerto;

	public Servidor(int puerto) {

		this.puerto = puerto;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 350, 350);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		textArea1 = new JTextArea();
		textArea1.setBounds(10, 10, 300, 300);

		contentPane.add(textArea1);
		setVisible(true);
		Thread hilo = new Thread(this);
		hilo.start();
	}

	@Override
	public void run() {
		String nick, mensaje;
		paqueteEnvio paqueteRecibido;

		try {
			ServerSocket servidor = new ServerSocket(puerto);
			System.out.println("SERVIDOR INICIADO");
			while (true) {
				Socket cliente = servidor.accept();
				ObjectInputStream paqueteDatos = new ObjectInputStream(cliente.getInputStream());
				paqueteRecibido = (paqueteEnvio) paqueteDatos.readObject();

				nick = paqueteRecibido.getNick();
				mensaje = paqueteRecibido.getMensaje();
				textArea1.append("\n" + nick + " " + mensaje);
				cliente.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		Servidor servidor = new Servidor(10000);

	}
}
