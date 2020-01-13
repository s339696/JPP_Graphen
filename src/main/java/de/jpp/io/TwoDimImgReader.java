package de.jpp.io;

import de.jpp.io.interfaces.GraphReader;
import de.jpp.io.interfaces.ParseException;
import de.jpp.model.TwoDimGraph;
import de.jpp.model.XYNode;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TwoDimImgReader implements GraphReader<XYNode, Double, TwoDimGraph, BufferedImage> {
    /**
     * Creates a graph from the specified input source
     *
     * @param input the input source
     * @return a graph from the input source
     * @throws ParseException if the input cannot be parsed correctly
     */
    @Override
    public TwoDimGraph read(BufferedImage input) throws ParseException {
       TwoDimGraph graph = new TwoDimGraph();

        for (int i = 0; i < input.getHeight(); i++){
            for (int j = 0; j < input.getWidth(); j++){

                int color = input.getRGB(j, i);

                int red = (color >> 16) & 0xFF;
                int green = (color >> 8) & 0xFF;
                int blue = (color) & 0xFF;

                float[] hsb = Color.RGBtoHSB( red, green, blue, null);

                if (hsb[2] > 0.5){
                    XYNode node = new XYNode("(" + j + "/" + i + ")", j, i);
                    graph.addNode(node);
                }


            }
        }

        ArrayList<XYNode> nodeList = (ArrayList<XYNode>) graph.getNodes();

        for (XYNode node : nodeList){
            for (XYNode other: nodeList){
                if (node.isNeighbour(other)){
                    graph.addEdge(node, other, Optional.of(1.0));
                }


            }


        }
    return graph;
    }
}
