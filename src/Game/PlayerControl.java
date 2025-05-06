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
    private List<Enemigo> enemigos;

    public PlayerControl(Socket socket, Player player, int[][] scenary, List<Player> players, List<Enemigo> enemigos) {
        this.socket = socket;
        this.player = player;
        this.scenary = scenary;
        this.players = players;
        this.enemigos = enemigos;
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
                        // Verificando si el proyectil choca con un enemigo
                        for(Enemigo enemigo: Servidor.getEnemigos()){
                            if ((p.x == enemigo.x||p.x==enemigo.x-1||p.x==enemigo.x+1||p.x==enemigo.x+2||p.x==enemigo.x+3||p.x==enemigo.x+4||p.x==enemigo.x+5) && (p.y == enemigo.y||p.y == enemigo.y-1) && enemigo.isActiv()) {
                                enemigo.destruir();
                                p.activo = false; // Destruir el proyectil
                                Servidor.enemigoMuerto();
                                break;
                            }
                        }
                    }
                }
                proyectiles.removeIf(p -> !p.activo);

                //String frame = scenaryInString(scenary, min, screen, player);
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

                // Imprimir enemigos en pantalla
                if(!dibujado){
                    for(Enemigo enemigo: Servidor.getEnemigos()){
                        if (!enemigo.isActiv()) continue;

                        int cx = enemigo.x;
                        int cy = enemigo.y;

                        // Dibujar parte superior de la nave
                        if (i == min + cy - 1 && j == cx - 1) { scnr.append("┌"); dibujado = true; break; }
                        if (i == min + cy - 1 && j == cx)     { scnr.append("o"); dibujado = true; break; }
                        if (i == min + cy - 1 && j == cx+1)     { scnr.append("┘"); dibujado = true; break; }
                        //if (i == min + cy - 2 && j == cx + 1) { scnr.append("┐"); dibujado = true; break; }
                        if (i == min + cy - 1 && j == cx+2)     { scnr.append("-"); dibujado = true; break; }
                        if (i == min + cy - 1 && j == cx+3)     { scnr.append("-"); dibujado = true; break; }
                        //if (i == min + cy - 2 && j == cx + 4) { scnr.append("┌"); dibujado = true; break; }
                        if (i == min + cy - 1 && j == cx+4)     { scnr.append("└"); dibujado = true; break; }
                        if (i == min + cy - 1 && j == cx+5)     { scnr.append("o"); dibujado = true; break; }
                        if (i == min + cy - 1 && j == cx + 6) { scnr.append("┐"); dibujado = true; break; }

                        // Dibujar centro
                        //if (i == min + cy && j == cx - 3) { scnr.append("|"); dibujado = true; break; }
                        if (i == min + cy && j == cx - 2) { scnr.append("┌"); dibujado = true; break; }
                        if (i == min + cy && j == cx - 1)     { scnr.append("|"); dibujado = true; break; }
                        if (i == min + cy && j == cx)         { scnr.append(" "); dibujado = true; break; }
                        if (i == min + cy && j == cx+1)         { scnr.append("°"); dibujado = true; break; }
                        if (i == min + cy && j == cx+2)         { scnr.append(" "); dibujado = true; break; }
                        if (i == min + cy && j == cx+3)         { scnr.append(" "); dibujado = true; break; }
                        if (i == min + cy && j == cx+4)         { scnr.append("°"); dibujado = true; break; }
                        if (i == min + cy && j == cx+5)         { scnr.append(" "); dibujado = true; break; }
                        if (i == min + cy && j == cx + 6)     { scnr.append("|"); dibujado = true; break; }
                        if (i == min + cy && j == cx + 7) { scnr.append("┐"); dibujado = true; break; }
                        //if (i == min + cy && j == cx + 8) { scnr.append("|"); dibujado = true; break; }

                        // Dibujar parte inferior
                        if (i == min + cy + 1 && j == cx - 1) { scnr.append("└"); dibujado = true; break; }
                        if (i == min + cy + 1 && j == cx)     { scnr.append("o"); dibujado = true; break; }
                        if (i == min + cy + 1 && j == cx+1)     { scnr.append("‾");scnr.append("‾"); dibujado = true; break; }
                        if (i == min + cy + 1 && j == cx+2)     { scnr.append("‾"); dibujado = true; break; }
                        if (i == min + cy + 1 && j == cx+3)     { scnr.append("‾"); dibujado = true; break; }
                        if (i == min + cy + 1 && j == cx+4)     { scnr.append("‾");scnr.append("‾"); dibujado = true; break; }
                        if (i == min + cy + 1 && j == cx+5)     { scnr.append("o"); dibujado = true; break; }
                        if (i == min + cy + 1 && j == cx + 6) { scnr.append("┘"); dibujado = true; break; }

                        /*if (i == min+enemigo.y && j == enemigo.x && enemigo.isActiv()) {
                            scnr.append("X");
                            dibujado = true;
                            break;
                        }*/
                    }
                }
                // Imprimir proyectil en pantalla
                for(Proyectil p : player.getProyectiles()){
                    if (i == min+p.y && j == p.x && p.activo) {
                        scnr.append("¡");
                        dibujado = true;
                        break;
                    }
                    /*if (i == min+p.y && j == p.x+2 && p.activo) {
                        scnr.append("¡");
                        dibujado = true;
                        break;
                    }*/
                }

                // Imprimir jugador en pantalla
                if(!dibujado) {
                    //boolean foundPlayer = false;
                    for (Player p : players) {
                        int px = p.posX();
                        int py = p.posY();
                        boolean local = (p == player);
                        // Dibujar jugador local (con forma)
                        if (local) {
                            // Superior
                            if (i == min + py - 1 && j == px-3)       { scnr.append("┌"); dibujado = true; break; }
                            if (i == min + py - 1 && j == px-2)       { scnr.append("^"); dibujado = true; break; }
                            if (i == min + py - 1 && j == px-1)       { scnr.append("-"); dibujado = true; break; }
                            if (i == min + py - 1 && j == px)       { scnr.append("|"); dibujado = true; break; }
                            if (i == min + py - 1 && j == px+1)       { scnr.append("-"); dibujado = true; break; }
                            if (i == min + py - 1 && j == px+2)       { scnr.append("^"); dibujado = true; break; }
                            if (i == min + py - 1 && j == px+3)       { scnr.append("┐"); dibujado = true; break; }
                            //Medio
                            if (i == min + py && j == px - 3)       { scnr.append("└"); dibujado = true; break; }
                            if (i == min + py && j == px - 2)       { scnr.append("U"); dibujado = true; break; }
                            if (i == min + py && j == px - 1)       { scnr.append("-"); dibujado = true; break; }
                            if (i == min + py && j == px)           { scnr.append("°"); dibujado = true; break; }
                            if (i == min + py && j == px + 1)       { scnr.append("-"); dibujado = true; break; }
                            if (i == min + py && j == px + 2)       { scnr.append("U"); dibujado = true; break; }
                            if (i == min + py && j == px + 3)       { scnr.append("┘"); dibujado = true; break; }
                            //Inferior
                            /*if (i == min + py + 1 && j == px-3)       { scnr.append(" "); dibujado = true; break; }
                            if (i == min + py + 1 && j == px-2)       { scnr.append("U"); dibujado = true; break; }
                            if (i == min + py + 1 && j == px-1)       { scnr.append("‾"); dibujado = true; break; }
                            if (i == min + py + 1 && j == px)       { scnr.append("‾");scnr.append("‾"); dibujado = true; break; }
                            if (i == min + py + 1 && j == px+1)       { scnr.append("‾"); dibujado = true; break; }
                            if (i == min + py + 1 && j == px+2)       { scnr.append("U"); dibujado = true; break; }
                            if (i == min + py + 1 && j == px+3)       { scnr.append(" "); dibujado = true; break; }
                            */
                        }
                        else {
                            if (i == min + py && j == px)           { scnr.append("o"); dibujado = true; break; }
                        }
                        // El i==min+p.posY() es para que el jugador se mueva en el escenario pero para nuestra perspectiva sea la misma
                        /*if (i == min + p.posY() && j == p.posX()) {
                            scnr.append(p == player ? "P" : "E"); // P es propio y E es enemigo u otro jugador
                            //foundPlayer = true;
                            break;
                        }*/
                    }
                    //

                    //
                    if (!dibujado/*foundPlayer*/) {
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
