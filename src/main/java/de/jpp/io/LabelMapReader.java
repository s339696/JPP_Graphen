package de.jpp.io;

import de.jpp.io.interfaces.ParseException;
import de.jpp.model.LabelMapGraph;
import org.jdom2.Element;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class LabelMapReader extends GxlReaderTemplate<String, Map<String, String>, LabelMapGraph, String> {

    /**
     * Erzeugt einen neuen, leeren Graphen in den die neuen Objekte hinzugefügt werden können.
     * Dies muss abstrakt sein, da wir noch nicht wissen, welchen Typ von Graphen wir erzeugen. Im Falle des TwoDimGraph wird ein leerer TwoDimGraph erstellt.
     */
    @Override
    public LabelMapGraph createGraph() {
        return new LabelMapGraph();
    }

    /**
     * Liest die ID des Knoten aus der GXL-Darstellung des Knoten (übergeben als org.jdom2.Element) aus.
     * In den Beispielen des TwoDimGraph ist die ID im GXL-Element als Attribut "id" gespeichert und kann von dort ausgelesen werden.
     *
     * @param node
     * @param element
     */
    @Override
    public String readNodeId(String node, Element element) {
        String nodeId = element.getAttributeValue("id");
        return nodeId;
    }

    /**
     * Erzeugt das entsprechende Knotenobjekt aus dem org.jdom2.Element Objekt.
     * Im Falle des TwoDimGraph werden hier x- und y-Koordinate des Knoten ausgelesen und das entsprechende Objekt damit erstellt.
     *
     * @param element
     */
    @Override
    public String readNode(Element element) throws ParseException {
        if (element.getChildren().isEmpty()){
            throw new ParseException("Node needs a description");
        }
        String node = element.getChild("attr").getValue().trim();

        return node;
    }

    /**
     * Erzeugt die entsprechende Annotationa us dem org.jdom2.Element Objekt.
     * Im Falle des TwoDimGraph wird hier das Label des Knoten ausgelesen und zurückgegeben.
     *
     * @param element
     */
    @Override
    public Optional<Map<String, String>> readAnnotation(Element element) {
        Map<String, String> attrMap = new HashMap<>();
        List<Element> attr = element.getChildren("attr");
        if (attr.isEmpty()){
            return Optional.empty();
        } else{

            for ( Element e: attr) {
                attrMap.put(e.getAttributeValue("name"), e.getValue().trim());
            }

            return Optional.of(attrMap);
        }
    }

    public static void main(String[] args) {

        LabelMapReader reader = new LabelMapReader();
        LabelMapWriter writer = new LabelMapWriter();
        String str = "<gxl>\n" +
                " <graph id=\"id1\">\n" +
                " <node id=\"id1\"> \n" +
                " <attr name=\"description\">\n" +
                " <string>n1</string>\n" +
                " </attr>\n" +
                " <attr name=\"x\">\n" +
                " <int>0.00</int>\n" +
                " </attr>\n" +
                " <attr name=\"y\">\n" +
                " <int>0</int>\n" +
                " </attr>\n" +
                " <attr name=\"foo\">\n" +
                " <String>bar</String>\n" +
                " </attr>\n" +
                " <attr name = \"bar\">\n" +
                " <float>3.0</float>\n" +
                " </attr>\n" +
                " </node>\n" +
                " <node id=\"id2\">\n" +
                " </node>\n" +
                " <edge from=\"id1\" id=\"id3\" to=\"id3\">\n" +
                " </edge>\n" +
                " </graph>\n" +
                " </gxl>";
        try {
            LabelMapGraph graph = reader.read(str);

            for ( String node: graph.getNodes()) {
                System.out.println(node);
            }

//            String string = writer.write(graph);
//            System.out.println(string);




        } catch (ParseException e) {
            e.printStackTrace();
        }


    }
}
