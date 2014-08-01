package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import noise.NoiseGenerator;

import org.spaceroots.mantissa.random.MersenneTwister;

import signaling.*;

public class Ex_Inverse_Main {
	public static final int signal[] = {0, 1};

	public static final MersenneTwister mt = new MersenneTwister();

	public static final NoiseGenerator noise = new NoiseGenerator(mt); 	//ChipErrorを発生させるノイズ生成器

	private static Liu cdma = new Liu();

	//Parameter 変数
	private static long run;
	private static int p;
	private static int codelength;
	private static int[] N;
	private static int Nmax;
	private static double[] chipErrorRate;
	private static UserSelector selector = UserSelector.getInstance();
	private static void init(){
		try {
			Parameter param = new Parameter();

			//Propertyで読むパラメータ
			run = (long) param.getValue("RUN");
			p = (int) param.getValue("P");
			N = (int[]) param.getValue("N");
			chipErrorRate = (double[]) param.getValue("CER");

			//各信号方式での符号語の生成
			cdma.setValue(p);
			cdma.outputInvCodes();
			codelength = cdma.codelength;

			// 最大ユーザーの設定
			Nmax = cdma.Nmax;
			selector.setMaxUser(Nmax);

			if(N[N.length-1] > Nmax) {
				System.err.println("最大ユーザー数以上のユーザー数が設定されています．");
				System.exit(0);
			}

			//NoiseGenerator 出力用のpの格納
			noise.setValue(p);
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	private static OutputResults result;
	public static void main(String[] args) {
		//初期化　　Propertyの読み込み，符号語の生成
		init();

		//シミュレーションの実行
		for(int n = 0; n < N.length; n++){
			//実験結果ファイルの作成
			result = new OutputResults("Inv"+cdma.name+"_p"+p+"_N"+N[n]+"_t"+run+".txt");
			Date date = new Date();
			result.setText(date.toString());

			System.out.println(result.setText("P="+p+"\t AvgUser="+N[n]+"\t 試行回数="+run));
			System.out.println(result.setText("チップ誤り率 \t  復号誤り回数 \t 復号誤り率"));

			long startTime = System.currentTimeMillis();

			//実行
			for(int c = 0; c < chipErrorRate.length; c++){
				execute(run, N[n], chipErrorRate[c]);
			}

			long stopTime = System.currentTimeMillis();

			System.out.println(result.setText("Stop Experiment. time:"+(stopTime - startTime)+"[ms]"));
			result.close();
		}
	}

	// 実験用の実行スクリプト
	private static void execute(Long run, int n, double chipErrorRate){
		long numberOfBitError = 0;

		int fixUser = 0;
		int fixUserSignal = 0;

		for(int t=0; t < run; t++){
			ArrayList<Integer> sendUser = selector.avgselectSendUser(n);
			ArrayList<Integer> sendUserSignal = new ArrayList<>();

			selector.fixSendUser(fixUser, sendUser);

			//送信ユーザーと送信符号の決定，決定した符号はmpsc.encodeにより対応する符号語が送られる．
			//int transSignal;
			//int transCodes[] = new int[codelength]; //送信符号語の格納
			/*for(Integer user : sendUser){
				//送信符号語は干渉値として送られる.
				transSignal = mt.nextInt(signal.length);
				sendUserSignal.add(transSignal);

				if(user == fixUser) fixUserSignal = transSignal;

				int[] inverseCode = SignalingType.inverse(cdma.encode(user, transSignal));

				transCodes = interfere(transCodes, noise.occurChipError(chipErrorRate, inverseCode));
			}*/

			//Liu
			int transSignal;
			int transCodes[] = new int[codelength+1]; //送信符号語の格納
			for(Integer user : sendUser){
				//送信符号語は干渉値として送られる.
				transSignal = mt.nextInt(signal.length);
				sendUserSignal.add(transSignal);

				if(user == fixUser) fixUserSignal = transSignal;

				int[] inverseCode = SignalingType.inverse(cdma.encode(user, transSignal));
				int[] liuinverseCode = new int[inverseCode.length+1];

				System.arraycopy(inverseCode, 0, liuinverseCode, 0, inverseCode.length);
				liuinverseCode[liuinverseCode.length-1] = transSignal;

				System.out.println("user:"+user+",sig:"+transSignal+",code:"+toString(liuinverseCode));

				transCodes = interfere(transCodes, noise.occurChipError(chipErrorRate, liuinverseCode));
			}
			//同時送信による干渉符号語
			System.out.println("InfereCode:"+toString(transCodes));

			//復号データが間違っていた場合，BitErrorを増やす
			/*for(int i=0; i < sendUser.size(); i++)
				if(cdma.invdecode(transCodes, sendUser.get(i)) != sendUserSignal.get(i)) numberOfBitError += 1;
				*/

			if(cdma.invdecode(transCodes, fixUser) != fixUserSignal) numberOfBitError += 1;

		}

		double bitErrorRate = (double)numberOfBitError / (double)(run);
		System.out.println(result.setData(chipErrorRate, numberOfBitError, bitErrorRate));
	}

	//送信符号語の干渉値の計算
	private static int[] interfere(int transCodes[], int otherUserCode[]){
		int muiCode[] = new int[transCodes.length];

		for(int i=0; i < transCodes.length; i++)
			muiCode[i] = transCodes[i] + otherUserCode[i];

		return muiCode;
	}

	//送信符号語の出力用
	private static String toString(int code[]){
		String str = "";
		for(int i=0; i < code.length; i++){
			if(i % p == 0) str = str + " ";
			str = str + code[i];
		}

		return str;
	}
}
