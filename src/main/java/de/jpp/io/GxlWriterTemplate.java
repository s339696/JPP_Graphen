package de.jpp.io;

import de.jpp.io.interfaces.GraphWriter;
import de.jpp.model.interfaces.Edge;
import de.jpp.model.interfaces.Graph;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.XMLOutputter;

import java.util.Collection;

public abstract class GxlWriterTemplate<N, A, G extends Graph<N,A>, F> implements GraphWriter<N, A, G, String> {


    public GxlWriterTemplate() {
    }

    public String write(G graph) {

        Document document = new Document();
        Element root = new Element("gxl");


        Element graphChild = new Element("graph");


        Collection<Edge<N,A>> edgeCollection = graph.getEdges();
        Collection<N> nodeCollection = graph.getNodes();

        for(N node : nodeCollection){
            Element nodeChild = writeNode(node);
            nodeChild.setAttribute("id", calculateId(node));
            graphChild.addContent(nodeChild);
        }

        for ( Edge<N,A> edge : edgeCollection) {
            Element edgeChild = writeEdge(edge);
            edgeChild.setAttribute("id", calculateId(edge));
            graphChild.addContent(edgeChild);
        }

        root.addContent(graphChild);
        document.setContent(root);

        return new XMLOutputter().outputString(document);
    }

    /**
     *Erstellt aus dem übergebenen Knoten ein org.jdom2.Element-Objekt.
     */
    public abstract Element writeNode(N node);

    /**
     *Erstellt aus der übergebenen Kante ein org.jdom2.Element-Objekt.
     */
    public abstract Element writeEdge(Edge<N,A> edge);

    /**
     * Weis dem übergebenen Knoten / der übergebenen Kante eine einzigartige ID zu. Das ist notwendig, da die Kanten nur die ID der Knotenobjekte als Start- bzw.
     *  Endpunkt speichern sollen. Im einfachsten Fall fangen sie mit der ID 1 an und weisen den Objekten nacheinander höhere Zahlen als IDs zu.
     */
    public abstract String calculateId(N node);

    /**
     * Weis dem übergebenen Knoten / der übergebenen Kante eine einzigartige ID zu. Das ist notwendig, da die Kanten nur die ID der Knotenobjekte als Start- bzw.
     * Endpunkt speichern sollen. Im einfachsten Fall fangen sie mit der ID 1 an und weisen den Objekten nacheinander höhere Zahlen als IDs zu.
     */
    public abstract  String calculateId(Edge<N,A> edge);



}
