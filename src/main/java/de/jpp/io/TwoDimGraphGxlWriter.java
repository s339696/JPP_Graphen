package de.jpp.io;

import de.jpp.model.TwoDimGraph;
import de.jpp.model.XYNode;
import de.jpp.model.interfaces.Edge;
import org.jdom2.Content;
import org.jdom2.Element;

import java.util.HashMap;
import java.util.Map;

public class TwoDimGraphGxlWriter extends GxlWriterTemplate<XYNode, Double, TwoDimGraph, String> {

    Map<Object, String> idMap = new HashMap<>();
    int maxId;

    /**
     * Erstellt aus dem übergebenen Knoten ein org.jdom2.Element-Objekt.
     *
     * @param node
     */
    @Override
    public Element writeNode(XYNode node) {
        Element nodeChild = new Element("node");
        nodeChild.addContent(createAttribute("x", "float", String.valueOf(node.getX())));
        nodeChild.addContent(createAttribute("y", "float", String.valueOf(node.getY())));
        nodeChild.addContent(createAttribute("description","string", node.getLabel()));

        return nodeChild;
    }

    /**
     * Erstellt aus der übergebenen Kante ein org.jdom2.Element-Objekt.
     *
     * @param edge
     */
    @Override
    public Element writeEdge(Edge<XYNode, Double> edge) {

        Element nodeChild = new Element("edge");
        nodeChild.setAttribute("from", assignedId(edge.getStart()));
        nodeChild.setAttribute("to", assignedId(edge.getDestination()));
        Element attr = new Element("attr").setAttribute("name", "cost");
        Element typElement = new Element("float");
        if (edge.getAnnotation().isPresent()){
            typElement.setText(String.valueOf(edge.getAnnotation().get()));
            attr.addContent(typElement);
            nodeChild.addContent(attr);
        }
        return nodeChild;
    }

    /**
     * Weis dem übergebenen Knoten / der übergebenen Kante eine einzigartige ID zu. Das ist notwendig, da die Kanten nur die ID der Knotenobjekte als Start- bzw.
     * Endpunkt speichern sollen. Im einfachsten Fall fangen sie mit der ID 1 an und weisen den Objekten nacheinander höhere Zahlen als IDs zu.
     *
     * @param node
     */
    @Override
    public String calculateId(XYNode node) {
        maxId++;
        String id = String.valueOf(maxId);
        idMap.put(node, id);
        return id;
    }

    /**
     * Weis dem übergebenen Knoten / der übergebenen Kante eine einzigartige ID zu. Das ist notwendig, da die Kanten nur die ID der Knotenobjekte als Start- bzw.
     * Endpunkt speichern sollen. Im einfachsten Fall fangen sie mit der ID 1 an und weisen den Objekten nacheinander höhere Zahlen als IDs zu.
     *
     * @param edge
     */
    @Override
    public String calculateId(Edge<XYNode, Double> edge) {
        maxId++;
        String id = String.valueOf(maxId);
        idMap.put(edge, id);
        return id;
    }

    public String assignedId(Object object){
        String id = idMap.get(object);
        return id;
    }

    public Element createAttribute(String name, String typ, String value){
        Element element = new Element("attr");
        element.setAttribute("name", name);
        Element typElement = new Element(typ);
        typElement.setText(value);
        element.addContent(typElement);

    return element;

    }

    public static void main(String[] args) {



    }
}
