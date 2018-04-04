package ru.fink.service;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.impl.OWLNamedIndividualNode;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.fink.config.OntologyConfig;
import uk.ac.manchester.cs.owl.owlapi.OWLNamedIndividualImpl;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class OntologyService {

    private final OntologyConfig ontologyConfig;

    private OWLOntology owlOntology;
    private OWLReasoner reasoner;

    @Autowired
    public OntologyService(OntologyConfig ontologyConfig) {
        this.ontologyConfig = ontologyConfig;
    }

    @PostConstruct
    public void init() throws OWLOntologyCreationException {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        owlOntology = manager.loadOntologyFromOntologyDocument(new File(ontologyConfig.getOntologyFile()));
        OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
        reasoner = reasonerFactory.createReasoner(owlOntology);
    }

    public Map<String, String> getMap() {
        Map<String, String> map = new HashMap<>();
        Set<OWLClass> classesInSignature = owlOntology.getClassesInSignature();
        for (OWLClass owlClass : classesInSignature) {
            map.put(owlClass.getIRI().getShortForm(), getIndividualByClass(owlClass).getIRI().getShortForm());
        }
        return map;
    }

    private OWLNamedIndividualImpl getIndividualByClass(OWLClass owlClass) {
        NodeSet<OWLNamedIndividual> instances = reasoner.getInstances(owlClass, true);
        Object[] nodes = instances.getNodes().toArray();
        OWLNamedIndividualNode owlNamedIndividuals = (OWLNamedIndividualNode) nodes[0];
        Object[] entities = owlNamedIndividuals.getEntities().toArray();
        return (OWLNamedIndividualImpl) entities[0];
    }


}
