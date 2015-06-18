import javax.swing.*;
import java.awt.*;
import java.util.*;

class Ghost{

    private int size;
    private int x;
    private int y;
    private String direction;
    private ImageIcon icon;
    private Image img;
    private boolean visible;
    private int moveCount;

    public Ghost(int size, ImageIcon icon){
        this.size = size;
        this.icon = icon;
        this.img = icon.getImage();
        visible = true;
        direction = "right";
        moveCount = 0;
    }

    public void setPosition(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public String getDirection(){
        return direction;
    }

    public void makeInvisible(){
        visible = false;
    }

    public void draw(Graphics g){
        if(visible){
            g.drawImage(img, x*size, y*size, size, size, null);            
        }
    }

    public void move(char[][] mapArray, String direction){

        if (tunnelFound(mapArray)){
            return;
        }

        // To introduce slightly more random behaviour
        // Ghost will change direction every 20 moves
        else if(moveCount > 20){
            getNextMove(mapArray);
            moveCount = 0;
        }

        else if(canMove(mapArray, direction)){

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

        else{
            getNextMove(mapArray);
        }

        moveCount ++;
    }

    // Very basic AI
    private void getNextMove(char[][] mapArray){

        boolean moved = false;
        Random random = new Random();
        String[] moves = {"right", "left", "up", "down"};
        Integer nextMove = null;
        while(!moved){
            // Pick a random move from moves array
            nextMove = random.nextInt(4 - 0) + 0;
            if(canMove(mapArray, moves[nextMove])){
                moved = true;
                direction = moves[nextMove];
            }
        }
    }

    // Checks whether character is at the edge of the map tunnel
    private boolean tunnelFound(char[][] mapArray){

        boolean found = false;

        // If at right side of tunnel, switch to other side
        if(mapArray[x][y] == 'r'){
            x = 1;
            found = true;
        }
        // If at left side of tunnel, switch to other side
        if(mapArray[x][y] == 'l'){
            x = mapArray[x].length;
            found = true;
        }
        return found;
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