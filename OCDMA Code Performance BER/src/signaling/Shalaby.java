package signaling;

public class Shalaby extends SignalingType{;
	public Shalaby() {
		// TODO 自動生成されたコンストラクター・スタブ
		super("SHALABY");
	}

	@Override
	public void setNmax(int p) {
		// TODO 自動生成されたメソッド・スタブ
		super.Nmax = p * p - p;
		super.codelength = p*p;
	}

	//参照符号語の格納
	private static int[][] refer;
	private static int[][] referMPS;
	@Override
	public Object encodeSetting(int[][][] user, int[][][] userMPS) {
		// TODO 自動生成されたメソッド・スタブ
		//ユーザー符号語
		user = new int[Nmax][super.signal.length][super.codelength];
		userMPS = new int[Nmax][super.signal.length][p];

		//参照符号語
		refer = new int[p][super.codelength];
		referMPS = new int[p][p];

		//初期化コード
		int[] code0 = super.initCode();

		int jump = 0;
		int flg = 1;
		for(int i=0; i < Nmax; i++){
			// pが奇数の時の処理．
			if(p*flg - i -jump == 1) {
				jump += 1;
				flg += 1;
			}

			//符号語
			user[i][1] = super.mps.getMPSC(i+jump);
			user[i][0] = code0;

			//相関値計算用の'1'の位置
			userMPS[i][1] = super.mps.getMPS(i+jump);
		}

		//参照符号語
		for(int i=1; i < p+1; i++){
			refer[i-1] = super.mps.getMPSC(p*i-1);
			referMPS[i-1] = super.mps.getMPS(p*i-1);
		}

		CodeSet codeSet = new CodeSet(user, userMPS);
		return codeSet;
	}

	@Override
	public int decodeSetting(int no, int[] muiCode, int[][][] userNoMPS) {
		// TODO 自動生成されたメソッド・スタブ
		int decodeSignal = 0;
		int correlation[] = new int[super.signal.length];

		for(int i=0; i < p; i++){
			correlation[0] = correlation[0] + muiCode[referMPS[no/p][i]];
			correlation[1] = correlation[1] + muiCode[userMPS[no][1][i]];
		}

		float q = (float)p / (float)2;
		if((correlation[1] - correlation[0]) >= q) decodeSignal = 1;

		return decodeSignal;
	}

	@Override
	public int invdecodeSetting(int no, int[] muiCode, int[][][] userMPS) {
		// TODO 自動生成されたメソッド・スタブ
		int decodeSignal = 0;
		int correlation[] = new int[super.signal.length];

		for(int i=0; i < muiCode.length; i++){
			if(i != referMPS[no/p][i/p])
				correlation[0] = correlation[0] + muiCode[i];
			if(i != userMPS[no][1][i/p])
				correlation[1] = correlation[1] + muiCode[i];
		}

		float q = (float)p / (float)2;
		if((correlation[1] - correlation[0]) >= q) decodeSignal = 1;
		//System.out.println("'0':"+correlation[0] + " '1':"+correlation[1]);

		return decodeSignal;
	}

	@Override
	public void addInfo() {
		// TODO 自動生成されたメソッド・スタブ
		System.out.println("ReferenceCodes \n");
		for(int i=0; i < refer.length; i++){
			System.out.println(" Group "+i+":"+super.toString(refer[i]));
		}
	}
}