package io.github.juanjalvarez.socialnetwork;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoggingStream extends OutputStream {

	public static final String OUTPUT_DIRECTORY = "output/";
	public static final File OUTPUT_FILE = new File(OUTPUT_DIRECTORY + "output_" + getDateAndTimePrefix() + ".txt");

	static {
		File outputDir = new File(OUTPUT_DIRECTORY);
		if (!outputDir.exists())
			outputDir.mkdir();
	}

	private OutputStream stdout;
	private FileOutputStream fos;
	private boolean newline;

	public LoggingStream() {
		stdout = System.out;
		try {
			fos = new FileOutputStream(OUTPUT_FILE);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		newline = true;
	}

	@Override
	public void write(int b) throws IOException {
		if ((char) b == '\n') {
			stdout.write(b);
			fos.write(b);
			newline = true;
		} else {
			if (newline) {
				newline = false;
				byte[] data = ("[" + getDateAndTimePrefix() + "] -> ").getBytes();
				stdout.write(data);
				fos.write(data);
			}
			stdout.write(b);
			fos.write(b);
		}
	}

	public static String getDateAndTimePrefix() {
		Date d = new Date();
		DateFormat df = new SimpleDateFormat("MM-dd-yy_HH-mm-ss");
		return df.format(d);
	}
}