package Game;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerControl extends Thread{
    private Socket socket;
    private Player player;
    private int[][] scenary;
    private List<Player> players;

    public PlayerControl(Socket socket, Player player, int[][] scenary, List<Player> players) {
        this.socket = socket;
        this.player = player;
        this.scenary = scenary;
        this.players = players;
    }

    @Override
    public void run() {

        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.println("Conectado como " + player.getName());

            int min = 0, screen = 30;

            while (true) {
                // Leyendo input del jugador (para actualizar posición en caso teclee algo)
                if (in.ready()){
                    String input = in.readLine();
                    if (input != null){
                        processInput(input);
                    }
                }

                // Moviendo los proyectiles
                ArrayList<Proyectil> proyectiles = player.getProyectiles();
                for(Proyectil p: proyectiles){
                    if(p.activo){
                        p.mover();
                    }
                }

                proyectiles.removeIf(p -> !p.activo);

                String frame = scenaryInString(scenary, min, screen, player);
                out.println(frame);
                out.println("--Fin--"); // Para que el cliente sepa que terminó el frame
                out.flush();

                min++;
                if (min + screen >= scenary.length) {
                    min = 0;
                }

                Thread.sleep(100);
            }

        } catch (IOException | InterruptedException e) {
            System.out.println(player.getName() + " se desconectó.");
        }
    }

    // Método para convertir el escenario a String
    public String scenaryInString(int[][] scenary, int min, int screen, Player player) {
        StringBuilder scnr = new StringBuilder();
        for (int i = min; i < min + screen; i++) {
            for (int j = 0; j < scenary[i].length; j++) {
                //boolean foundPlayer = false;
                boolean dibujado = false; // Reemplaza a founPlayer porque este es más general y el foundPlayer será para dibujar compañeros u enemigos
                // Imprimir proyectil en pantalla
                for(Proyectil p : player.getProyectiles()){
                    if (i == min+p.y && j == p.x && p.activo) {
                        scnr.append("¡¡");
                        dibujado = true;
                        break;
                    }
                }

                // Imprimir jugador en pantalla
                if(!dibujado) {
                    boolean foundPlayer = false;
                    for (Player p : players) {
                        if (i == min + p.posY() && j == p.posX()) { // El i==min+p.posY() es para que el jugador se mueva en el escenario pero para nuestra perspectiva sea la misma
                            scnr.append(p == player ? "P" : "E"); // P es propio y E es enemigo u otro jugador
                            foundPlayer = true;
                            break;
                        }
                    }
                    if (!foundPlayer) {
                        if (scenary[i][j] == 1) {
                            scnr.append("#");
                        } else /* (scenary[i][j] == 0)*/ {
                            scnr.append(" ");
                        }
                    }
                }
            }
            scnr.append("\n");
        }
        return scnr.toString();
    }

    // Método para procesar la tecla ingresada por el jugador
    public void processInput(String input){
        int x = player.posX();
        int y = player.posY();
        switch(input.toUpperCase()){
            case "W": if (y>0) y--; break;
            case "S": if (y<scenary.length-1) y++; break;
            case "A": if (x>0) x-=2; break;
            case "D": if (x<scenary[0].length-1) x+=2; break;
            case "F": player.disparar(); return;
        }

        player.position(x, y);
    }
}
