package de.jpp.io;


import de.jpp.io.interfaces.GraphReader;
import de.jpp.io.interfaces.GraphWriter;
import de.jpp.model.TwoDimGraph;
import de.jpp.model.XYNode;
import de.jpp.model.interfaces.Edge;
import de.jpp.model.interfaces.Graph;

import de.jpp.io.interfaces.ParseException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TwoDimGraphDotIO<N,A, G extends Graph<N,A>, F> implements GraphWriter<XYNode, Double, TwoDimGraph, String>, GraphReader<XYNode, Double, TwoDimGraph, String> {

    public TwoDimGraphDotIO() {
    }

    /**
     * Creates a graph from the specified input source
     *
     * @param input the input source
     * @return a graph from the input source
     * @throws ParseException if the input cannot be parsed correctly
     */
    @Override
    public TwoDimGraph read(String input) throws ParseException {
        // regex für invalider aufbau der datei einfügen
        TwoDimGraph graph = new TwoDimGraph();
        Map<Integer, XYNode> nodeMap = new HashMap<>();
        input = input.substring(input.indexOf("{")+1, input.lastIndexOf("}")+1);

        String[] graphs = input.split("\n");
        for ( String str : graphs) {
            parseLine(graph, nodeMap, str);
        }
        if (graph.getEdges().isEmpty() && graph.getNodes().isEmpty()){
            throw new ParseException("Input is Empty");
        } else{

        return graph;
        }
    }

    /**
     * Creates the output from the specified graph
     *
     * @param graph the graph
     * @return the output from the specified graph
     */
    @Override
    public String write(TwoDimGraph graph) {
        Map <XYNode,Integer > keyValueMap = new HashMap<>();
        int count = 1;
        for ( XYNode node:graph.getNodes()) {
            keyValueMap.put(node, count++);
        }
        String input = "";
        input += "digraph {\n";
//        int count =0;
        for ( XYNode node: graph.getNodes()) {
            input += "\t" + keyValueMap.get(node) +"  [x="+ node.getX() + " y=" + node.getY() + " label=" + node.getLabel() +"]\n";
        }
        input += "\n";
        for (Edge<XYNode, Double> edge : graph.getEdges()){
            input += "\t" + keyValueMap.get(edge.getStart()) + " -> " + keyValueMap.get(edge.getDestination()) + "[dist=1.0]\n";
        }

        input += "}";

        return input;
    }

    /**
     * Fügt das Element der Zeile zum Graphen hinzu
     */

    public void parseLine(TwoDimGraph graph, Map<Integer, XYNode> map, String str) throws ParseException {
        if(str.matches("^\\s*\\d*\\s*\\[(?=.*label=(?:\".*\"|\\w|\\())(?=.*x=\\d)(?=.*\\sy\\s*\\=\\d).*\\]\\s*$")){
            try {
                parseNode(graph, map, str);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if (str.matches("^\\s*\\d*\\s\\-\\>\\s\\d*\\s*\\[\\s*dist\\=\\d\\.\\d\\s*\\]\\s*$")){
            try {
                parseEdge(graph, map, str);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if (!str.matches("\\s*")){

        }
    }

    /**
     * Fügt das Element der Zeile zum Graphen hinzu, falls es sich um eine Zeile handelt, die einen Knoten definiert
     */
    public void parseNode(TwoDimGraph graph, Map<Integer, XYNode> map, String str) throws ParseException {
        Map<String, String> attributes = parseAttribute(str);
        String label = attributes.get("label");
        double x = Double.parseDouble(attributes.get("x"));
        double y = Double.parseDouble(attributes.get("y"));


        String[] substring = str.split("\\t | \\s*");
        int mapKey = Integer.parseInt(substring[0].trim());

        XYNode node;
        if (label.matches("\"")){
            node =  new XYNode(null,x, y);
        }else {
             node = new XYNode(label, x, y);
        }

        map.put(mapKey, node);
        graph.addNode(node);

    }


    /**
     * Fügt das Element der Zeile zum Graphen hinzu, falls es sich um eine Zeile handelt, die eine Kante definiert
     */

    public void parseEdge(TwoDimGraph graph, Map<Integer, XYNode> map, String str) throws ParseException {
        String pair = str.substring(1, str.indexOf("["));
        String dist = str.substring(str.indexOf("[") +1 ,str.lastIndexOf("]"));

        String[] edgeData = pair.split("->");
        String [] distData = dist.split("=");

        XYNode start = map.get(Integer.parseInt(edgeData[0].trim()));
        XYNode dest = map.get(Integer.parseInt(edgeData[1].trim()));
        Double annotation = Double.valueOf(distData[1].trim());
        if (annotation == 0.0){
            throw new ParseException("Gewicht is null");
        }
        graph.addEdge(start, dest, Optional.of(annotation));

//        graph.addEuclidianEdge(start, dest);
    }

    /**
     * FLiest alle key=value-Werte die in [ ] der übergebenen Zeile ein und gibt sie als Map zurück.
     */
    public Map<String, String> parseAttribute(String str) throws ParseException {
        Map<String, String> attributes = new HashMap<>();
        str = str.substring(str.indexOf("[") + 1 , str.lastIndexOf("]"));
        String[] substring = str.split("\\s+", 3);

        for ( String string: substring) {
            if (string.isEmpty()){
                continue;
            }
            String[] keyValuePair = string.split("=");
            if (keyValuePair[1].contains("\"")){
                String labelpair = str.substring(str.indexOf("\"")+1, str.lastIndexOf("\""));
                attributes.put(keyValuePair[0], labelpair);
            }else {
                if (keyValuePair[1].isEmpty()){
                    throw new ParseException();
                }
                attributes.put(keyValuePair[0], keyValuePair[1]);
            }
        }


        return attributes;
    }



    public static void main(String[] args) {
        TwoDimGraphDotIO graph = new TwoDimGraphDotIO();

        TwoDimImgReader imgReader = new TwoDimImgReader();
        TwoDimGraphDotIO readWrite = new TwoDimGraphDotIO();
        String annoying = "digraph{\t1 [x=0 y=0 label=\"annoying      1 -> 7  1 [x===6][3427433361727079]}\\//\"]}";
        String lol = "digraph{\n" +
                "\t1 [x=0 y=1 label=\"annoying      1 -> 7  1 [x===6][197]}\\//]\n" +
                "}";
        String str = "digraph{\n" +
                "\t1    [label=\"\" x=50.0 y=450.0]\n" +
                "\t2 [label=n7 x=300.0 y=350.0  ]\n" +
                "\t3 [label=n1 x=100.0 y=100.0]\n" +
                "\t5 [label=n4 x=50.0 y=200.0]\n" +
                "\t4 [label=n11 x=450.0 y=250.0]\n" +
                "\t6 [label=n8 x=200.0 y=350.0]\n" +
                "\t7 [label=n3 x=150.0 y=250.0]\n" +
                "\t8 [label=n10 x=100.0 y=300.0]\n" +
                "\t9 [label=n15 x=150.0 y=450.0]\n" +
                "\t10 [label=n18 x=150.0 y=550.0]\n" +
                "\t11 [label=n6 x=350.0 y=250.0]\n" +
                "\t12  [label=n13      x=450.0 y=450.0]\n" +
                "\t13 [x=350.0 y=450.0     label=n14]\n" +
                "\t14 [label=n12 x=500.0 y=350.0]\n" +
                "\t15 [label=n9 x=200.0 y=100.0]\n" +
                "\t16 [label=n17 y=550.0 x=50.0]\n" +
                "\t17 [label=n2 x=200.0 y=150.0]\n" +
                "\t18 [label=n5 x=300.0 y=150.0]\n" +
                "\n" +
                "\t1 -> 16 [ dist=1.0]\n" +
                "\t2 -> 11 [dist=1.0 ]\n" +
                "\t2 -> 6 [dist=1.0]\n" +
                "\t2 -> 13 [dist=1.0 ]\n" +
                "\t3 -> 17 [dist=1.0]\n" +
                "\t3 -> 5 [dist=1.0]\t\n" +
                "\t3 -> 15 [dist=1.0]\n" +
                "\t4 -> 11 [dist=1.0]\n" +
                "\t4 -> 14 [dist=1.0]\n" +
                "\t5 -> 7 [dist=1.0]\n" +
                "\t5 -> 3 [dist=1.0]\n" +
                "\t5 -> 8 [dist=1.0]\n" +
                "\t6 -> 2 [dist=1.0]\n" +
                "\t6 -> 7 [dist=1.0]\n" +
                "\t6 -> 8 [dist=1.0]\n" +
                "\t6 -> 9 [dist=1.0]\n" +
                "\t7 -> 17 [dist=1.0]\n" +
                "\t7 -> 5 [dist=1.0]\n" +
                "\t8 -> 6 [dist=1.0]\n" +
                "\t9 -> 1 [dist=1.0]\n" +
                "\t10 -> 9 [dist=1.0]\n" +
                "\t11 -> 18 [dist=1.0]\n" +
                "\t11 -> 2 [dist=1.0]\n" +
                "\t18 -> 15 [dist=1.0]\n" +
                "\t11 -> 4 [dist=1.0]\n" +
                "\t12 -> 14 [dist=1.0]\n" +
                "\t12 -> 13 [dist=1.0]\n" +
                "\t13 -> 12 [dist=1.0]\n" +
                "\t13 -> 2 [dist=1.0]\n" +
                "\t14 -> 4 [dist=1.0]\t\n" +
                "\t14 -> 12 [dist=1.0]\n" +
                "\t15 -> 18 [dist=1.0]\n" +
                "\t16 -> 10 [dist=1.0]\n" +
                "\t17 -> 3 [dist=1.0]\n" +
                "\t17 -> 7 [dist=1.0]\n" +
                "\t18 -> 17 [dist=1.0]\n" +
                "\t18 -> 11 [dist=1.0]\n" +
                "}";
        String string = "";

        try {
            BufferedImage img = ImageIO.read( new File("C:\\Users\\Bastian\\Desktop\\basti\\Java-Aufgaben\\JPP_Graphen\\TestFiles\\img\\valid\\Maze.png"));
            TwoDimGraph graph2 = imgReader.read(img);
            String graphString = readWrite.write(graph2);
            TwoDimGraph graph3 = readWrite.read(graphString);

            System.out.println(graphString);

//            TwoDimGraph graph1 = graph.read(lol);
//            System.out.println(graph1.getNodes().size());
//            for (XYNode node: graph1.getNodes()){
//                System.out.println(node.getLabel());
//            }
//            System.out.println(graph1.getEdges().size());
//            System.out.println(graph1.getEdges().size());
//             string = graph.write(graph1);
//            System.out.println(string);





        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(string);


    }

}
