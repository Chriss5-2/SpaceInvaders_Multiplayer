package Game;

import java.util.ArrayList;

public class Player{
    private String name;
    private int x, y;
    private ArrayList<Proyectil> proyectiles = new ArrayList<>(); // Creando lista de proyectiles
    //private int vida = 3;
    //private boolean activo = true; // Cambiado a booleano

    public Player(String name,int x, int y){
        this.name = name;
        this.x = x;
        this.y = y;
        this.proyectiles = new ArrayList<>(); // Creando lista de proyectiles en el constructor
    }

    /*public int getVida(){
        return vida;
    }*/

    public int posX(){
        return x;
    }
    public int posY(){
        return y;
    }

    public String getName(){
        return name;
    }

    public void position(int x, int y){
        this.x = x;
        this.y = y;
    }

    public ArrayList<Proyectil> getProyectiles(){
        return proyectiles;
    }

    public void disparar(){
        proyectiles.add(new Proyectil(x, y-1));
    }

    /*public void recibirDanio() {
        vida--;
        if (vida <= 0) {
            activo = false; // Podrías tener un booleano que indica si está vivo
            System.out.println(name + " ha muerto.");
        } else {
            System.out.println(name + " fue golpeado. Vida restante: " + vida);
        }
    }

    public boolean estaActivo() {
        return activo;
    }*/

}
