package com.sentimetrix.ctakes.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CtakesFileWriter {    
	private String filename;
	
    public CtakesFileWriter(String filename) throws IOException {
    	this.filename = filename.replaceAll(" ", "_");
    }
    
    public void write(String data) throws IOException {
    	File outFile = new File(this.filename);
		if (!outFile.exists()) {
			outFile.createNewFile();
		}
		
		FileWriter fw = new FileWriter(outFile.getAbsoluteFile(), true);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(data);
		bw.write(System.getProperty("line.separator"));
		bw.close();
    }
}
