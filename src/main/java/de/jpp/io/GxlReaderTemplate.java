package de.jpp.io;
 import de.jpp.io.interfaces.GraphReader;
 import de.jpp.io.interfaces.ParseException;
 import de.jpp.model.interfaces.Graph;

 import org.jdom2.*;
 import org.jdom2.filter.ElementFilter;
 import org.jdom2.input.SAXBuilder;

 import java.io.IOException;
 import java.io.StringReader;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;
 import java.util.Optional;

public abstract class GxlReaderTemplate<N, A, G extends Graph<N,A>, F> implements GraphReader<N, A, G, String> {

    public GxlReaderTemplate() {
    }

    /**
     *
     */
    public G read(String input) throws ParseException {
        G graph = createGraph();

        if (!input.contains("/gxl")){
            throw new ParseException("Invalid Format");
        }

        Map<String, N> map = new HashMap<>();
        SAXBuilder builder = new SAXBuilder();
        try {
            Document doc = builder.build(new StringReader(input));
            Element root = doc.getRootElement();

            Element graphElement = root.getChild("graph");

//            if (graphElement == null){
//                throw new ParseException("Empty gxl");
//            }
            List<Element> edgeList = graphElement.getChildren("edge");
            List<Element> nodeList = graphElement.getChildren("node");

            for ( Element e: nodeList) {
                N node = readNode(e);
                String nodeId = readNodeId(node, e);

                graph.addNode(node);
                map.put(nodeId, node);
            }

            for ( Element e: edgeList) {
                addEdge(graph, e, map);
            }

        } catch (JDOMException e) {
            throw new  ParseException("Invalid Format");
        } catch (IOException e) {
            e.printStackTrace();
        }


        return graph;
    }

    /**
     *
     */
    private void addEdge(G graph, Element element, Map<String, N> map){

        String startNode = element.getAttribute("from").getValue();
        String destNode = element.getAttribute("to").getValue();


        N start = map.get(startNode);
        N dest = map.get(destNode);
        Optional<A> annotation = readAnnotation(element);

        graph.addEdge(start, dest, annotation);


    }

    /**
     *Erzeugt einen neuen, leeren Graphen in den die neuen Objekte hinzugefügt werden können.
     *  Dies muss abstrakt sein, da wir noch nicht wissen, welchen Typ von Graphen wir erzeugen. Im Falle des TwoDimGraph wird ein leerer TwoDimGraph erstellt.
     */
    public abstract G createGraph();

    /**
     * Liest die ID des Knoten aus der GXL-Darstellung des Knoten (übergeben als org.jdom2.Element) aus.
     * In den Beispielen des TwoDimGraph ist die ID im GXL-Element als Attribut "id" gespeichert und kann von dort ausgelesen werden.
     */
    public  abstract String readNodeId(N node, Element element);

    /**
     *Erzeugt das entsprechende Knotenobjekt aus dem org.jdom2.Element Objekt.
     *  Im Falle des TwoDimGraph werden hier x- und y-Koordinate des Knoten ausgelesen und das entsprechende Objekt damit erstellt.
     */
    public  abstract N readNode(Element element) throws ParseException;

    /**
     *Erzeugt die entsprechende Annotationa us dem org.jdom2.Element Objekt.
     * Im Falle des TwoDimGraph wird hier das Label des Knoten ausgelesen und zurückgegeben.
     */
    public abstract Optional<A> readAnnotation(Element element);

    public static void main(String[] args) {
        SAXBuilder builder = new SAXBuilder();
        GxlReaderTemplate template = null;
        try {
            Document doc = builder.build("C:\\Users\\Bastian\\Desktop\\basti\\Java-Aufgaben\\JPP_Graphen\\TestFiles\\gxl\\valid\\sample.gxl");
            Element root = doc.getRootElement();
            System.out.println(root);
            Element graph = root.getChild("graph");
            ElementFilter filter = new org.jdom2.filter.ElementFilter("node");
            for(Element c : graph.getDescendants(filter)) {
                List<Attribute> attributeList = c.getAttributes();
                for ( Attribute attribute :attributeList) {
                    System.out.println(attribute.getName());
                }

//                System.out.println(c.getAttributeValue("id"));
//                System.out.println(c.getAttribute("cost").getValue());


            }



        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
