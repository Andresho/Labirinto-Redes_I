import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JTextField;

public class ClientMain {

	public static void main(String[] args) throws Exception{
		
		System.out.println(" TESTE ");
		
		// Ser�o usados pelo KeySender
		Socket socket = new Socket("127.0.0.1", 6789);
		
		System.out.println(" TESTE 2");
		
		BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		DataOutputStream output = new DataOutputStream(socket.getOutputStream());
		
		System.out.println(" TESTE ");
		
		/*
		 * Se comunicando com o ServerMain, para pegar o PORT
		 * de um jogador livre
		 * 
		 * Caso n�o encontrar nenhum, fechar
		 * 
		 * fechar o socket, buffer e a stream linkados ao ServerMain
		 * */
		output.writeBytes("GET_PLAYER_PORT\n");
		int newPort = Integer.parseInt( input.readLine() );
		if(newPort == -1) {
			System.out.println("The Game is Full \n Exit");
			System.exit(-1);
		}
		
		output.close();
		input.close();
		socket.close();
		
		System.out.println(newPort);
		
		/*
		 * Abrir comunica��o com a thread player
		 * com o PORT recebido pelo ServerMain
		 * */
		
		socket = new Socket("127.0.0.1", newPort);
		output = new DataOutputStream(socket.getOutputStream());
		input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		
		output.writeBytes("START\n");
		
		
		// Listener do teclado que envia as teclas clicadas
		// ao servidor pelo socket
		KeySender ks = new KeySender(socket, output, input);
		
		// � necess�ria uma janela para ser o foco do teclado
		JFrame aWindow = new JFrame("Labirinto");
		aWindow.setBounds(50, 100, 300, 300);
		aWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Ativa o KeySender como um listener e o linka
		// � janela, assim a sua fun��o keyPressed ser� 
		// chamada adequadamente
		JTextField typingArea = new JTextField(20);
		typingArea.addKeyListener(ks);
		
		aWindow.add(typingArea);
	    aWindow.setVisible(true);
	}
}


