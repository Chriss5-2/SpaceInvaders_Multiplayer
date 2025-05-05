package Game;

import java.net.*;
import java.io.*;

public class Cliente {
    // Establecer conexión con el servidor en el puerto 4444
    public static void main(String[] args) throws IOException {
        Socket s= new Socket("localhost", 4444);

        // Recibir el escenario del servidor
        BufferedReader sc = new BufferedReader(new InputStreamReader(s.getInputStream()));
        while(true) {
            String line;
            StringBuilder frame = new StringBuilder();
            while ((line = sc.readLine()) != null) {
                if (line.equals("--Fin--")) break; // Cierra correctamente el cliente
                //System.out.println(line);
                frame.append(line).append("\n");
                System.out.println(line);
            }


            System.out.print("\033[H\033[2J");
            System.out.flush();

            System.out.println(frame.toString());
        }
    }
}
