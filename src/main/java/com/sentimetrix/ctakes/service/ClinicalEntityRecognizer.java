package com.sentimetrix.ctakes.service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.List;

import org.apache.ctakes.core.cc.pretty.plaintext.PrettyTextWriter;
import org.apache.ctakes.typesystem.type.textspan.Sentence;
import org.apache.log4j.Logger;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.cas.Feature;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.cas.TOP;
import org.apache.uima.jcas.JCas;
import org.apache.uima.fit.util.JCasUtil;

import com.sentimetrix.ctakes.pipeline.Pipeline;

public class ClinicalEntityRecognizer {
	private static final Logger LOGGER = Logger.getLogger(ClinicalEntityRecognizer.class);
	private static final NumberFormat formatter = new DecimalFormat("#0.00000");
	private JCas jcas = null;
	private AnalysisEngine pipeline = null;
	
	public ClinicalEntityRecognizer() throws Exception {
		try {
			pipeline = Pipeline.getAggregateBuilder().createAggregate();
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
						&& val.trim().length() > 0
						) {
					sb.append(" " + feature.getShortName());
					sb.append(":" + val);
				}
			}
		}
	}

}
