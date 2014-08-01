package signaling;

public class EWO extends SignalingType{
	public EWO() {
		// TODO 自動生成されたコンストラクター・スタブ
		super("EWO");
	}

	//Nmaxの計算
	@Override
	public void setNmax(int p) {
		// TODO 自動生成されたメソッド・スタブ
		super.Nmax = p * (p / 2);
		super.codelength = p*p;
	}

	//エンコードルールの設定
	@Override
	public Object encodeSetting(int[][][] user, int[][][] userMPS) {
		// TODO 自動生成されたメソッド・スタブ
		//ユーザー符号語
		user = new int[Nmax][super.signal.length][super.codelength];
		userMPS = new int[Nmax][super.signal.length][p];

		int jump = 0;
		int flg = 1;
		for(int i=0; i < Nmax; i++){
			// pが奇数の時の処理．
			if(p*flg - 2*i - jump == 1) {
				jump += 1;
				flg += 1;
			}

			for(int j =0; j < super.signal.length; j++){
				//符号語
				user[i][j] = super.mps.getMPSC(2*i+jump+j);

				//相関値計算用の'1'の位置
				userMPS[i][j] = super.mps.getMPS(2*i+jump+j);
			}
		}

		CodeSet codeSet = new CodeSet(user, userMPS);
		return codeSet;
	}

	//デコードルールの設定
	@Override
	public int decodeSetting(int no, int[] muiCode, int[][][] userMPS) {
		// TODO 自動生成されたメソッド・スタブ
		int decodeSignal = 0;
		int correlation[] = new int[super.signal.length];

		for(int i=0; i < p; i++){
			correlation[0] = correlation[0] + muiCode[userMPS[no][0][i]];
			correlation[1] = correlation[1] + muiCode[userMPS[no][1][i]];
		}

		if((correlation[1] - correlation[0]) >= 0) decodeSignal = 1;

		return decodeSignal;
	}

	//反転時のデコードルールの設定
	@Override
	public int invdecodeSetting(int no, int[] muiCode, int[][][] userMPS) {
		// TODO 自動生成されたメソッド・スタブ
		int decodeSignal = 0;
		int correlation[] = new int[super.signal.length];

		for(int i=0; i < p; i++){
			correlation[0] = correlation[0] + muiCode[userMPS[no][0][i]];
			correlation[1] = correlation[1] + muiCode[userMPS[no][1][i]];
		}

		if((correlation[0] - correlation[1] ) > 0) decodeSignal = 1;

		return decodeSignal;
	}

	@Override
	public void addInfo() {
		// TODO 自動生成されたメソッド・スタブ
	}
}