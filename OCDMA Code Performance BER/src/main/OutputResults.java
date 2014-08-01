package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class OutputResults {
	private File file;
	private PrintWriter pw;
	public OutputResults(String filename) {
		// TODO 自動生成されたコンストラクター・スタブ
		try {
			this.file = new File(filename);
			this.pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	public String setText(String str){
		pw.println(str);
		return str;
	}

	public String setData(double chipErrorRate, long numBitError, double bitErrorRate){
		String str = chipErrorRate+" \t"+numBitError+"\t"+String.format("%.10f", bitErrorRate);
		return setText(str);
	}

	public void close(){
		pw.close();
	}
}
