package Game;

import javax.swing.*;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Servidor {
    // Levantar el servidor en el puerto 4444
    private static int numPlayer=0;
    private static List<Player> players = new ArrayList<>();
    private static List<Enemigo> enemies = new ArrayList<>();
    private static int wave = 1;
    private static int enemigosGenerados = 0;
    private static int enemigosMuertos = 0;
    private static int totalPorOleada = 5;
    private static boolean gameStarted = false;

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

        // Esperar palabra clave "Ready" para iniciar el juego
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Esperando a que el servidor reciba el comando 'Ready' para comenzar el juego...");
        String readyCommand = consoleReader.readLine();

        if (readyCommand.equalsIgnoreCase("Ready")) {
            gameStarted = true;
            System.out.println("El juego ha comenzado.");
        } else {
            System.out.println("El juego no comenzará hasta recibir 'Ready'.");
            return;
        }

        // Creando oleada de enemigos
        new Thread(() -> {
            while(gameStarted){
                if (enemigosMuertos < totalPorOleada){
                    int x = (int) (Math.random()*(columns-6))+6;
                    Enemigo enemy = new Enemigo(x, 0, wave);
                    synchronized (enemies){
                        enemies.add(enemy);
                    }
                    enemigosGenerados++;
                }

                // Pasar a la siguiente oleada
                if(enemigosMuertos >= totalPorOleada){
                    wave++;
                    enemigosGenerados = 0;
                    enemigosMuertos = 0;
                    //totalPorOleada += 10; // Aumentar la cantidad de enemigos por oleada
                    if(wave==2) totalPorOleada = 10;
                    else if (wave == 3) totalPorOleada = 20;
                    else {
                        System.out.println("Ganaste el juego");
                        JOptionPane.showMessageDialog(null, "Ganaste el juego");
                        return;
                    }

                    String msg = "Oleada " +wave+" iniciada";
                    System.out.println(msg);
                    javax.swing.JOptionPane.showMessageDialog(null, msg);
                }

                try{
                    Thread.sleep(2000);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }).start();

        // Mover Enemigos
        new Thread(() ->{
            while (gameStarted){
                synchronized (enemies){
                    for (Enemigo enemigo : enemies) {
                        if (enemigo.isActiv()) {
                            enemigo.actualizarMovimiento(); // Baja una fila (y++)
                        }
                    }
                }
                try{
                    Thread.sleep(100); // Esperar 1 segundo
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        // Creación de 5 enemigos
        /*for(int i=0; i<10; i++){
            int x = 5 + (int)(Math.random() * (columns-10));
            int y = 0;
            enemies.add(new Enemigo(x, y));
        }
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(2000); // Esperar 1 segundo
                    for (Enemigo enemigo : enemies) {
                        if (enemigo.isActiv()) {
                            enemigo.mover(); // Baja una fila (y++)
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();*/

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

    public static synchronized void enemigoMuerto(){
        enemigosMuertos++;
    }
    public static List<Enemigo> getEnemigos() {
        return enemies;
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