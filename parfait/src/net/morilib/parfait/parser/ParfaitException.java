package net.morilib.parfait.parser;

import java.io.PrintStream;
import java.util.ResourceBundle;

/**
 * 
 */
public class ParfaitException extends RuntimeException {

	//
	private static final String CLSNAME;
	private static ResourceBundle msgs;

	//
	static {
		String p;

		p = ParfaitException.class.getPackage().getName();
		CLSNAME = p.replace('.', '/');
		msgs = ResourceBundle.getBundle(CLSNAME + "/messages");
	}

	//
	private Object[] args;

	/**
	 * 
	 * @param message
	 * @param args
	 */
	public ParfaitException(String message, Object... args) {
		super(message);
		this.args = args;
	}

	/**
	 * 
	 * @param p
	 * @param a
	 */
	public void perror(PrintStream out) {
		out.print(msgs.getString("errorheader"));
		out.format(msgs.getString(getMessage()), args);
		out.println();
	}

	/**
	 * 
	 * @param p
	 * @param a
	 */
	public void pwarn(PrintStream out) {
		out.print(msgs.getString("warnheader"));
		out.format(msgs.getString(getMessage()), args);
		out.println();
	}

	/**
	 * 
	 * @param out
	 * @param ver
	 */
	public static void usage(PrintStream out, String ver) {
		int l = Integer.parseInt(msgs.getString("usage.length"));
		String x;

		out.printf("title", ver);
		for(int i = 1; i <= l; i++) {
			x = String.format("%03d", i);
			out.println("usage." + x);
		}
	}

}
