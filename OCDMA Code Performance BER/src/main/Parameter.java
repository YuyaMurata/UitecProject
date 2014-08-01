package main;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;


public class Parameter {
	private Properties conf = null;
	private static final File file = new File("parameter.properties");

	public static HashMap<String, Object> parameter = new HashMap<>();
	public Parameter() throws IOException {
		// TODO 自動生成されたコンストラクター・スタブ
		conf = new Properties();
		conf.load(new FileInputStream(file));

		init();
	}

	private void init(){
		parameter.put("RUN", Long.valueOf(conf.getProperty("RUN")));
		parameter.put("P", Integer.valueOf(conf.getProperty("P")));

		String[] nParam = conf.getProperty("N").split(",");
		int[] N = new int[nParam.length];
		for(int i=0; i < N.length; i++)
			N[i] = Integer.valueOf(nParam[i]);
		parameter.put("N", N);

		String[] cerParam = conf.getProperty("CER").split(",");
		double[] chipErrorRate = new double[cerParam.length];
		for(int i=0; i < chipErrorRate.length; i++)
			chipErrorRate[i] = Double.valueOf(cerParam[i]);
		parameter.put("CER", chipErrorRate);
	}

	public Object getValue(String key){
		return parameter.get(key);
	}

	public static void printPropaty(){
		try {
			Parameter param = new Parameter();

			System.out.println("Run ="+param.getValue("RUN"));
			System.out.println("P = "+param.getValue("P"));
			System.out.println("N ="+ param.getValue("N"));
			System.out.println("ChipErrorRate=["+param.getValue("CER")+"]");

		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

	}


	/**public static void main(String[] args) {
		printPropaty();
	}**/


}
