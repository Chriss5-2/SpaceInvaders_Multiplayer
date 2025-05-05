package Game;

public class Enemigo {
    int x, y;
    boolean activo = true;
    private int velocidad;
    private int delayActual = 0;

    public Enemigo(int x, int y, int wave){
        this.x = x;
        this.y = y;
        this.activo= true;

        if(wave == 1) velocidad=30;
        else if (wave == 2) velocidad = 15;
        else velocidad = 5;
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

    public void actualizarMovimiento(){
        delayActual++;
        if(delayActual >= velocidad){
            y++;
            delayActual = 0;
        }
    }
}
