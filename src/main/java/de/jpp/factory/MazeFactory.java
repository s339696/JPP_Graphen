package de.jpp.factory;

import de.jpp.io.interfaces.GraphReader;
import de.jpp.maze.Maze;
import de.jpp.maze.MazeImpl;
import de.jpp.maze.MazeReader;
import de.jpp.model.TwoDimGraph;
import de.jpp.model.XYNode;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;


import static java.awt.image.BufferedImage.TYPE_INT_RGB;

public class MazeFactory {


    /**
     * Creates a new empty maze with the specified width and height
     *
     * @param width  the width
     * @param height the height
     * @return a new empty maze with the specified width and height
     */
    public Maze getEmptyMaze(int width, int height) {

        return new MazeImpl(width, height);
    }

    /**
     * Returns a pixel representation of the specified maze
     *
     * @param maze the maze
     * @return a pixel representation of the specified maze
     */
    public BufferedImage getMazeAsImage(Maze maze) {
        BufferedImage img = new BufferedImage(maze.getWidth()*2 +1, maze.getHeight()*2 +1, TYPE_INT_RGB);



        // turn cells white
        for (int y = 1; y < img.getHeight(); y+=2){
            for (int x= 1; x < img.getWidth(); x+=2){
                img.setRGB(x,y, Color.WHITE.getRGB());
            }
        }

        //black dots in the middle
        for (int y = 2; y < img.getHeight(); y+=2){
            for (int x= 2; x < img.getWidth(); x+=2){
                img.setRGB(x,y, Color.BLACK.getRGB());
            }
        }


        //decide which hWalls are closed
        if (maze.getWidth()>1){

        int yHWAll = 0;
        int xHWALL = 0;
        for (int x = 2; x < img.getWidth()-1; x+=2){
            for (int y = 1; y < img.getHeight(); y+=2){
                if (maze.isHWallActive(xHWALL, yHWAll)){
                    img.setRGB(x, y, Color.BLACK.getRGB());
                }else {
                    img.setRGB(x, y, Color.WHITE.getRGB());
                }
                yHWAll++;
            }
            xHWALL++;
            yHWAll = 0;
            }

        }

        //decide which vWalls are closed
        if (maze.getHeight() > 1){

        int yVWALL = 0;
        int xVWALL = 0;
        for (int x = 1;x < img.getWidth(); x+=2){
            for (int y = 2; y < img.getHeight()-1;y +=2){
                if (maze.isVWallActive(xVWALL,yVWALL)){
                    System.out.println("x:" + x + "y:"+ y);
                    img.setRGB(x,y, Color.BLACK.getRGB());
                } else{
                    img.setRGB(x,y, Color.WHITE.getRGB());
                }
                yVWALL++;
            }
            xVWALL++;
            yVWALL = 0;
            }
        }


        //paint the X- edge black
        for (int x =0; x <= img.getWidth()-1; x++){
            img.setRGB(x, 0, Color.BLACK.getRGB());
            img.setRGB(x, img.getHeight()-1, Color.BLACK.getRGB());
        }

        //paint the Y-Edge Black
        for (int y = 0; y <= img.getHeight()-1; y++){
            img.setRGB(0, y, Color.BLACK.getRGB());
            img.setRGB(img.getWidth()-1, y, Color.BLACK.getRGB());
        }


    return img;

    }

    public static void main(String[] args) throws IOException {
        MazeImpl maze = new MazeImpl(5,5);
        maze.setHWall(0,0,true);
        Random rnd = new Random();
        Maze maze2 = getRandomMaze(rnd, 10,9);

        MazeFactory factory = new MazeFactory();
        BufferedImage img = factory.getMazeAsImage(maze2);
        File file = new File("C:\\Users\\Bastian\\Desktop\\basti\\Java-Aufgaben\\test.png");

        ImageIO.write(img, "png", file);
    }

