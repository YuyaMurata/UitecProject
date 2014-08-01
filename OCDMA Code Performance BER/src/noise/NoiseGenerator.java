package noise;
import org.spaceroots.mantissa.random.MersenneTwister;


public class NoiseGenerator {
	private MersenneTwister mt;
	public static int[] noisyCode;

	//Noise発生タイミングで使う乱数を取得
	public NoiseGenerator(MersenneTwister mt) {
		// TODO 自動生成されたコンストラクター・スタブ
		this.mt = mt;
	}

	private int p;
	public void setValue(int p){
		this.p = p;
	}

	//ランダムに送信データのチップを反転させる．
	public int[] occurChipError(Double errorRate, int[] code){
		int noise;

		for(int i=0; i < code.length; i++)
			if(mt.nextDouble() < errorRate){
				noise =  (code[i]+1) % 2;
				//System.out.println("i:"+code[i]+","+noise);
				code[i] = noise;
			}

		return code;
	}

	//クラス内でのノイズコード出力用
	public String toString(int[] code){
		StringBuilder sb = new StringBuilder();

		for(int i=0; i < code.length; i++){
			if(i % p == 0) sb.append(" ");
			sb.append(code[i]);
		}

		return sb.toString();
	}
}
