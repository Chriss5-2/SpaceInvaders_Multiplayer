package Game;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Servidor {
    // Levantar el servidor en el puerto 4444
    private static int numPlayer=0;
    private static List<Player> players = new ArrayList<>();
    private static List<Enemigo> enemies = new ArrayList<>();

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

        // Creación de 5 enemigos
        for(int i=0; i<5; i++){
            int x = 5 + (int)(Math.random() * (columns-10));
            int y = 0;
            enemies.add(new Enemigo(x, y));
        }
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000); // Esperar 1 segundo
                    for (Enemigo enemigo : enemies) {
                        if (enemigo.isActiv()) {
                            enemigo.mover(); // Baja una fila (y++)
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        int posX=10, posY=15;
        while (true) {
            //Conectamos con el jugador entrante y agregamos uno al nro de jugadores
            Socket plSckt = server.accept();
            numPlayer++;

            Player player = new Player("Jugador" + numPlayer, posX, posY);
            players.add(player);
            System.out.println(player.getName() + " conectado");
            posX=posX+15;
            PlayerControl control = new PlayerControl(plSckt, player, scenary, players, enemies);
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