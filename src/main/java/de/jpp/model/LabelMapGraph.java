package de.jpp.model;

import de.jpp.model.interfaces.Graph;

import java.util.Map;

/**
 * A LabelMapGraph. <br>
 * The abstract-tag is only set because the tests will not compile otherwise. You should remove it!
 */
public  class LabelMapGraph extends GraphImpl<String, Map<String, String>> implements Graph<String, Map<String, String>> {

    public static void main(String[] args) {

    }

}
