import javax.swing.*;
import java.awt.*;

class Pacman{

    private int size;
    private int x;
    private int y;
    private String direction;
    private Status mouth;
    private int mouthStartAngle;
    private int mouthArcSize;
    private int mouthOpenValue;
    private int mouthClosedValue;
    private boolean dead;
    private int movement;

    private final int MOUTH_SIZE = 60;
    private final int MOUTH_MOVEMENT = 20;
    private final int RIGHT_CLOSED_VALUE = 0;
    private final int LEFT_CLOSED_VALUE = 180;
    private final int UP_CLOSED_VALUE = 90;
    private final int DOWN_CLOSED_VALUE = -90;
    private final int MOUTH_CLOSED_VALUE = 360;
    private final int DEATH_SPEED = 5;
    private final int DEAD_VALUE = 0;

    private enum Status{

        OPENING,
        CLOSING
    }

    Pacman(int size){;
        this.size = size;
        mouthStartAngle = 0;
        mouthArcSize = 360;
        dead = false;
        direction = "right";
        movement = 0;
    }

    public void setPosition(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void setDirection(String direction){

        if(!dead){
           this.direction = direction;
           
           if(direction.equals("right")){
               mouthStartAngle = RIGHT_CLOSED_VALUE;
           }
           if(direction.equals("left")){
               mouthStartAngle = LEFT_CLOSED_VALUE;
           }
           if(direction.equals("up")){
               mouthStartAngle = UP_CLOSED_VALUE;
           }
           if(direction.equals("down")){
               mouthStartAngle = DOWN_CLOSED_VALUE;
           }

           mouthArcSize = MOUTH_CLOSED_VALUE; 
       }
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public int getSize(){
        return size;
    }

    public String getDirection(){
        return direction;
    }

    public void draw(Graphics g){

        g.setColor(Color.yellow);

        g.fillArc(x*size, y*size, size, size, mouthStartAngle, mouthArcSize);
    }

    public void move(char[][] mapArray, String direction){

        if(canMove(mapArray, direction) && !dead){

            if(direction.equals("right")){
                x += 1;
            }
            if(direction.equals("left")){                
                x -= 1;
            }
            if(direction.equals("up")){
                y -= 1;
            }
            if(direction.equals("down")){
                y += 1;
            }
        }
        // If at right side of tunnel, switch to other side
        if(mapArray[x][y] == 'r'){
            x = 1;
        }
        // If at left side of tunnel, switch to other side
        if(mapArray[x][y] == 'l'){
            x = mapArray[x].length;
        }
    }

    public void animate(char[][] mapArray){

        if(dead){
            if(mouthArcSize >= DEAD_VALUE){
                mouthStartAngle += DEATH_SPEED;
                mouthArcSize -= DEATH_SPEED*2;                
            }
        }

        else if(canMove(mapArray, direction)){

            if(mouth == Status.OPENING){
                mouthStartAngle += MOUTH_MOVEMENT;
                mouthArcSize -= MOUTH_MOVEMENT*2;
            }
            else if(mouth == Status.CLOSING){
                mouthStartAngle -= MOUTH_MOVEMENT;
                mouthArcSize += MOUTH_MOVEMENT*2;
            }
            if(mouthArcSize <= MOUTH_CLOSED_VALUE - MOUTH_SIZE){
                mouth = Status.CLOSING;
            }

            if(mouthArcSize >= MOUTH_CLOSED_VALUE){
                mouth = Status.OPENING;
            }
        }
    }

    public void die(){
        dead = true;
    }

    private int getStartAngle(String direction){

        int startAngle = 0;

        if(direction.equals("right")){
            startAngle = RIGHT_CLOSED_VALUE;
        }
        if(direction.equals("left")){
            startAngle = LEFT_CLOSED_VALUE;
        }
        if(direction.equals("up")){
            startAngle = UP_CLOSED_VALUE;
        }
        if(direction.equals("down")){
            startAngle = DOWN_CLOSED_VALUE;
        }
        
        return startAngle;
    }

    // Checks that a move is valid.
    private boolean canMove(char[][] mapArray, String direction){

        int newPositionX = x;
        int newPositionY = y; 

        if(direction == "left"){
            newPositionX -= 1;
        }
        if(direction == "right"){
            newPositionX += 1;
        }
        if(direction == "up"){
            newPositionY -= 1;
        }
        if(direction == "down"){
            newPositionY += 1;
        }

        // If new location after move is a wall
        if(mapArray[newPositionX][newPositionY] == 'x'){

            return false;
        }
        else{

            return true;
        }
    }
}