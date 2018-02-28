package thread;

public class MultiThreadEx {
	public static void main(String[] args) {
		Thread t1 = new AlphabetThread();
		Thread t2 = new Thread(new DigitThread());
		
		t1.start();
		t2.start();
	}
}
