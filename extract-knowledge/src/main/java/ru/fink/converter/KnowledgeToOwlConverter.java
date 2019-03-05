package ru.fink.converter;


import lombok.SneakyThrows;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import ru.fink.model.TreeNode;

import java.util.List;


public class KnowledgeToOwlConverter {

    private static final OWLOntologyManager MANAGER = OWLManager.createOWLOntologyManager();

    @SneakyThrows
    public static byte[] convert(List<TreeNode> parsedTempates) {
        OWLDataFactory factory = MANAGER.getOWLDataFactory();
        OWLOntology ontology = MANAGER.createOntology();

        //TODO
        return null;
    }

}
