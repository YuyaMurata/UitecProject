package main;

import java.util.ArrayList;

import org.spaceroots.mantissa.random.MersenneTwister;

public class UserSelector {
	private static final UserSelector selector;
	private static MersenneTwister mt = new MersenneTwister();

	//Singleton
	static {
		selector = new UserSelector();
	}

	public static UserSelector getInstance(){
		return selector;
	}

	public UserSelector() {
		// TODO 自動生成されたコンストラクター・スタブ
	}

	private static int nmax;
	private static  ArrayList<Integer> userList = new ArrayList<>();
	public void setMaxUser(int nmax){
		this.nmax = nmax;

		for(int i=0; i < nmax; i++)
			userList.add(i);
	}

	public ArrayList<Integer> selectSendUser(int n){
		ArrayList<Integer> sendUser = new ArrayList<>();
		ArrayList<Integer> sendUserList = (ArrayList<Integer>) userList.clone();

		while(sendUser.size() != n){
			int i = mt.nextInt(sendUserList.size());
			sendUser.add(sendUserList.get(i));
			sendUserList.remove(i);
		}

		return sendUser;
	}

	public ArrayList<Integer> avgselectSendUser(int n){
		ArrayList<Integer> sendUser = new ArrayList<>();
		ArrayList<Integer> sendUserList = (ArrayList<Integer>) userList.clone();

		for(Integer user : sendUserList)
			if(mt.nextDouble() <= (double)n / (double)nmax) sendUser.add(user);

		return sendUser;
	}

	public void fixSendUser(int no, ArrayList<Integer> sendUser){
		if(sendUser.isEmpty()) sendUser.add(no);
		else if(!sendUser.contains((Integer) no)) sendUser.set(0, no);
	}

	/*Test
	public static void main(String[] args) {
		UserSelector selector = new UserSelector();
		selector.setMaxUser(10);

		for(int i=0; i< 10; i++){
			System.out.println(selector.avgselectSendUser(4).toString());
		}
	}
	*/
}
