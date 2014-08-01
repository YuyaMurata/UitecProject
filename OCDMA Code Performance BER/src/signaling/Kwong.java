package signaling;

public class Kwong extends SignalingType{
	public Kwong() {
		// TODO 自動生成されたコンストラクター・スタブ
		super("KWONG");
	}

	@Override
	public void setNmax(int p) {
		// TODO 自動生成されたメソッド・スタブ
		super.Nmax = p*p;
		super.codelength = p*p;
	}

	@Override
	public Object encodeSetting(int[][][] user, int[][][] userMPS) {
		// TODO 自動生成されたメソッド・スタブ
		//ユーザー符号語
		user = new int[Nmax][super.signal.length][super.codelength];
		userMPS = new int[Nmax][super.signal.length][p];

		//初期化コード
		int[] code0 = super.initCode();

		for(int i=0; i < Nmax; i++){
			//符号語
			user[i][1] = super.mps.getMPSC(i);
			user[i][0] = code0;

			//相関値計算用の'1'の位置
			userMPS[i][1] = super.mps.getMPS(i);
		}

		CodeSet codeSet = new CodeSet(user, userMPS);
		return codeSet;
	}

	@Override
	public int decodeSetting(int no, int[] muiCode, int[][][] userNoMPS) {
		// TODO 自動生成されたメソッド・スタブ
		int decodeSignal = 0;
		int correlation[] = new int[super.signal.length];

		if(withOHL) OHL(muiCode);

		for(int i=0; i < p; i++){
			correlation[1] = correlation[1] + muiCode[userMPS[no][1][i]];
		}

		if(correlation[1] >= p) decodeSignal = 1;

		return decodeSignal;
	}

	//OHL
	private static void OHL(int[] muiCode){
		for(int i=0; i < muiCode.length; i++)
			if(1 < muiCode[i]) muiCode[i] = 1;
	}

	//Kwong with OHL (true)
	private boolean withOHL;
	public Kwong(boolean withOHL) {
		// TODO 自動生成されたコンストラクター・スタブ
		super("KWONG+OHL");
		this.withOHL = withOHL;
	}

	@Override
	public int invdecodeSetting(int no, int[] muiCode, int[][][] userMPS) {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public void addInfo() {
		// TODO 自動生成されたメソッド・スタブ
	}
}
