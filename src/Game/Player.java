package Game;

import java.util.ArrayList;

public class Player{
    private String name;
    private int x, y;
    private ArrayList<Proyectil> proyectiles = new ArrayList<>(); // Creando lista de proyectiles

    public Player(String name,int x, int y){
        this.name = name;
        this.x = x;
        this.y = y;
        this.proyectiles = new ArrayList<>(); // Creando lista de proyectiles en el constructor
    }

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

}
