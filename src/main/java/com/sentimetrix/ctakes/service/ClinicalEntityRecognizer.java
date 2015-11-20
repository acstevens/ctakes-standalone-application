package com.sentimetrix.ctakes.service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.ctakes.core.cc.pretty.SemanticGroup;
import org.apache.ctakes.core.cc.pretty.plaintext.PrettyTextWriter;
import org.apache.ctakes.core.util.IdentifiedAnnotationUtil;
import org.apache.ctakes.typesystem.type.constants.CONST;
import org.apache.ctakes.typesystem.type.refsem.UmlsConcept;
import org.apache.ctakes.typesystem.type.textsem.IdentifiedAnnotation;
import org.apache.ctakes.typesystem.type.textspan.Sentence;
import org.apache.log4j.Logger;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.cas.Feature;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.cas.TOP;
import org.apache.uima.jcas.JCas;
import org.apache.uima.fit.util.JCasUtil;

import com.google.gson.Gson;
import com.sentimetrix.ctakes.application.model.Entity;
import com.sentimetrix.ctakes.pipeline.PipelineFactory;
import com.sentimetrix.ctakes.pipeline.PipelineEnum;

public class ClinicalEntityRecognizer {
	private static final Logger LOGGER = Logger.getLogger(ClinicalEntityRecognizer.class);
	private static final NumberFormat formatter = new DecimalFormat("#0.00000");
	private JCas jcas = null;
	private AnalysisEngine pipeline = null;
	private Gson gson = null;

	public ClinicalEntityRecognizer(PipelineEnum pipelineType) throws Exception {
		gson = new Gson();

		try {
			pipeline = PipelineFactory.getPipeline(pipelineType).createAggregate();
			jcas = pipeline.newJCas();
		} catch (Exception e) {
			LOGGER.error("unable to create pipeline: " + e.getLocalizedMessage());
			throw e;
		}
	}

	public JCas process(String text) {
		if (text == null || text.isEmpty()) {
			return null;
		}

		try {
			long start = System.currentTimeMillis();
			jcas.reset();
			jcas.setDocumentText(text);
			pipeline.process(jcas);
			String elapsed = formatter.format((System.currentTimeMillis() - start) / 1000d);
			LOGGER.info("processed in " + elapsed + " secs");
			return jcas;
		} catch (Exception e) {
			LOGGER.error("unable to extract clinical entities: " + e.getLocalizedMessage());
		}

		return null;
	}

	public String formatResultsSentence(JCas jcas) throws IOException {
		StringWriter sw = new StringWriter();
		BufferedWriter writer = new BufferedWriter(sw);
		Collection<Sentence> sentences = JCasUtil.select(jcas, Sentence.class);
		for (Sentence sentence : sentences) {
			PrettyTextWriter.writeSentence(jcas, sentence, writer);
		}
		writer.close();
		return sw.toString();
	}

	public String formatResultsText(JCas jcas) {
		Collection<TOP> annotations = JCasUtil.selectAll(jcas);
		StringBuffer sb = new StringBuffer();

		for (TOP a : annotations) {
			sb.append("\n" + a.getType().getShortName() + " ->");
			extractFeatures(sb, (FeatureStructure) a);
		}
		return sb.toString();
	}

	public void extractFeatures(StringBuffer sb, FeatureStructure fs) {
		List<?> plist = fs.getType().getFeatures();
		for (Object obj : plist) {
			if (obj instanceof Feature) {
				Feature feature = (Feature) obj;
				String val = "";
				if (feature.getRange().isPrimitive()) {
					val = fs.getFeatureValueAsString(feature);
				} else if (feature.getRange().isArray()) {
					// Flatten the Arrays
					FeatureStructure featval = fs.getFeatureValue(feature);
					if (featval instanceof FSArray) {
						FSArray valarray = (FSArray) featval;
						for (int i = 0; i < valarray.size(); ++i) {
							FeatureStructure temp = valarray.get(i);
							extractFeatures(sb, temp);
						}
					}
				}
				if (feature.getName() != null 
						&& val != null
						&& val.trim().length() > 0) {
					sb.append(" " + feature.getShortName());
					sb.append(":" + val);
				}
			}
		}
	}

	public String formatResultsJson(JCas jcas) {
		List<Entity> entities = new ArrayList<Entity>();

		for (IdentifiedAnnotation identifiedAnnotation : JCasUtil.select(jcas,
				IdentifiedAnnotation.class)) {

			Map<String, Collection<String>> semanticCuis = getSemanticCuis(identifiedAnnotation);

			if ( semanticCuis != null & !semanticCuis.isEmpty() ) {
				Map.Entry<String, Collection<String>> entry=semanticCuis.entrySet().iterator().next();
				Entity entity = new Entity();
				entity.setName(identifiedAnnotation.getCoveredText());
				entity.setPolarity(identifiedAnnotation.getPolarity());
				entity.setType(entry.getKey());
				entity.setCuis(entry.getValue());
				entity.setSubject(identifiedAnnotation.getSubject());
				entity.setUncertain((identifiedAnnotation.getUncertainty() == CONST.NE_UNCERTAINTY_PRESENT));
				entity.setGeneric((identifiedAnnotation.getGeneric() == CONST.NE_GENERIC_TRUE));
				entity.setConditional((identifiedAnnotation.getConditional() == CONST.NE_CONDITIONAL_TRUE));
				entity.setHistoryOf((identifiedAnnotation.getHistoryOf() == CONST.NE_HISTORY_OF_PRESENT));

				entities.add(entity);
			}
		}

		return gson.toJson(entities);
	}

	public String format(OutputEnum outputType, JCas jcas) throws IOException {
		switch (outputType) {
		case TEXT:
			return formatResultsText(jcas);
		case SENTENCE:
			return formatResultsSentence(jcas);
		case JSON:
			return formatResultsJson(jcas);
		}
		return formatResultsSentence(jcas);
	}

	static private Map<String, Collection<String>> getSemanticCuis( final IdentifiedAnnotation identifiedAnnotation ) {
		final Collection<UmlsConcept> umlsConcepts = IdentifiedAnnotationUtil.getUmlsConcepts( identifiedAnnotation );
		if ( umlsConcepts == null || umlsConcepts.isEmpty() ) {
			return Collections.emptyMap();
		}
		final Map<String, Collection<String>> semanticCuis = new HashMap<String, Collection<String>>();
		for ( UmlsConcept umlsConcept : umlsConcepts ) {
			final String cui = umlsConcept.getCui();
			final String tui = umlsConcept.getTui();
			final String semanticName = SemanticGroup.getSemanticName( tui );
			Collection<String> cuis = semanticCuis.get( semanticName );
			if ( cuis == null ) {
				cuis = new HashSet<String>();
				semanticCuis.put( semanticName, cuis );
			}
			cuis.add( cui );
		}
		return semanticCuis;
	}
}
