package ru.fink.converter;

//import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.OWLXMLDocumentFormat;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import ru.fink.model.TreeNode;
import ru.fink.utils.parser.seman.KnowledgeSign;
import ru.fink.utils.parser.seman.KnowledgeType;

import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class KnowledgeToOwlConverter {

    private static final OWLOntologyManager MANAGER = OWLManager.createOWLOntologyManager();
    private static final OWLDataFactory FACTORY = MANAGER.getOWLDataFactory();

    @SneakyThrows
    public static byte[] convert(List<TreeNode> parsedTemplates) {
        OWLOntology ontology = MANAGER.createOntology();
//      TODO: не компилируется
//        ByteOutputStream outputStream = new ByteOutputStream();

        parsedTemplates.forEach(parsedTemplate -> processTemplate(ontology, parsedTemplate));

//        MANAGER.saveOntology(ontology, new OWLXMLDocumentFormat(), outputStream);
//        return (outputStream).getBytes();
        return null;
    }

    private static void processTemplate(OWLOntology ontology, TreeNode parsedTemplate) {
        KnowledgeSign knowledgeSign = parsedTemplate.getNode().knowledgeSign;

        if (knowledgeSign != null) {
            addKnowledge(ontology, knowledgeSign, parsedTemplate.getNode().name);
        }

        parsedTemplate.getTreeNodes().forEach(child -> processTemplate(ontology, child));
    }

    private static void addKnowledge(OWLOntology ontology, KnowledgeSign knowledgeSign, String name) {
        if (knowledgeSign.getType() == KnowledgeType.CLASS) {
            OWLClass originalClass = createClass(name);

            if (StringUtils.isNotEmpty(knowledgeSign.getParentClass())) {
                OWLClass parentClass = getClass(knowledgeSign.getParentClass());
                addSubClassConnection(ontology, parentClass, originalClass);
            }

            if (StringUtils.isNotEmpty(knowledgeSign.getPredicate())) {
                OWLObjectProperty objectProperty = createObjectProperty(knowledgeSign.getPredicate());

                if (knowledgeSign.getSubjectType() == KnowledgeType.CLASS) {
                    OWLClass subject = createClass(knowledgeSign.getSubject());
                    addNewEquivalent(ontology, originalClass, objectProperty, subject);
                } else {
                    OWLIndividual individual = createIndividual(knowledgeSign.getSubject());
                    addNewEquivalent(ontology, originalClass, objectProperty, individual);
                }

            }

        } else {
            OWLIndividual object = createIndividual(name);

            if (StringUtils.isNotEmpty(knowledgeSign.getClassObject())) {
                OWLClass classObject = getClass(knowledgeSign.getClassObject());
                addObjectClassConnection(ontology, classObject, object);
            }

            if (StringUtils.isNotEmpty(knowledgeSign.getPredicate())) {
                OWLObjectProperty objectProperty = createObjectProperty(knowledgeSign.getPredicate());

                if (knowledgeSign.getSubjectType() == KnowledgeType.OBJECT) {
                    OWLIndividual individual = createIndividual(knowledgeSign.getSubject());
                    addNewEquivalent(ontology, object, objectProperty, individual);
                }

            }
        }

    }

    private static void addSubClassConnection(OWLOntology ontology, OWLClass owlClass, OWLClass owlSubClass) {
        MANAGER.applyChange(new AddAxiom(ontology, FACTORY.getOWLSubClassOfAxiom(owlSubClass, owlClass)));
    }

    private static OWLClass getClass(String name) {
        return FACTORY.getOWLClass(IRI.create(name));
    }

    private static OWLClass createClass(String name) {
        return FACTORY.getOWLClass(IRI.create(name));
    }

    private static void addObjectClassConnection(OWLOntology owlOntology, OWLClass owlClass,
                                                 OWLIndividual owlIndividual) {
        OWLClassAssertionAxiom owlClassAssertionAxiom = FACTORY.getOWLClassAssertionAxiom(owlClass, owlIndividual);
        MANAGER.addAxiom(owlOntology, owlClassAssertionAxiom);
    }

    private static OWLIndividual createIndividual(String name) {
        return FACTORY.getOWLNamedIndividual(IRI.create(name));
    }

    private static OWLObjectProperty createObjectProperty(String name) {
        return FACTORY.getOWLObjectProperty(IRI.create(name));
    }

    private static void addNewEquivalent(OWLOntology owlOntology, OWLIndividual owlClassDomain,
                                         OWLObjectProperty owlObjectProperty, OWLIndividual owlIndividual) {

        if (owlObjectProperty == null) {
            return;
        }

        OWLObjectPropertyAssertionAxiom owlObjectPropertyAssertionAxiom =
                FACTORY.getOWLObjectPropertyAssertionAxiom(owlObjectProperty, owlClassDomain, owlIndividual);
        MANAGER.addAxiom(owlOntology, owlObjectPropertyAssertionAxiom);

    }

    private static void addNewEquivalent(OWLOntology owlOntology, OWLClass owlClassDomain,
                                         OWLObjectProperty owlObjectProperty, OWLIndividual owlIndividual) {

        if (owlObjectProperty == null) {
            return;
        }

        OWLClassExpression newExpression = FACTORY.getOWLObjectHasValue(owlObjectProperty, owlIndividual);
        OWLEquivalentClassesAxiom owlEquivalentClassesAxiom =
                FACTORY.getOWLEquivalentClassesAxiom(owlClassDomain, newExpression);
        MANAGER.addAxiom(owlOntology, owlEquivalentClassesAxiom);

    }

    private static void addNewEquivalent(OWLOntology owlOntology, OWLClass owlClassDomain,
                                         OWLObjectProperty owlObjectProperty, OWLClass owlClass) {

        if (owlObjectProperty == null) {
            return;
        }

        OWLClassExpression newExpression = FACTORY.getOWLObjectSomeValuesFrom(owlObjectProperty, owlClass);
        OWLEquivalentClassesAxiom owlEquivalentClassesAxiom =
                FACTORY.getOWLEquivalentClassesAxiom(owlClassDomain, newExpression);
        MANAGER.addAxiom(owlOntology, owlEquivalentClassesAxiom);

    }

    private static void addEquivalent(OWLOntology owlOntology, OWLClass owlClassDomain,
                                      OWLObjectProperty owlObjectProperty, OWLIndividual owlIndividual) {

        if (owlObjectProperty == null) {
            return;
        }
        Set<OWLEquivalentClassesAxiom> equivalentClassesAxioms = owlOntology.getEquivalentClassesAxioms(owlClassDomain);
        OWLClassExpression newExpression = FACTORY.getOWLObjectHasValue(owlObjectProperty, owlIndividual);

        if (equivalentClassesAxioms.isEmpty()) {
            OWLEquivalentClassesAxiom owlEquivalentClassesAxiom =
                    FACTORY.getOWLEquivalentClassesAxiom(owlClassDomain, newExpression);
            MANAGER.addAxiom(owlOntology, owlEquivalentClassesAxiom);
        } else {
            OWLEquivalentClassesAxiom next = equivalentClassesAxioms.iterator().next();
            Iterator<OWLClassExpression> iterator = next.getClassExpressions().iterator();
            OWLClassExpression oldExpression = iterator.next();
            if (oldExpression.equals(owlClassDomain)) {
                oldExpression = iterator.next();
            }
            MANAGER.removeAxiom(owlOntology, next);
            OWLEquivalentClassesAxiom owlEquivalentClassesAxiom =
                    FACTORY.getOWLEquivalentClassesAxiom(owlClassDomain,
                            FACTORY.getOWLObjectIntersectionOf(oldExpression, newExpression));
            MANAGER.addAxiom(owlOntology, owlEquivalentClassesAxiom);
        }

    }

}
