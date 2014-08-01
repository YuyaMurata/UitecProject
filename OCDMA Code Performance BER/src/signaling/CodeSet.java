package signaling;

public class CodeSet {
	int[][][] user;
	int[][][] userMPS;

	public CodeSet(int[][][] user, int[][][] userMPS) {
		// TODO 自動生成されたコンストラクター・スタブ
		this.user = user;
		this.userMPS = userMPS;
	}

	public String toString(){
		StringBuilder sb = new StringBuilder();

		for(int i=0; i < user.length; i++){
			sb.append("User");
			sb.append(i);
			sb.append(" ");
			sb.append("符号 ");
			sb.append("符号語 \n");
			for(int j=0; j < user[i].length; j++){
				sb.append("'");
				sb.append(j);
				sb.append("':");
				for(int k=0; k < user[i][j].length; k++){
					sb.append(user[i][j][k]);
					if(k % userMPS[i][j].length == 0) sb.append(" ");
				}
				sb.append("\n");
			}
			sb.append("\n");
		}

		return sb.toString();
	}
}
