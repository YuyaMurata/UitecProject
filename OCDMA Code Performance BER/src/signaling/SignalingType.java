package signaling;

import code.MPSC;


public abstract class SignalingType {
	public final int signal[] = {0, 1};
	public String name;
	public SignalingType(String name) {
		// TODO 自動生成されたコンストラクター・スタブ
		this.name = name;
	}

	//最大ユーザー数の設定
	public int Nmax;
	public abstract void setNmax(int p);

	//ユーザー符号語とMPSの格納
	public int[][][] user;
	public int[][][] userMPS;

	public static int p;
	public static int codelength;
	public MPSC mps;

	public void setValue(int p){
		this.p = p;
		this.mps = new MPSC(p);
		setNmax(p);

		//ユーザー符号語とMPSが格納されたクラス
		CodeSet codeSet = (CodeSet)encodeSetting(user, userMPS);
		this.user = codeSet.user;
		this.userMPS = codeSet.userMPS;
	}

	//エンコードルールの設定
	public abstract Object encodeSetting(int[][][] user, int[][][] userMPS);
	public  int[] encode(int no, int signal){
		return user[no][signal].clone();
	};

	//デコードルールの設定
	public abstract int decodeSetting(int no, int[] muiCode, int[][][] userMPS);
	public int decode(int[] muiCode, int no){
		int decodeSignal = decodeSetting(no, muiCode, userMPS);
		return decodeSignal;
	};

	//反転時のデコードルールの設定
	public abstract int invdecodeSetting(int no, int[] muiCode, int[][][] userMPS);
	public int invdecode(int[] muiCode, int no){
		int decodeSignal = invdecodeSetting(no, muiCode, userMPS);
		return decodeSignal;
	};

	//符号語の反転
	public static int[] inverse(int[] code){
		int[] invCode = new int[code.length];

		for(int i=0; i < code.length; i++)
			invCode[i] = (code[i] + 1) % 2;

		return invCode;
	}

	//符号語の初期化(全ゼロ系列の生成)
	public static int[] initCode(){
		int[] code = new int[codelength];
		for(int i=0; i < code.length; i++)
			code[i] = 0;
		return code;
	}

	//以下 出力用のメソッド
	public void outputCodes(){
		for(int i=0; i < user.length; i++){
			System.out.println("User:"+i+" 送信符号");
			for(int j=0; j < user[i].length; j++){
				System.out.println("'"+j+"' "+toString(user[i][j]));
			}
		}
		addInfo();
	}

	public void outputInvCodes(){
		for(int i=0; i < user.length; i++){
			System.out.println("User:"+i+" 送信符号");
			for(int j=0; j < user[i].length; j++){
				System.out.println("'"+j+"' "+toString(inverse(user[i][j])));
			}
		}
		addInfo();
	}

	//出力情報への追加
	public abstract void addInfo();

	public static String toString(int[] code){
		StringBuilder str = new StringBuilder();
		for(int i=0; i < code.length; i++){
			if(i % p == 0) str.append(" ");
			str.append(code[i]);
		}
		return str.toString();
	}
}
