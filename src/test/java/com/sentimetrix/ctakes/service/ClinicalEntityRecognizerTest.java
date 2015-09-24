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
    	
		String expectedResult = "SENTENCE:  The patient has diabetes.\n           DT    NN    VBZ    NN    \n                           |======| \n                           Disorder \n                           C0011849 \n\n";
    	JCas jcas = cer.process("The patient has diabetes.");
    	String result = cer.formatResultsSentence(jcas);
    	assertEquals(result, expectedResult);
    }
	
	@Test
	public void processNegativeTest() throws Exception {
    	
		String expectedResult = "SENTENCE:  The patient has no headaches.\n           DT    NN    VBZ DT    NNS    \n                              |=======| \n                               Finding  \n                              C0018681  \n                               Negated  \n\n"; 
    	JCas jcas = cer.process("The patient has no headaches.");
    	String result = cer.formatResultsSentence(jcas);
    	assertEquals(result, expectedResult);
    	System.out.println(cer.formatResultsSentence(jcas));
    }


}
