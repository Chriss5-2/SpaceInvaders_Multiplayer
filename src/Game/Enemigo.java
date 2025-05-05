package Game;

public class Enemigo {
    int x, y;
    boolean activo = true;

    public Enemigo(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void mover(){
        y++;
    }

    public boolean isActiv(){
        return activo;
    }

    public void destruir(){
        activo = false;
    }
}