    /**
     * Returns a random maze with specified width, height created by the specified algorithm specified from the instruction <br>
     * Random numbers are only taken from the specified random number generator (RNG) and only as specified in the instruction
     *
     * @param ran    the random number generator (RNG)
     * @param width  the width
     * @param height the height

     * @return a random maze with specified width and height
     */
    public static Maze getRandomMaze(Random ran, int width, int height){
        MazeImpl maze = new MazeImpl(width, height);

        if (height<2|| width<2){
            System.out.println("Maze is to small");
        } else {
            if (width>height){
                divideMazeHorizontal(maze, ran,0, width, 0, height);
            } else if (height>width){
                divideMazeVertical(maze, ran, 0, width, 0, height);
            } else{
                if (ran.nextBoolean()){
                    divideMazeHorizontal(maze, ran,0, width, 0, height);
                } else{
                    divideMazeVertical(maze, ran, 0, width, 0, height);

                }
            }
        }

        return maze;
    }
    private static void divideMazeVertical(Maze maze, Random ran, int minX, int maxX, int minY, int maxY) {
        int rangeX = (maxX-1) - minX;
        int rangeY = (maxY-1) -minY;

        int rnd1 = ran.nextInt(rangeY) + minY;
        int rnd2 = ran.nextInt(rangeX) + minX;

        for (int j = minX; j<maxX; j++){
            if (rnd2 !=j){
                maze.setVWall(j, rnd1, true);
            }
        }

        int newMinXMaze1 = minX;
        int newMaxXMaze1 = maxX;

        int newMinYMaze1 = minY;
        int newMaxYMaze1 = rnd1+1;

        int rangeMaze1X = newMaxXMaze1 - newMinXMaze1;
        int rangeMaze1Y = newMaxYMaze1 - newMinYMaze1;

        int newMinXMaze2 = minX;
        int newMaxXMaze2 = maxX;

        int newMinYMaze2 = rnd1+1;
        int newMaxYMaze2 = maxY;

        int rangeMaze2X = newMaxXMaze2 - newMinXMaze2;
        int rangeMaze2Y = newMaxYMaze2 - newMinYMaze2;

        decideMazeSide(maze, ran, newMinXMaze1, newMaxXMaze1, newMinYMaze1, newMaxYMaze1, rangeMaze1X, rangeMaze1Y);
        decideMazeSide(maze, ran, newMinXMaze2, newMaxXMaze2, newMinYMaze2, newMaxYMaze2, rangeMaze2X, rangeMaze2Y);

    }

    public static void divideMazeHorizontal(Maze maze, Random ran, int minX, int maxX, int minY, int maxY){
        int rangeX = (maxX-1) - minX;
        int rangeY = (maxY-1) -minY;

        int rnd1 = ran.nextInt(rangeX) + minX;
        int rnd2 = ran.nextInt(rangeY) + minY;


        for (int i = minY; i<maxY; i++){
            if (rnd2 != i){
                maze.setHWall(rnd1, i,true);
            }
        }


        int newMinXMaze1 = minX;
        int newMaxXMaze1 = rnd1+1;

        int newMinYMaze1 = minY;
        int newMaxYMaze1 = maxY;

        int rangeMaze1X = newMaxXMaze1 - newMinXMaze1;
        int rangeMaze1Y = newMaxYMaze1 - newMinYMaze1;

        int newMinXMaze2 = rnd1+1;
        int newMaxXMaze2 = maxX;

        int newMinYMaze2 = minY;
        int newMaxYMaze2 = maxY;

        int rangeMaze2X = newMaxXMaze2 - newMinXMaze2;
        int rangeMaze2Y = newMaxYMaze2 - newMinYMaze2;

        decideMazeSide(maze, ran, newMinXMaze1, newMaxXMaze1, newMinYMaze1, newMaxYMaze1, rangeMaze1X, rangeMaze1Y);
        decideMazeSide(maze, ran, newMinXMaze2, newMaxXMaze2, newMinYMaze2, newMaxYMaze2, rangeMaze2X, rangeMaze2Y);


    }



    private static void decideMazeSide(Maze maze, Random ran, int newMinXMaze, int newMaxXMaze, int newMinYMaze, int newMaxYMaze, int rangeMazeX, int rangeMazeY) {
        if (rangeMazeX<2 || rangeMazeY<2){

        }else{
            if (rangeMazeX>rangeMazeY){
                divideMazeHorizontal(maze, ran, newMinXMaze, newMaxXMaze, newMinYMaze, newMaxYMaze);
            } else if (rangeMazeX<rangeMazeY){
                divideMazeVertical(maze, ran, newMinXMaze, newMaxXMaze, newMinYMaze, newMaxYMaze);
            } else{
                if (ran.nextBoolean()){
                    divideMazeHorizontal(maze, ran, newMinXMaze, newMaxXMaze, newMinYMaze, newMaxYMaze);
                } else{
                    divideMazeVertical(maze, ran, newMinXMaze, newMaxXMaze, newMinYMaze, newMaxYMaze);
                }
            }
        }
    }



    /**
     * Returns a GraphReader which parses a TwoDimGraph from a Maze-Object
     *
     * @return a GraphReader which parses a TwoDimGraph from a Maze-Object
     */
    public GraphReader<XYNode, Double, TwoDimGraph, Maze> getMazeReader() {
        return new MazeReader();
    }

}
