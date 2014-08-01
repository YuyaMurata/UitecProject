package testpack;

public class GFtest {
	private static int[][] gf, gfmps, mps;

	public static void main(String[] args) {
		int p = 7;

		/*GF(p)の計算。 0~p-1の値をとる*/
		gf = new int[p][p];
		for(int i=0; i < p; i++)
			for(int j=0; j < p; j++)
				gf[i][j] = i * j  % p;

		toString("GF("+p+")",gf);

		/*GF(p)を並べ替えて作成されたMPS。0~p-1の値をとる*/
		gfmps = new int[p*p][p];
		for(int i=0; i < p; i++)
			for(int j=0; j < p; j++)
				for(int k=0; k < gf[i].length; k++)
					gfmps[p*i+j][k] = (gf[i][k] + j) % p;

		/*0~p-1の値をコードの'1'が立つ位置に対応させたMPS。0~p*p-1の値をとる*/
		mps = new int[p*p][p];
		for(int i=0; i < p*p; i++)
			for(int j=0; j < p; j++)
				mps[i][j] = gfmps[i][j] + j * p;

		toString("MPS("+p+")",mps);
		inValiable(mps);

	}

	/*行列の出力用*/
	private static void toString(String valName, int arr[][]){
		System.out.println();
		System.out.println(valName +":");

		for(int i=0; i < arr.length; i++){
			if(i % arr[i].length == 0) System.out.println();

			for(int j=0; j < arr[i].length; j++){
				System.out.print(arr[i][j] + " ");
			}
			System.out.println();
		}
	}

	private static void inValiable(int arr[][]){
		for(int i=0; i < arr.length; i++){
			if(i % arr[i].length == 0)  System.out.println();
			System.out.print("{");
			for(int j=0; j < arr[i].length; j++){
				System.out.print(arr[i][j]);
				if(j < arr[i].length-1) System.out.print(",");
			}
			System.out.print("},");
		}
	}
}
