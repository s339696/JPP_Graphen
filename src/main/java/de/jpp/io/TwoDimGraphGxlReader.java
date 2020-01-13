package de.jpp.io;

import de.jpp.io.interfaces.ParseException;
import de.jpp.model.TwoDimGraph;
import de.jpp.model.XYNode;
import de.jpp.model.interfaces.Edge;
import org.jdom2.Element;

import java.util.List;
import java.util.Optional;

public class TwoDimGraphGxlReader extends GxlReaderTemplate<XYNode, Double, TwoDimGraph, String> {

    public TwoDimGraphGxlReader() {
    }


    public String getAttrValue(List<Element> elementList, String str){
        String attrValue = "";

        for (Element element: elementList){
            if (element.getAttributeValue(str).isEmpty()){
                throw new NullPointerException("AttrValue does not exist");
            } else{
            attrValue = element.getAttributeValue(str);
            }
        }

        return attrValue;
    }
    /**
     * Erzeugt einen neuen, leeren Graphen in den die neuen Objekte hinzugefügt werden können.
     * Dies muss abstrakt sein, da wir noch nicht wissen, welchen Typ von Graphen wir erzeugen. Im Falle des TwoDimGraph wird ein leerer TwoDimGraph erstellt.
     */
    @Override
    public TwoDimGraph createGraph() {
        return new TwoDimGraph();
    }

