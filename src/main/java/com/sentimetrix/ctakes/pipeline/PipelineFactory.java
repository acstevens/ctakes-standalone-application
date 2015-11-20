package com.sentimetrix.ctakes.pipeline;

import org.apache.ctakes.assertion.medfacts.cleartk.ConditionalCleartkAnalysisEngine;
import org.apache.ctakes.assertion.medfacts.cleartk.GenericCleartkAnalysisEngine;
import org.apache.ctakes.assertion.medfacts.cleartk.HistoryCleartkAnalysisEngine;
import org.apache.ctakes.assertion.medfacts.cleartk.PolarityCleartkAnalysisEngine;
import org.apache.ctakes.assertion.medfacts.cleartk.SubjectCleartkAnalysisEngine;
import org.apache.ctakes.assertion.medfacts.cleartk.UncertaintyCleartkAnalysisEngine;
import org.apache.ctakes.clinicalpipeline.ClinicalPipelineFactory;
import org.apache.ctakes.constituency.parser.ae.ConstituencyParser;
import org.apache.ctakes.dependency.parser.ae.ClearNLPDependencyParserAE;
import org.apache.ctakes.dictionary.lookup.ae.UmlsDictionaryLookupAnnotator;
import org.apache.ctakes.dictionary.lookup2.ae.AbstractJCasTermAnnotator;
import org.apache.ctakes.dictionary.lookup2.ae.DefaultJCasTermAnnotator;
import org.apache.ctakes.dictionary.lookup2.ae.JCasTermAnnotator;
import org.apache.uima.fit.factory.AggregateBuilder;
import org.apache.uima.fit.factory.AnalysisEngineFactory;

public class PipelineFactory {
	public static AggregateBuilder getPipeline(PipelineEnum pipelineType) throws Exception {
		switch (pipelineType) {
			case DEFAULT:
				return getDefaultPipeline();
			case FAST:
				return getFastPipeline();
		}
		return getDefaultPipeline();
	}

	public static AggregateBuilder getDefaultPipeline() throws Exception {
		AggregateBuilder builder = new AggregateBuilder();
	      builder.add( ClinicalPipelineFactory.getTokenProcessingPipeline() );
	      builder.add( ClinicalPipelineFactory.getNpChunkerPipeline() );
	      builder.add( AnalysisEngineFactory.createEngineDescription( ConstituencyParser.class ) );
	      builder.add( UmlsDictionaryLookupAnnotator.createAnnotatorDescription() );
	      builder.add( ClearNLPDependencyParserAE.createAnnotatorDescription() );
	      builder.add( PolarityCleartkAnalysisEngine.createAnnotatorDescription() );
	      builder.add( UncertaintyCleartkAnalysisEngine.createAnnotatorDescription() );
	      builder.add( HistoryCleartkAnalysisEngine.createAnnotatorDescription() );
	      builder.add( ConditionalCleartkAnalysisEngine.createAnnotatorDescription() );
	      builder.add( GenericCleartkAnalysisEngine.createAnnotatorDescription() );
	      builder.add( SubjectCleartkAnalysisEngine.createAnnotatorDescription() );

		return builder;
	}

	public static AggregateBuilder getFastPipeline() throws Exception {
		AggregateBuilder builder = new AggregateBuilder();
	      builder.add( ClinicalPipelineFactory.getTokenProcessingPipeline() );

	      builder.add( AnalysisEngineFactory.createEngineDescription( DefaultJCasTermAnnotator.class,
	               AbstractJCasTermAnnotator.PARAM_WINDOW_ANNOT_KEY,
	               "org.apache.ctakes.typesystem.type.textspan.Sentence",
	               JCasTermAnnotator.DICTIONARY_DESCRIPTOR_KEY,
	               "org/apache/ctakes/dictionary/lookup/fast/cTakesHsql.xml" )
	         );
	      builder.add( ClearNLPDependencyParserAE.createAnnotatorDescription() );
	      builder.add( PolarityCleartkAnalysisEngine.createAnnotatorDescription() );
	      builder.add( UncertaintyCleartkAnalysisEngine.createAnnotatorDescription() );
	      builder.add( HistoryCleartkAnalysisEngine.createAnnotatorDescription() );
	      builder.add( ConditionalCleartkAnalysisEngine.createAnnotatorDescription() );
	      builder.add( GenericCleartkAnalysisEngine.createAnnotatorDescription() );
	      builder.add( SubjectCleartkAnalysisEngine.createAnnotatorDescription() );
		return builder;
	}
}
