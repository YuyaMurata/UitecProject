package signaling;

public class Liu extends SignalingType{
	public Liu() {
		// TODO 自動生成されたコンストラクター・スタブ
		super("LIU");
	}

	@Override
	public void setNmax(int p) {
		// TODO 自動生成されたメソッド・スタブ
		super.Nmax = p*p;
		super.codelength = p*p+p;
	}

	@Override
	public Object encodeSetting(int[][][] user, int[][][] userMPS) {
		// TODO 自動生成されたメソッド・スタブ

		user = new int[Nmax][super.signal.length][super.codelength];
		userMPS = new int[Nmax][super.signal.length][p];

		int[] code0 = super.initCode();

		int s = -1;
		for(int i=0; i < Nmax; i++){
			//符号語
			for(int j=0; j < p*p; j++){
				user[i][0][j] = code0[j];
				user[i][1][j] = super.mps.getMPSC(i, j);
			}

			//相関値計算用の'1'の位置
			userMPS[i][1] = super.mps.getMPS(i);

			//pビット情報を付加
			if(i % p == 0) s++;
			for(int k=0; k < p; k++) {
				user[i][1][p*p + k] = 0;
				user[i][0][p*p + k] = 0;
				if(k == s) {
					user[i][1][p*p + k] = 1;
					userMPS[i][0][k] = -1;
				}else{
					userMPS[i][0][k] = p*p + k;
				}
			}
		}

		CodeSet codeSet = new CodeSet(user, userMPS);
		return codeSet;
	}

	@Override
	public int decodeSetting(int no, int[] muiCode, int[][][] userNoMPS) {
		// TODO 自動生成されたメソッド・スタブ
		int decodeSignal = 0;
		int correlation[] = new int[signal.length];

		for(int i=0; i < p; i++){
			correlation[1] = correlation[1] + muiCode[userMPS[no][1][i]];
			if(userMPS[no][0][i] != -1)
				correlation[0] = correlation[0] + muiCode[userMPS[no][0][i]];
		}

		float q = (float)p / (float)2;
		if((correlation[1] - correlation[0]) >= q) decodeSignal = 1;

		return decodeSignal;
	}

	@Override
	public int invdecodeSetting(int no, int[] muiCode, int[][][] userMPS) {
		// TODO 自動生成されたメソッド・スタブ
		int decodeSignal = 0;
		int correlation[] = new int[signal.length];

		for(int i=0; i < codelength-p; i++){
			if(i != userMPS[no][1][i/p])
				correlation[1] = correlation[1] + muiCode[i];
		}

		for(int i=0;i < p; i++){
			if(userMPS[no][0][i] != -1)
				correlation[0] = correlation[0] + muiCode[userMPS[no][0][i]];
		}
		correlation[0] = correlation[0] + muiCode[muiCode.length-1];

		System.out.println("c0:"+correlation[0]+", c1:"+correlation[1]);

		float q = (float)p / (float)2;
		if((correlation[0] - correlation[1]) > q) decodeSignal = 1;

		return decodeSignal;
	}

	@Override
	public void addInfo() {
		// TODO 自動生成されたメソッド・スタブ
	}

}