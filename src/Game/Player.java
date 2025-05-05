package Game;

public class Player{
    private String name;
    private int x, y;

    public Player(String name,int x, int y){
        this.name = name;
        this.x = x;
        this.y = y;
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

}
