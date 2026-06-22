package base;

public class QuitBot {

	/*
	 * checking bot status terminate or running
	 */
	private static QuitBot bot = null;
	public boolean quit;

	private QuitBot() {

	}

	public boolean isQuit() {
		return quit;
	}

	public void setQuit(boolean quit) {
		this.quit = quit;
	}

	public static QuitBot getObj() {

		if (bot == null) {
			bot = new QuitBot();
		}
		return bot;

	}
}