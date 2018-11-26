package base;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import base.Cliente.paqueteEnvio;

public class Servidor extends JFrame implements Runnable {

	private JPanel contentPane;
	private JTextArea textArea1;
	private int puerto;

	public Servidor() {

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
		String nick, mensaje, ip;
		paqueteEnvio paqueteRecibido;
		ArrayList<String> listaIP = new ArrayList<String>();

		try {
			ServerSocket servidor = new ServerSocket(9999);
			System.out.println("SERVIDOR INICIADO");
			while (true) {
				Socket cliente = servidor.accept();

				ObjectInputStream paqueteDatos = new ObjectInputStream(cliente.getInputStream());
				paqueteRecibido = (paqueteEnvio) paqueteDatos.readObject();

				nick = paqueteRecibido.getNick();
				mensaje = paqueteRecibido.getMensaje();
				ip = paqueteRecibido.getIp();

				if (!mensaje.equals(" online")) {
					textArea1.append("\n" + nick + ": " + mensaje);

					Socket enviaDestinatario = new Socket(ip, 9090);

					ObjectOutputStream paqueteReenvio = new ObjectOutputStream(enviaDestinatario.getOutputStream());
					paqueteReenvio.writeObject(paqueteRecibido);

					paqueteReenvio.close();
					enviaDestinatario.close();
					cliente.close();
				} else {
					// -----------DETECTA USUARIOS ONLINE------------//

					InetAddress localizacion = cliente.getInetAddress(); /* obtengo ip del cliente que se conecto */
					String ipRemota = localizacion.getHostAddress(); /* almaceno la ip en un String */
					System.out.println("ONLINE: " + ipRemota);

					listaIP.add(ipRemota);

					paqueteRecibido.setArrayClienteIP(listaIP);

					for (String i : listaIP) {
						System.out.println("arrayIP: " + i);

						Socket enviaDestinatario = new Socket(i, 9090);

						ObjectOutputStream paqueteReenvio = new ObjectOutputStream(enviaDestinatario.getOutputStream());
						paqueteReenvio.writeObject(paqueteRecibido);

						paqueteReenvio.close();
						enviaDestinatario.close();
						cliente.close();
					}

					// ----------------------------------------------//
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		Servidor servidor = new Servidor();

	}
}
