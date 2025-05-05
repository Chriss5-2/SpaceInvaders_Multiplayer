package Game;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Servidor {
    // Levantar el servidor en el puerto 4444
    private static int numPlayer=0;
    private static List<Player> players = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        // Creación de servidor
        ServerSocket server = new ServerSocket(4444);
        System.out.println("Servidor creado");

        //Creación del escenario
        int rows = 60;
        int columns = 150;
        int screen = 30;
        int[][] scenary = create_Scenary(rows, columns);
        System.out.println("Escenario creado"); //Línea para confirmar la creación del escenario
        int posX=10, posY=15;
        while (true) {
            //Conectamos con el jugador entrante y agregamos uno al nro de jugadores
            Socket plSckt = server.accept();
            numPlayer++;

            Player player = new Player("Jugador" + numPlayer, posX, posY);
            players.add(player);
            System.out.println(player.getName() + " conectado");
            posX=posX+15;
            PlayerControl control = new PlayerControl(plSckt, player, scenary, players);
            control.start();
        }
    }



    public static int[][] create_Scenary(int rows, int columns) {
        int[][] scenary = new int[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (j == 0 || j == columns - 1) {
                    scenary[i][j] = 1; //Espacio límite
                }else{
                    scenary[i][j] = 0; //Espacio vacío
                }
            }
        }
        return scenary;
    }

}