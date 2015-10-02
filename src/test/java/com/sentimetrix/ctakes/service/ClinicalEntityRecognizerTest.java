package com.sentimetrix.ctakes.service;

import static org.junit.Assert.*;

import org.apache.uima.jcas.JCas;
import org.junit.Before;
import org.junit.Test;

public class ClinicalEntityRecognizerTest {
	private ClinicalEntityRecognizer cer = null;
	
    @Before
    public void setUp() throws Exception
    {
    	cer = new ClinicalEntityRecognizer();
    }

	@Test
	public void processPositiveTest() throws Exception {
		JCas jcas = cer.process("The patient has diabetes.");
		String result = cer.formatResultsText(jcas);
		assertFalse(result.isEmpty());
    }
	
	@Test
	public void processNegativeTest() throws Exception {
		JCas jcas = cer.process("The patient has no headaches.");
		String result = cer.formatResultsSentence(jcas);
		assertFalse(result.isEmpty());
    }


}