    /**
     * Liest die ID des Knoten aus der GXL-Darstellung des Knoten (übergeben als org.jdom2.Element) aus.
     * In den Beispielen des TwoDimGraph ist die ID im GXL-Element als Attribut "id" gespeichert und kann von dort ausgelesen werden.
     *
     * @param node
     * @param element
     */
    @Override
    public String readNodeId(XYNode node, Element element) {
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
    public XYNode readNode(Element element) throws ParseException {
        List<Element> attributeList = element.getChildren("attr");


        double x = 0.0;
        double y = 0.0;
        String label = "";
        int count =0;

        for ( Element e : attributeList) {
            if (e.getAttributeValue("name").equals("x")){

                if (e.getValue().isEmpty()){
                    throw new ParseException("missing x");
                } else if (e.getValue().contains(",")){
                    throw new ParseException("Unparsable x");
                } else{
                    x = Double.parseDouble(e.getValue());
                    count++;
                }
            } else if (e.getAttributeValue("name").equals("y")){
                if (e.getValue().isEmpty()){
                    throw new ParseException("missing y");
                } else{
                    y = Double.parseDouble(e.getValue());
                    count++;
                }
            } else if (e.getAttributeValue("name").equals("description")){
                if (e.getValue().isEmpty()){
                    throw new ParseException("missing label");
                } else {
                    label = e.getValue().trim();
                    count++;
                }
            } else{
                System.out.println("Data is not needed.");
            }


        }
        if (count != 3){
            throw new ParseException("Error");
        }

        XYNode node = new XYNode(label, x ,y);


        return node;
    }

    /**
     * Erzeugt die entsprechende Annotationa us dem org.jdom2.Element Objekt.
     * Im Falle des TwoDimGraph wird hier das Label des Knoten ausgelesen und zurückgegeben.
     *
     * @param element
     */
    @Override
    public Optional<Double> readAnnotation(Element element) {
        List<Element> list = element.getChildren("attr");
        Double cost =0.0;
        for ( Element attr: list) {
            if (attr.getAttributeValue("name").equals("cost")){
                cost = Double.parseDouble(attr.getValue().trim());
            }
        }

//        Optional<Double> label = Optional.of(Double.parseDouble(element.getChild("attr").getAttributeValue("cost").trim()));
        return Optional.of(cost);
    }


    public static void main(String[] args) {
        TwoDimGraph graph = new TwoDimGraph();
        String invalid = "<gxl><graph><node id=\"1\"><attr name=\"x\"><float>100.0</float></attr><attr name=\"y\"><float>100.0</float></attr><attr name=\"description\"><string>n1</string></attr></node><node id=\"2\"><attr name=\"x\"><float>200.0</float></attr><attr name=\"y\"><float>150.0</float></attr><attr name=\"description\"><string>n2</string></attr></node><node id=\"3\"><attr name=\"x\"><float>150.0</float></attr><attr name=\"y\"><float>250.0</float></attr><attr name=\"description\"><string>n3</string></attr></node><node id=\"4\"><attr name=\"x\"><float>50.0</float></attr><attr name=\"y\"><float>200.0</float></attr><attr name=\"description\"><string>n4</string></attr></node><node id=\"5\"><attr name=\"x\"><float>300.0</float></attr><attr name=\"y\"><float>150.0</float></attr><attr name=\"description\"><string>n5</string></attr></node><node id=\"6\"><attr name=\"x\"><float>350.0</float></attr><attr name=\"y\"><float>250.0</float></attr><attr name=\"description\"><string>n6</string></attr></node><node id=\"7\"><attr name=\"x\"><float>300.0</float></attr><attr name=\"y\"><float>350.0</float></attr><attr name=\"description\"><string>n7</string></attr></node><node id=\"8\"><attr name=\"x\"><float>200.0</float></attr><attr name=\"y\"><float>350.0</float></attr><attr name=\"description\"><string>n8</string></attr></node><node id=\"9\"><attr name=\"x\"><float>200.0</float></attr><attr name=\"y\"><float>100.0</float></attr><attr name=\"description\"><string>n9</string></attr></node><node id=\"10\"><attr name=\"x\"><float>100.0</float></attr><attr name=\"y\"><float>300.0</float></attr><attr name=\"description\"><string>n10</string></attr></node><node id=\"11\"><attr name=\"x\"><float>450.0</float></attr><attr name=\"y\"><float>250.0</float></attr><attr name=\"description\"><string>n11</string></attr></node><node id=\"12\"><attr name=\"x\"><float>500.0</float></attr><attr name=\"y\"><float>350.0</float></attr><attr name=\"description\"><string>n12</string></attr></node><node id=\"13\"><attr name=\"x\"><float>450.0</float></attr><attr name=\"y\"><float>450.0</float></attr><attr name=\"description\"><string>n13</string></attr></node><node id=\"14\"><attr name=\"x\"><float>350.0</float></attr><attr name=\"y\"><float>450.0</float></attr><attr name=\"description\"><string>n14</string></attr></node><node id=\"15\"><attr name=\"x\"><float>150.0</float></attr><attr name=\"y\"><float>450.0</float></attr><attr name=\"description\"><string>n15</string></attr></node><node id=\"16\"><attr name=\"x\"><float>50.0</float></attr><attr name=\"y\"><float>450.0</float></attr><attr name=\"description\"><string>n16</string></attr></node><node id=\"17\"><attr name=\"x\"><float>50.0</float></attr><attr name=\"y\"><float>550.0</float></attr><attr name=\"description\"><string>n17</string></attr></node><node id=\"18\"><attr name=\"x\"><float>150.0</float></attr><attr name=\"y\"><float>550.0</float></attr><attr name=\"description\"><string>n18</string></attr></node><edge from=\"n1\" to=\"n2\" id=\"19\"><attr name=\"cost\"><float>1.0</float></attr></edge><edge from=\"n1\" to=\"n4\" id=\"20\"><attr name=\"cost\"><float>1.0</float></attr></edge><edge from=\"n1\" to=\"n9\" id=\"21\"><attr name=\"cost\"><float>1.0</float></attr></edge><edge from=\"n4\" to=\"n3\" id=\"22\"><attr name=\"cost\"><float>1.0</float></attr></edge><edge from=\"n4\" to=\"n1\" id=\"23\"><attr name=\"cost\"><float>1.0</float></attr></edge><edge from=\"n4\" to=\"n10\" id=\"24\"><attr name=\"cost\"><float>1.0</float></attr></edge><edge from=\"n14\" to=\"n13\" id=\"25\"><attr name=\"cost\"><float>1.0</float></attr></edge><edge from=\"n14\" to=\"n7\" id=\"26\"><attr name=\"cost\"><float>1.0</float></attr></edge><edge from=\"n3\" to=\"n2\" id=\"27\"><attr name=\"cost\"><float>1.0</float></attr></edge><edge from=\"n3\" to=\"n4\" id=\"28\"><attr name=\"cost\"><float>1.0</float></attr></edge><edge from=\"n13\" to=\"n12\" id=\"29\"><attr name=\"cost\"><float>1.0</float></attr></edge><edge from=\"n13\" to=\"n14\" id=\"30\"><attr name=\"cost\"><float>1.0</float></attr></edge><edge from=\"n5\" to=\"n2\" id=\"31\"><attr name=\"cost\"><float>1.0</float></attr></edge><edge from=\"n5\" to=\"n6\" id=\"32\"><attr name=\"cost\"><float>1.0</float></attr></edge><edge from=\"n5\" to=\"n9\" id=\"33\"><attr name=\"cost\"><float>1.0</float></attr></edge><edge from=\"n8\" to=\"n7\" id=\"34\"><attr name=\"cost\"><float>1.0</float></attr></edge><edge from=\"n8\" to=\"n3\" id=\"35\"><attr name=\"cost\"><float>1.0</float></attr></edge><edge from=\"n8\" to=\"n10\" id=\"36\"><attr name=\"cost\"><float>1.0</float></attr></edge><edge from=\"n8\" to=\"n15\" id=\"37\"><attr name=\"cost\"><float>1.0</float></attr></edge><edge from=\"n15\" to=\"n16\" id=\"38\"><attr name=\"cost\"><float>1.0</float></attr></edge><edge from=\"n9\" to=\"n5\" id=\"39\"><attr name=\"cost\"><float>1.0</float></attr></edge><edge from=\"n16\" to=\"n17\" id=\"40\"><attr name=\"cost\"><float>1.0</float></attr></edge><edge from=\"n6\" to=\"n5\" id=\"41\"><attr name=\"cost\"><float>1.0</float></attr></edge><edge from=\"n6\" to=\"n7\" id=\"42\"><attr name=\"cost\"><float>1.0</float></attr></edge><edge from=\"n6\" to=\"n11\" id=\"43\"><attr name=\"cost\"><float>1.0</float></attr></edge><edge from=\"n11\" to=\"n6\" id=\"44\"><attr name=\"cost\"><float>1.0</float></attr></edge><edge from=\"n11\" to=\"n12\" id=\"45\"><attr name=\"cost\"><float>1.0</float></attr></edge><edge from=\"n18\" to=\"n15\" id=\"46\"><attr name=\"cost\"><float>1.0</float></attr></edge><edge from=\"n2\" to=\"n1\" id=\"47\"><attr name=\"cost\"><float>1.0</float></attr></edge><edge from=\"n2\" to=\"n3\" id=\"48\"><attr name=\"cost\"><float>1.0</float></attr></edge><edge from=\"n10\" to=\"n8\" id=\"49\"><attr name=\"cost\"><float>1.0</float></attr></edge><edge from=\"n17\" to=\"n18\" id=\"50\"><attr name=\"cost\"><float>1.0</float></attr></edge><edge from=\"n7\" to=\"n6\" id=\"51\"><attr name=\"cost\"><float>1.0</float></attr></edge><edge from=\"n7\" to=\"n8\" id=\"52\"><attr name=\"cost\"><float>1.0</float></attr></edge><edge from=\"n7\" to=\"n14\" id=\"53\"><attr name=\"cost\"><float>1.0</float></attr></edge><edge from=\"n12\" to=\"n11\" id=\"54\"><attr name=\"cost\"><float>1.0</float></attr></edge><edge from=\"n12\" to=\"n13\" id=\"55\"><attr name=\"cost\"><float>1.0</float></attr></edge></graph></gxl>";
        String str = "<gxl>\n" +
                "<graph id=\"id0\">\n" +
                "<node id=\"id1\">\n" +
                "<attr name=\"description\">\n" +
                "<string>n1</string>\n" +
                "</attr>\n" +
                "<attr name=\"x\">\n" +
                "<int>100</int>\n" +
                "</attr>\n" +
                "<attr name=\"y\">\n" +
                "<int>100</int>\n" +
                "</attr>\n" +
                "</node>\n" +
                "<node id=\"id2\">\n" +
                "<attr name=\"description\">\n" +
                "<string>n2</string>\n" +
                "</attr>\n" +
                "<attr name=\"x\">\n" +
                "<int>200</int>\n" +
                "</attr>\n" +
                "<attr name=\"y\">\n" +
                "<int>150</int>\n" +
                "</attr>\n" +
                "</node>\n" +
                "<node id=\"id3\">\n" +
                "<attr name=\"description\">\n" +
                "<string>n3</string>\n" +
                "</attr>\n" +
                "<attr name=\"x\">\n" +
                "<int>150</int>\n" +
                "</attr>\n" +
                "<attr name=\"y\">\n" +
                "<int>250</int>\n" +
                "</attr>\n" +
                "</node>\n" +
                "<node id=\"id4\">\n" +
                "<attr name=\"description\">\n" +
                "<string>n4</string>\n" +
                "</attr>\n" +
                "<attr name=\"x\">\n" +
                "<int>50</int>\n" +
                "</attr>\n" +
                "<attr name=\"y\">\n" +
                "<int>200</int>\n" +
                "</attr>\n" +
                "</node>\n" +
                "<node id=\"id5\">\n" +
                "<attr name=\"description\">\n" +
                "<string>n5</string>\n" +
                "</attr>\n" +
                "<attr name=\"x\">\n" +
                "<int>300</int>\n" +
                "</attr>\n" +
                "<attr name=\"y\">\n" +
                "<int>150</int>\n" +
                "</attr>\n" +
                "</node>\n" +
                "<node id=\"id6\">\n" +
                "<attr name=\"description\">\n" +
                "<string>n6</string>\n" +
                "</attr>\n" +
                "<attr name=\"x\">\n" +
                "<int>350</int>\n" +
                "</attr>\n" +
                "<attr name=\"y\">\n" +
                "<int>250</int>\n" +
                "</attr>\n" +
                "</node>\n" +
                "<node id=\"id7\">\n" +
                "<attr name=\"description\">\n" +
                "<string>n7</string>\n" +
                "</attr>\n" +
                "<attr name=\"x\">\n" +
                "<int>300</int>\n" +
                "</attr>\n" +
                "<attr name=\"y\">\n" +
                "<int>350</int>\n" +
                "</attr>\n" +
                "</node>\n" +
                "<node id=\"id8\">\n" +
                "<attr name=\"description\">\n" +
                "<string>n8</string>\n" +
                "</attr>\n" +
                "<attr name=\"x\">\n" +
                "<int>200</int>\n" +
                "</attr>\n" +
                "<attr name=\"y\">\n" +
                "<int>350</int>\n" +
                "</attr>\n" +
                "</node>\n" +
                "<node id=\"id9\">\n" +
                "<attr name=\"description\">\n" +
                "<string>n9</string>\n" +
                "</attr>\n" +
                "<attr name=\"x\">\n" +
                "<int>200</int>\n" +
                "</attr>\n" +
                "<attr name=\"y\">\n" +
                "<int>100</int>\n" +
                "</attr>\n" +
                "</node>\n" +
                "<node id=\"id10\">\n" +
                "<attr name=\"description\">\n" +
                "<string>n10</string>\n" +
                "</attr>\n" +
                "<attr name=\"x\">\n" +
                "<int>100</int>\n" +
                "</attr>\n" +
                "<attr name=\"y\">\n" +
                "<int>300</int>\n" +
                "</attr>\n" +
                "</node>\n" +
                "<node id=\"id11\">\n" +
                "<attr name=\"description\">\n" +
                "<string>n11</string>\n" +
                "</attr>\n" +
                "<attr name=\"x\">\n" +
                "<int>450</int>\n" +
                "</attr>\n" +
                "<attr name=\"y\">\n" +
                "<int>250</int>\n" +
                "</attr>\n" +
                "</node>\n" +
                "<node id=\"id12\">\n" +
                "<attr name=\"description\">\n" +
                "<string>n12</string>\n" +
                "</attr>\n" +
                "<attr name=\"x\">\n" +
                "<int>500</int>\n" +
                "</attr>\n" +
                "<attr name=\"y\">\n" +
                "<int>350</int>\n" +
                "</attr>\n" +
                "</node>\n" +
                "<node id=\"id13\">\n" +
                "<attr name=\"description\">\n" +
                "<string>n13</string>\n" +
                "</attr>\n" +
                "<attr name=\"x\">\n" +
                "<int>450</int>\n" +
                "</attr>\n" +
                "<attr name=\"y\">\n" +
                "<int>450</int>\n" +
                "</attr>\n" +
                "</node>\n" +
                "<node id=\"id14\">\n" +
                "<attr name=\"description\">\n" +
                "<string>n14</string>\n" +
                "</attr>\n" +
                "<attr name=\"x\">\n" +
                "<int>350</int>\n" +
                "</attr>\n" +
                "<attr name=\"y\">\n" +
                "<int>450</int>\n" +
                "</attr>\n" +
                "</node>\n" +
                "<node id=\"id15\">\n" +
                "<attr name=\"description\">\n" +
                "<string>n15</string>\n" +
                "</attr>\n" +
                "<attr name=\"x\">\n" +
                "<int>150</int>\n" +
                "</attr>\n" +
                "<attr name=\"y\">\n" +
                "<int>450</int>\n" +
                "</attr>\n" +
                "</node>\n" +
                "<node id=\"id16\">\n" +
                "<attr name=\"description\">\n" +
                "<string>n16</string>\n" +
                "</attr>\n" +
                "<attr name=\"x\">\n" +
                "<int>50</int>\n" +
                "</attr>\n" +
                "<attr name=\"y\">\n" +
                "<int>450</int>\n" +
                "</attr>\n" +
                "</node>\n" +
                "<node id=\"id17\">\n" +
                "<attr name=\"description\">\n" +
                "<string>n17</string>\n" +
                "</attr>\n" +
                "<attr name=\"x\">\n" +
                "<int>50</int>\n" +
                "</attr>\n" +
                "<attr name=\"y\">\n" +
                "<int>550</int>\n" +
                "</attr>\n" +
                "</node>\n" +
                "<node id=\"id18\">\n" +
                "<attr name=\"description\">\n" +
                "<string>n18</string>\n" +
                "</attr>\n" +
                "<attr name=\"x\">\n" +
                "<int>150</int>\n" +
                "</attr>\n" +
                "<attr name=\"y\">\n" +
                "<int>550</int>\n" +
                "</attr>\n" +
                "</node>\n" +
                "<edge from=\"id2\" id=\"id19\" to=\"id1\">\n" +
                "<attr name=\"description\">\n" +
                "<string>id101</string>\n" +
                "</attr>\n" +
                "<attr name=\"cost\">\n" +
                "<float>1.0</float>\n" +
                "</attr>\n" +
                "</edge>\n" +
                "<edge from=\"id1\" id=\"id20\" to=\"id2\">\n" +
                "<attr name=\"description\">\n" +
                "<string>id100</string>\n" +
                "</attr>\n" +
                "<attr name=\"cost\">\n" +
                "<float>1.0</float>\n" +
                "</attr>\n" +
                "</edge>\n" +
                "<edge from=\"id2\" id=\"id21\" to=\"id3\">\n" +
                "<attr name=\"description\">\n" +
                "<string>id102</string>\n" +
                "</attr>\n" +
                "<attr name=\"cost\">\n" +
                "<float>1.0</float>\n" +
                "</attr>\n" +
                "</edge>\n" +
                "<edge from=\"id3\" id=\"id22\" to=\"id2\">\n" +
                "<attr name=\"description\">\n" +
                "<string>id103</string>\n" +
                "</attr>\n" +
                "<attr name=\"cost\">\n" +
                "<float>1.0</float>\n" +
                "</attr>\n" +
                "</edge>\n" +
                "<edge from=\"id3\" id=\"id23\" to=\"id4\">\n" +
                "<attr name=\"description\">\n" +
                "<string>id104</string>\n" +
                "</attr>\n" +
                "<attr name=\"cost\">\n" +
                "<float>1.0</float>\n" +
                "</attr>\n" +
                "</edge>\n" +
                "<edge from=\"id4\" id=\"id24\" to=\"id3\">\n" +
                "<attr name=\"description\">\n" +
                "<string>id144</string>\n" +
                "</attr>\n" +
                "<attr name=\"cost\">\n" +
                "<float>1.0</float>\n" +
                "</attr>\n" +
                "</edge>\n" +
                "<edge from=\"id1\" id=\"id25\" to=\"id4\">\n" +
                "<attr name=\"description\">\n" +
                "<string>id105</string>\n" +
                "</attr>\n" +
                "<attr name=\"cost\">\n" +
                "<float>1.0</float>\n" +
                "</attr>\n" +
                "</edge>\n" +
                "<edge from=\"id4\" id=\"id26\" to=\"id1\">\n" +
                "<attr name=\"description\">\n" +
                "<string>id106</string>\n" +
                "</attr>\n" +
                "<attr name=\"cost\">\n" +
                "<float>1.0</float>\n" +
                "</attr>\n" +
                "</edge>\n" +
                "<edge from=\"id5\" id=\"id27\" to=\"id2\">\n" +
                "<attr name=\"description\">\n" +
                "<string>id107</string>\n" +
                "</attr>\n" +
                "<attr name=\"cost\">\n" +
                "<float>1.0</float>\n" +
                "</attr>\n" +
                "</edge>\n" +
                "<edge from=\"id5\" id=\"id28\" to=\"id6\">\n" +
                "<attr name=\"description\">\n" +
                "<string>id108</string>\n" +
                "</attr>\n" +
                "<attr name=\"cost\">\n" +
                "<float>1.0</float>\n" +
                "</attr>\n" +
                "</edge>\n" +
                "<edge from=\"id6\" id=\"id29\" to=\"id5\">\n" +
                "<attr name=\"description\">\n" +
                "<string>id109</string>\n" +
                "</attr>\n" +
                "<attr name=\"cost\">\n" +
                "<float>1.0</float>\n" +
                "</attr>\n" +
                "</edge>\n" +
                "<edge from=\"id6\" id=\"id30\" to=\"id7\">\n" +
                "<attr name=\"description\">\n" +
                "<string>id110</string>\n" +
                "</attr>\n" +
                "<attr name=\"cost\">\n" +
                "<float>1.0</float>\n" +
                "</attr>\n" +
                "</edge>\n" +
                "<edge from=\"id7\" id=\"id31\" to=\"id6\">\n" +
                "<attr name=\"description\">\n" +
                "<string>id111</string>\n" +
                "</attr>\n" +
                "<attr name=\"cost\">\n" +
                "<float>1.0</float>\n" +
                "</attr>\n" +
                "</edge>\n" +
                "<edge from=\"id7\" id=\"id32\" to=\"id8\">\n" +
                "<attr name=\"description\">\n" +
                "<string>id112</string>\n" +
                "</attr>\n" +
                "<attr name=\"cost\">\n" +
                "<float>1.0</float>\n" +
                "</attr>\n" +
                "</edge>\n" +
                "<edge from=\"id8\" id=\"id33\" to=\"id7\">\n" +
                "<attr name=\"description\">\n" +
                "<string>id113</string>\n" +
                "</attr>\n" +
                "<attr name=\"cost\">\n" +
                "<float>1.0</float>\n" +
                "</attr>\n" +
                "</edge>\n" +
                "<edge from=\"id8\" id=\"id34\" to=\"id3\">\n" +
                "<attr name=\"description\">\n" +
                "<string>id114</string>\n" +
                "</attr>\n" +
                "<attr name=\"cost\">\n" +
                "<float>1.0</float>\n" +
                "</attr>\n" +
                "</edge>\n" +
                "<edge from=\"id6\" id=\"id35\" to=\"id11\">\n" +
                "<attr name=\"description\">\n" +
                "<string>id115</string>\n" +
                "</attr>\n" +
                "<attr name=\"cost\">\n" +
                "<float>1.0</float>\n" +
                "</attr>\n" +
                "</edge>\n" +
                "<edge from=\"id11\" id=\"id36\" to=\"id6\">\n" +
                "<attr name=\"description\">\n" +
                "<string>id116</string>\n" +
                "</attr>\n" +
                "<attr name=\"cost\">\n" +
                "<float>1.0</float>\n" +
                "</attr>\n" +
                "</edge>\n" +
                "<edge from=\"id11\" id=\"id37\" to=\"id12\">\n" +
                "<attr name=\"description\">\n" +
                "<string>id117</string>\n" +
                "</attr>\n" +
                "<attr name=\"cost\">\n" +
                "<float>1.0</float>\n" +
                "</attr>\n" +
                "</edge>\n" +
                "<edge from=\"id12\" id=\"id38\" to=\"id11\">\n" +
                "<attr name=\"description\">\n" +
                "<string>id118</string>\n" +
                "</attr>\n" +
                "<attr name=\"cost\">\n" +
                "<float>1.0</float>\n" +
                "</attr>\n" +
                "</edge>\n" +
                "<edge from=\"id12\" id=\"id39\" to=\"id13\">\n" +
                "<attr name=\"description\">\n" +
                "<string>id119</string>\n" +
                "</attr>\n" +
                "<attr name=\"cost\">\n" +
                "<float>1.0</float>\n" +
                "</attr>\n" +
                "</edge>\n" +
                "<edge from=\"id13\" id=\"id40\" to=\"id12\">\n" +
                "<attr name=\"description\">\n" +
                "<string>id120</string>\n" +
                "</attr>\n" +
                "<attr name=\"cost\">\n" +
                "<float>1.0</float>\n" +
                "</attr>\n" +
                "</edge>\n" +
                "<edge from=\"id13\" id=\"id41\" to=\"id14\">\n" +
                "<attr name=\"description\">\n" +
                "<string>id121</string>\n" +
                "</attr>\n" +
                "<attr name=\"cost\">\n" +
                "<float>1.0</float>\n" +
                "</attr>\n" +
                "</edge>\n" +
                "<edge from=\"id14\" id=\"id42\" to=\"id13\">\n" +
                "<attr name=\"description\">\n" +
                "<string>id122</string>\n" +
                "</attr>\n" +
                "<attr name=\"cost\">\n" +
                "<float>1.0</float>\n" +
                "</attr>\n" +
                "</edge>\n" +
                "<edge from=\"id14\" id=\"id43\" to=\"id7\">\n" +
                "<attr name=\"description\">\n" +
                "<string>id123</string>\n" +
                "</attr>\n" +
                "<attr name=\"cost\">\n" +
                "<float>1.0</float>\n" +
                "</attr>\n" +
                "</edge>\n" +
                "<edge from=\"id7\" id=\"id44\" to=\"id14\">\n" +
                "<attr name=\"description\">\n" +
                "<string>id124</string>\n" +
                "</attr>\n" +
                "<attr name=\"cost\">\n" +
                "<float>1.0</float>\n" +
                "</attr>\n" +
                "</edge>\n" +
                "<edge from=\"id1\" id=\"id45\" to=\"id9\">\n" +
                "<attr name=\"description\">\n" +
                "<string>id125</string>\n" +
                "</attr>\n" +
                "<attr name=\"cost\">\n" +
                "<float>1.0</float>\n" +
                "</attr>\n" +
                "</edge>\n" +
                "<edge from=\"id9\" id=\"id46\" to=\"id5\">\n" +
                "<attr name=\"description\">\n" +
                "<string>id126</string>\n" +
                "</attr>\n" +
                "<attr name=\"cost\">\n" +
                "<float>1.0</float>\n" +
                "</attr>\n" +
                "</edge>\n" +
                "<edge from=\"id5\" id=\"id47\" to=\"id9\">\n" +
                "<attr name=\"description\">\n" +
                "<string>id136</string>\n" +
                "</attr>\n" +
                "<attr name=\"cost\">\n" +
                "<float>1.0</float>\n" +
                "</attr>\n" +
                "</edge>\n" +
                "<edge from=\"id10\" id=\"id48\" to=\"id8\">\n" +
                "<attr name=\"description\">\n" +
                "<string>id127</string>\n" +
                "</attr>\n" +
                "<attr name=\"cost\">\n" +
                "<float>1.0</float>\n" +
                "</attr>\n" +
                "</edge>\n" +
                "<edge from=\"id8\" id=\"id49\" to=\"id10\">\n" +
                "<attr name=\"description\">\n" +
                "<string>id147</string>\n" +
                "</attr>\n" +
                "<attr name=\"cost\">\n" +
                "<float>1.0</float>\n" +
                "</attr>\n" +
                "</edge>\n" +
                "<edge from=\"id4\" id=\"id50\" to=\"id10\">\n" +
                "<attr name=\"description\">\n" +
                "<string>id158</string>\n" +
                "</attr>\n" +
                "<attr name=\"cost\">\n" +
                "<float>1.0</float>\n" +
                "</attr>\n" +
                "</edge>\n" +
                "<edge from=\"id8\" id=\"id51\" to=\"id15\">\n" +
                "<attr name=\"description\">\n" +
                "<string>id129</string>\n" +
                "</attr>\n" +
                "<attr name=\"cost\">\n" +
                "<float>1.0</float>\n" +
                "</attr>\n" +
                "</edge>\n" +
                "<edge from=\"id15\" id=\"id52\" to=\"id16\">\n" +
                "<attr name=\"description\">\n" +
                "<string>id130</string>\n" +
                "</attr>\n" +
                "<attr name=\"cost\">\n" +
                "<float>1.0</float>\n" +
                "</attr>\n" +
                "</edge>\n" +
                "<edge from=\"id16\" id=\"id53\" to=\"id17\">\n" +
                "<attr name=\"description\">\n" +
                "<string>id131</string>\n" +
                "</attr>\n" +
                "<attr name=\"cost\">\n" +
                "<float>1.0</float>\n" +
                "</attr>\n" +
                "</edge>\n" +
                "<edge from=\"id17\" id=\"id54\" to=\"id18\">\n" +
                "<attr name=\"description\">\n" +
                "<string>id132</string>\n" +
                "</attr>\n" +
                "<attr name=\"cost\">\n" +
                "<float>1.0</float>\n" +
                "</attr>\n" +
                "</edge>\n" +
                "<edge from=\"id18\" id=\"id55\" to=\"id15\">\n" +
                "<attr name=\"description\">\n" +
                "<string>id133</string>\n" +
                "</attr>\n" +
                "<attr name=\"cost\">\n" +
                "<float>1.0</float>\n" +
                "</attr>\n" +
                "</edge>\n" +
                "</graph>\n" +
                "</gxl>";
        TwoDimGraphGxlReader reader = new TwoDimGraphGxlReader();
        TwoDimGraphGxlWriter writer = new TwoDimGraphGxlWriter();
        try {
            TwoDimGraph graph1 = reader.read(str    );

            System.out.println(graph1.getNodes().size());
            System.out.println(graph1.getEdges().size());
            for ( Edge<XYNode, Double> edge: graph1.getEdges()) {
                System.out.println(edge.toString());
            }

            String string = writer.write(graph1);
            System.out.println(string);

            TwoDimGraph graph2 = reader.read(string);


        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
