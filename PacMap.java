import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;

class PacMap extends JPanel implements KeyListener{

    private char mapArray[][];
    private int width;
    private int height;
    private boolean mapSetUp = false;
    private boolean playerWon = false;
    private boolean gameOver = false;

    // GRID_MULTIPLIER sets the size of each square
    public static final int GRID_MULTIPLIER = 25;
    
    public String[] currentLevel;
    private Pacman pacman;
    private Ghost blinky;
    private Ghost inky;

    PacMap(String[] level){

        this.width = level[0].length();
        this.height = level.length;
        this.currentLevel = level;
        this.addKeyListener(this);
        this.setFocusable(true);

        // Initialize characters and specify size and/images
        this.pacman = new Pacman(GRID_MULTIPLIER);
        
        URL u = this.getClass().getResource("images/blinky.png");
        ImageIcon blinkyIcon = new ImageIcon(u);
        this.blinky = new Ghost(GRID_MULTIPLIER, blinkyIcon);

        u = this.getClass().getResource("images/inky.png");
        ImageIcon inkyIcon = new ImageIcon(u);
        this.inky = new Ghost(GRID_MULTIPLIER, inkyIcon);

        // Initialize char array for game map logic
        mapArray = new char[width][height];

        // Set up board background
        setPreferredSize(new Dimension(width*GRID_MULTIPLIER, height*GRID_MULTIPLIER));
        setBackground(Color.black);
    }

    // Monitor key presses to control Pacman
    public void keyPressed(KeyEvent e){

        int key = e.getKeyCode();

        if(key == KeyEvent.VK_RIGHT){
            pacman.setDirection("right");
        }

        if(key == KeyEvent.VK_LEFT){
            pacman.setDirection("left");
        }

        if(key == KeyEvent.VK_UP){
            pacman.setDirection("up");
        }

        if(key == KeyEvent.VK_DOWN){
            pacman.setDirection("down");
        }
    }

    public void keyReleased(KeyEvent e){}
    public void keyTyped(KeyEvent e){}

    public void run(){

        // Get character start locations from map string if not already loaded
        if(!mapSetUp){
            pacman.setPosition(getCharacterLocation('x', "pacman"), getCharacterLocation('y', "pacman"));
            blinky.setPosition(getCharacterLocation('x', "blinky"), getCharacterLocation('y', "blinky"));
            inky.setPosition(getCharacterLocation('x', "inky"), getCharacterLocation('y', "inky"));
            mapSetUp = true;
        }

        // Game loop
        Timer timer = new Timer(10, new ActionListener(){
            int count = 0;
            public void actionPerformed(ActionEvent e){

                count++;

                // Check if Pacman has been caught by a ghost
                if((pacman.getX() == blinky.getX() && pacman.getY() == blinky.getY()) ||
                    (pacman.getX() == inky.getX() && pacman.getY() == inky.getY())){
                    gameOver = true;
                    blinky.makeInvisible();
                    inky.makeInvisible();
                    pacman.die();
                }
                // Pacman position is updated every 15 repaints
                if(count%15 == 0 && !gameOver && !playerWon){
                    pacman.move(mapArray, pacman.getDirection());
                    blinky.move(mapArray, blinky.getDirection());
                    inky.move(mapArray, inky.getDirection());
                    checkForWin();
                    updateMap();
                }
                // Animate Pacman every 5 repaints
                if(count%5 == 0){
                    pacman.animate(mapArray);
                }
                repaint();
            }
        });

        timer.start();
    }

    public void populate(){
        for(int row=0; row<height; row++){
            for(int col=0; col<width; col++){
                mapArray[col][row] = currentLevel[row].charAt(col);
            }
        }
    }

    // Print map to console
    public void display(){
        for(int row=0; row<height; row++){
            for(int col=0; col<width; col++){
                System.out.print(mapArray[col][row]);
            }
            System.out.println();
        }
    }

    public void updateMap(){

        int pacmanX = pacman.getX();
        int pacmanY = pacman.getY();

        // Overwrite any pills eaten with the 'p' character 
        for(int row=0; row<height; row++){
            for(int col=0; col<width; col++){
                if(col == pacmanX && row == pacmanY && mapArray[col][row] == '.'){
                    mapArray[col][row] = 'p';
                }
            }
        }
    }

    public void checkForWin(){
        int pillCount = 0;
        for(int row=0; row<height; row++){
            for(int col=0; col<width; col++){
                if(mapArray[col][row] == '.'){
                    pillCount++;
                }
            }
        }
        if(pillCount == 0){
            playerWon = true;
        }
    }

    // Draw map to screen.
    private void drawMap(Graphics g){

        // Get character locations
        int pacmanX = pacman.getX();
        int pacmanY = pacman.getY();
        int blinkyX = blinky.getX();
        int blinkyY = blinky.getY();
        int inkyX = inky.getX();
        int inkyY = inky.getY();

        // Set size of pills (dots to be eaten)
        int pillSize = GRID_MULTIPLIER/4;

        for(int row=0; row<height; row++){
            for(int col=0; col<width; col++){

                if(mapArray[col][row] == 'x'){
                    g.setColor(Color.blue);
                    g.fillRect(col*GRID_MULTIPLIER, row*GRID_MULTIPLIER, GRID_MULTIPLIER , GRID_MULTIPLIER);                    
                }

                if(mapArray[col][row] == '.'){
                    g.setColor(Color.white);
                    g.fillOval((col*GRID_MULTIPLIER)+GRID_MULTIPLIER/2 - 2, (row*GRID_MULTIPLIER)+GRID_MULTIPLIER/2 - 2, pillSize, pillSize);
                }
                if(col == pacmanX && row == pacmanY){
                    pacman.draw(g);
                }
                if(col == blinkyX && row == blinkyY){
                    blinky.draw(g);
                }
                if(col == inkyX && row == inkyY){
                    inky.draw(g);
                }
            }
        }
    }

    // Returns Pacman's start location for x either or y axis based on map string
    // Returns null if character is not found
    private Integer getCharacterLocation(char axis, String character){

        char characterSymbol = ' ';
        Integer location = null;

        if(character.equals("pacman")){
            characterSymbol = 'p';
        }
        if(character.equals("blinky")){
            characterSymbol = 'b';
        }
        if(character.equals("inky")){
            characterSymbol = 'i';
        }

        for(int row=0; row<height; row++){
            for(int col=0; col<width; col++){
                if(mapArray[col][row] == characterSymbol){
                    if(axis == 'x'){
                        location = col;
                    }
                    else if(axis == 'y'){
                        location = row;
                    }
                }
            }
        }
        return location;
    }

    public void paintComponent(Graphics g0){
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0;
        drawMap(g);
    }
}