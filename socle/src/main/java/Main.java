
import fr.argus.socle.thread.MainThread;

/**
 * © @author mamadou.dansoko 2016
 */
public class Main {

	public static void main(String... args) {

		String moduleName = "FORMAT_PIVOT";
		Thread mainThread = new Thread(new MainThread(moduleName));
		mainThread.setName("MainThread");
		mainThread.start();

	}

}
