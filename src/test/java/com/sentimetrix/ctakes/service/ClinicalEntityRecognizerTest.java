package com.sentimetrix.ctakes.service;

import static org.junit.Assert.*;

import org.apache.uima.jcas.JCas;
import org.junit.Before;
import org.junit.Test;

import com.sentimetrix.ctakes.pipeline.PipelineEnum;

public class ClinicalEntityRecognizerTest {
	private ClinicalEntityRecognizer cer = null;
	
    @Before
    public void setUp() throws Exception
    {
    	cer = new ClinicalEntityRecognizer(PipelineEnum.FAST);
    }

	@Test
	public void processPositiveTest() throws Exception {
		JCas jcas = cer.process("The patient did not have diabetes.  "
				+ "The patient reported pain in her upper arm. "
				+ "No swelling. "
				+ "Aspirin 325 mg."
		);
		String result = cer.format(OutputEnum.SENTENCE, jcas);
		System.out.println(result);
		assertFalse(result.isEmpty());
    }
	
}
