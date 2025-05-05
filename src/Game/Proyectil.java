package Game;

public class Proyectil {
    public int x, y;
    public boolean activo;

    public Proyectil(int x, int y){
        this.x = x;
        this.y = y;
        activo = true;
    }

    public void mover(){
        y--;
        if(y<0){
            activo = false;
        }
    }
}
