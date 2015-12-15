package com.sentimetrix.ctakes.util;

import org.apache.ctakes.typesystem.type.refsem.OntologyConcept;
import org.apache.ctakes.typesystem.type.refsem.UmlsConcept;
import org.apache.ctakes.typesystem.type.textsem.IdentifiedAnnotation;
import org.apache.log4j.Logger;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.jcas.cas.FSArray;

import java.util.Collection;
import java.util.HashSet;

/**
 * @author SPF , chip-nlp
 * @version %I%
 * @since 1/8/2015
 */
final public class IdentifiedAnnotationUtil {

   static private final Logger LOGGER = Logger.getLogger( "IdentifiedAnnotationUtil" );

   static public final String CTAKES_SNOMED_CODING_SCHEME = "SNOMED";
   static public final String CTAKES_RXNORM_CODING_SCHEME = "RXNORM";

   static private final FeatureStructure[] EMPTY_FEATURE_ARRAY = new FeatureStructure[ 0 ];

   private IdentifiedAnnotationUtil() {
   }


   static private FeatureStructure[] getOntologyConcepts( final IdentifiedAnnotation annotation ) {
      final FSArray ontologyConcepts = annotation.getOntologyConceptArr();
      if ( ontologyConcepts == null ) {
         return EMPTY_FEATURE_ARRAY;
      }
      return ontologyConcepts.toArray();
   }

   /**
    * @param annotation -
    * @return list of all Umls Concepts associated with the annotation
    */
   static public Collection<UmlsConcept> getUmlsConcepts( final IdentifiedAnnotation annotation ) {
      final FeatureStructure[] ontologyConcepts = getOntologyConcepts( annotation );
      final Collection<UmlsConcept> umlsConcepts = new HashSet<UmlsConcept>( ontologyConcepts.length );
      for ( FeatureStructure ontologyConcept : ontologyConcepts ) {
         if ( ontologyConcept instanceof UmlsConcept ) {
            umlsConcepts.add( (UmlsConcept)ontologyConcept );
         }
      }
      return umlsConcepts;
   }

   /**
    * @param annotation -
    * @return list of all Umls cuis associated with the annotation
    */
   static public Collection<String> getUmlsCuis( final IdentifiedAnnotation annotation ) {
      final FeatureStructure[] ontologyConcepts = getOntologyConcepts( annotation );
      final Collection<String> cuis = new HashSet<String>( ontologyConcepts.length );
      for ( FeatureStructure ontologyConcept : ontologyConcepts ) {
         if ( ontologyConcept instanceof UmlsConcept ) {
            final UmlsConcept umlsConcept = (UmlsConcept)ontologyConcept;
            final String cui = umlsConcept.getCui();
            cuis.add( cui );
         }
      }
      return cuis;
   }

   /**
    * @param annotation -
    * @return list of all Snomed codes associated with the annotation
    */
   static public Collection<String> getSnomedCodes( final IdentifiedAnnotation annotation ) {
      final FeatureStructure[] ontologyConcepts = getOntologyConcepts( annotation );
      final Collection<String> snomeds = new HashSet<String>();
      for ( FeatureStructure featureStructure : ontologyConcepts ) {
         final OntologyConcept ontologyConcept = (OntologyConcept)featureStructure;
         if ( ontologyConcept instanceof UmlsConcept ) {
            continue;
         }
         final String code = ontologyConcept.getCode();
         if ( code == null || code.isEmpty() ) {
            continue;
         }
         final String codingScheme = ontologyConcept.getCodingScheme();
         if ( codingScheme != null && CTAKES_SNOMED_CODING_SCHEME.equalsIgnoreCase( codingScheme.trim() ) ) {
            snomeds.add( code );
         }
      }
      return snomeds;
   }

   /**
    * @param annotation -
    * @return list of all rxNORM codes associated with the annotation
    */
   static public Collection<String> getRxNormCodes( final IdentifiedAnnotation annotation ) {
      final FeatureStructure[] ontologyConcepts = getOntologyConcepts( annotation );
      final Collection<String> rxNorms = new HashSet<String>();
      for ( FeatureStructure featureStructure : ontologyConcepts ) {
         final OntologyConcept ontologyConcept = (OntologyConcept)featureStructure;
         if ( ontologyConcept instanceof UmlsConcept ) {
            continue;
         }
         final String code = ontologyConcept.getCode();
         if ( code == null || code.isEmpty() ) {
            continue;
         }
         final String codingScheme = ontologyConcept.getCodingScheme();
         if ( codingScheme != null && CTAKES_RXNORM_CODING_SCHEME.equalsIgnoreCase( codingScheme.trim() ) ) {
            rxNorms.add( code );
         }
      }
      return rxNorms;
   }


}