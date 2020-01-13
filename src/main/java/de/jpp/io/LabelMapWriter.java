package de.jpp.io;

import de.jpp.model.LabelMapGraph;
import de.jpp.model.interfaces.Edge;
import org.jdom2.Element;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class LabelMapWriter extends GxlWriterTemplate<String, Map<String, String>, LabelMapGraph, String> {

    Map<Object, String> idMap = new HashMap<>();
    int maxId;

    /**
     * Erstellt aus dem übergebenen Knoten ein org.jdom2.Element-Objekt.
     *
     * @param node
     */
    @Override
    public Element writeNode(String node) {
        Element nodeChild = new Element("node");
        nodeChild.addContent(createAttribute("description", "string", node));
        return nodeChild;
    }

    /**
     * Erstellt aus der übergebenen Kante ein org.jdom2.Element-Objekt.
     *
     * @param edge
     */
    @Override
    public Element writeEdge(Edge<String, Map<String, String>> edge) {
        Element nodeChild = new Element("edge");
        nodeChild.setAttribute("from", assignedId(edge.getStart()));
        nodeChild.setAttribute("to", assignedId(edge.getDestination()));
        if (edge.getAnnotation().isPresent()){
            Map<String, String> attrMap = edge.getAnnotation().get();
            for ( Map.Entry<String,String> entry : attrMap.entrySet() ) {
                nodeChild.addContent(createAttribute(entry.getKey(), "string", entry.getValue()));

        }

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
    public String calculateId(String node) {
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
    public String calculateId(Edge<String, Map<String, String>> edge) {
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
}
