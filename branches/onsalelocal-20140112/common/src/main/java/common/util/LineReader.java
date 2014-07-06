package common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

public class LineReader {
	public static interface LineProcessor {
		void process(String line);
	}
	
	private BufferedReader br;
	
	public LineReader(InputStream stream) {
		br = new BufferedReader(new InputStreamReader(stream));
    }
	
	public LineReader(Reader stream) {
		br = new BufferedReader(stream);
    }
	
	public LineReader(String content) {
		br = new BufferedReader(new StringReader(content));
    }
	
	public void start(LineProcessor processor) throws IOException {
		while(true) {
			String line = br.readLine();
			if(line == null)
				break;
			processor.process(line);
		}
		br.close();
	}
}
