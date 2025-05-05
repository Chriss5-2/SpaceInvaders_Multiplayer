package Game;

import javax.swing.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import java.security.Key;

public class Cliente {
    static PrintWriter in;

    // Establecer conexión con el servidor en el puerto 4444
    public static void main(String[] args) throws IOException {
        JTextArea textArea = new JTextArea();

        Socket s= new Socket("localhost", 4444);
        // Generando hilo para escuchar el escenario
        new Thread(()-> {
            // Recibir el escenario del servidor
            try {
                BufferedReader sc = new BufferedReader(new InputStreamReader(s.getInputStream()));
                while (true) {
                    String line;
                    StringBuilder frame = new StringBuilder();
                    while ((line = sc.readLine()) != null) {
                        if (line.equals("--Fin--")) break; // Cierra correctamente el cliente
                        //System.out.println(line);
                        frame.append(line).append("\n");
                        //System.out.println(line);
                    }


                    System.out.print("\033[H\033[2J");
                    System.out.flush();

                    //System.out.println(frame.toString());
                    textArea.setText(frame.toString());
                }
            } catch (IOException e) {
                System.out.println("Servidor desconectado.");
            }
        }).start();

        // Hilo lector de teclas para el servidor
        new Thread(()->{
            try{
                in = new PrintWriter(s.getOutputStream(), true);
                while(true){
                    int key = System.in.read();
                    if(key != -1){
                        char c = (char) key;
                        if (c == 'w' || c == 'a' || c == 's' || c == 'd' ||
                                c == 'W' || c == 'A' || c == 'S' || c == 'D') {
                            in.println(c);
                        }
                        //in.flush();
                    }
                }
            } catch (IOException e){
                System.out.println("Error de entrada");
            }
        }).start();



        // Creación del frame para presentación del mapa en el GUI
        JFrame gui = new JFrame("Space Invaders - Cliente");
        gui.setSize(1100, 600);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        textArea.setEditable(false);
        textArea.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 12));
        gui.add(new JScrollPane(textArea));
        gui.setLocationRelativeTo(null);
        textArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();
                if (in != null) {
                    if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) in.println('w');
                    if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) in.println('a');
                    if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) in.println('s');
                    if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) in.println('d');
                    if (code == KeyEvent.VK_F || code == KeyEvent.VK_SPACE) in.println('f'); //Dispara

                }
            }
        });
        gui.setVisible(true);
        textArea.requestFocusInWindow();
    }
}
