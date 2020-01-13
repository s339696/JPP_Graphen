package de.jpp.maze;

import de.jpp.io.interfaces.GraphReader;
import de.jpp.io.interfaces.ParseException;
import de.jpp.model.TwoDimGraph;
import de.jpp.model.XYNode;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class MazeReader implements GraphReader<XYNode, Double, TwoDimGraph, Maze> {
    /**
     * Creates a graph from the specified input source
     *
     * @param input the input source
     * @return a graph from the input source
     * @throws ParseException if the input cannot be parsed correctly
     */
    @Override
    public TwoDimGraph read(Maze input) throws ParseException {
        TwoDimGraph graph = new TwoDimGraph();
        Map<String, XYNode> nodeMap = new HashMap<>();

        for (int x =0; x<input.getWidth(); x++){
            for (int y=0; y<input.getHeight(); y++){
                XYNode node = new XYNode("(" + x +"/"+ y + ")",x,y);
                graph.addNode(node);
                nodeMap.put("(" + x +"/"+ y + ")", node);
            }
        }
        for (int x =0; x<input.getWidth(); x++){
            for (int y=0; y<input.getHeight(); y++) {
                if (y < input.getHeight()-1 && !input.isVWallActive(x,y)){
                    System.out.println("(" + x +"/"+ (y+1) + ")");
                    graph.addEdge(nodeMap.get("(" + x +"/"+ y + ")"), nodeMap.get("(" + x +"/"+ (y+1) + ")"), Optional.of(1.0));
                }
                if (x < input.getWidth()-1 && !input.isHWallActive(x,y)){
                    graph.addEdge(nodeMap.get("(" + x +"/"+ y + ")"), nodeMap.get("(" + (x+1) +"/"+ y + ")"), Optional.of(1.0));
                }
            }
        }



        return graph;
    }

    public static void main(String[] args) throws ParseException {
        Maze maze = new MazeImpl(20, 20);
        maze.setHWall(0,0,true);
        MazeReader reader = new MazeReader();
        TwoDimGraph graph = reader.read(maze);
        System.out.println(graph.toString());
    }

}
