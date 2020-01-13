package de.jpp.maze;

import java.util.Arrays;

public class MazeImpl implements Maze {

    int[][] cell;
    boolean[][] hWall;
    boolean[][] vWall;

    public MazeImpl(int width, int height){
        hWall = new boolean[width-1][height];
        vWall = new boolean[width][height-1];
        cell = new int[width][height];
    }

    @Override
    public String toString() {
        return "MazeImpl{" +
                "cell=" + Arrays.toString(cell) +
                ", hWall=" + Arrays.toString(hWall) +
                ", vWall=" + Arrays.toString(vWall) +
                '}';
    }

    /**
     * Sets the horizontal wall active (non-passable) or inactive (passable)
     *
     * @param x          the x coordinate of the horizontal wall
     * @param y          the y coordinate of the horizontal wall
     * @param wallActive whether the wall is active (non-passable) or not
     */


    @Override
    public void setHWall(int x, int y, boolean wallActive) {
        hWall[x][y] = wallActive;
    }

    /**
     * Sets the vertical wall active (non-passable) or inactive (passable)
     *
     * @param x          the x coordinate of the vertical wall
     * @param y          the y coordinate of the vertical wall
     * @param wallActive whether the wall is active (non-passable) or not
     */
    @Override
    public void setVWall(int x, int y, boolean wallActive) {
        vWall[x][y] = wallActive;

    }

    /**
     * Sets all vertical and horizontal walls active (non-passable) or inactive (passable)
     *
     * @param wallActive whether the walls are active (non-passable) or not
     */
    @Override
    public void setAllWalls(boolean wallActive) {
        for (int x =0; x < hWall.length; x++){
            for (int y=0; y < hWall[x].length; y++){
                setHWall(x, y, wallActive);
            }
        }

        for (int x =0; x < vWall.length; x++){
            for (int y=0; y < vWall[x].length; y++){
                setVWall(x, y, wallActive);
            }
        }


    }

    /**
     * returns the width (amount of cells in a row) of this maze
     *
     * @return the width (amount of cells in a row) of this maze
     */
    @Override
    public int getWidth() {
        return cell.length;
    }

    /**
     * returns the height (amount of cells in a column) of this maze
     *
     * @return the height (amount of cells in a column) of this maze
     */
    @Override
    public int getHeight() {
        return cell[0].length;
    }

    /**
     * Returns whether the horizontal wall at the specified coordinate is active (non-passable) or inactive (passable)
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @return whether the horizontal wall at the specified coordinate is active (non-passable) or inactive (passable)
     */
    @Override
    public boolean isHWallActive(int x, int y) {
        return hWall[x][y];
    }

    /**
     * Returns whether the vertical wall at the specified coordinate is active (non-passable) or inactive (passable)
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @return whether the vertical wall at the specified coordinate is active (non-passable) or inactive (passable)
     */
    @Override
    public boolean isVWallActive(int x, int y) {
        return vWall[x][y];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MazeImpl maze = (MazeImpl) o;
        return Arrays.deepEquals(cell, maze.cell) &&
                Arrays.deepEquals(hWall, maze.hWall) &&
                Arrays.deepEquals(vWall, maze.vWall);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(cell);
        result = 31 * result + Arrays.hashCode(hWall);
        result = 31 * result + Arrays.hashCode(vWall);
        return result;
    }
}
