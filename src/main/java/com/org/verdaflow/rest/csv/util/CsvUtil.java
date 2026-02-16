package com.org.verdaflow.rest.csv.util;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import org.springframework.stereotype.Component;

import com.org.verdaflow.rest.config.common.StringConst;

@Component
public class CsvUtil {
	private final char DEFAULT_SEPARATOR = ','; // NOSONAR

	public void writeLine(Writer w, List<String> values) throws IOException {
		writeLine(w, values, DEFAULT_SEPARATOR, ' ');
	}

	public void writeLine(Writer w, List<String> values, char separators) throws IOException {
		writeLine(w, values, separators, ' ');
	}

	private String followCVSformat(String value) {
		String result = value;
		if (result.contains("\"")) {
			result = result.replace("\"", "\"\"");
		}
		return result;
	}

	public void writeLine(Writer w, List<String> values, char separators, char customQuote) throws IOException {
		boolean first = true;

		// default customQuote is empty
		if (separators == ' ') {
			separators = DEFAULT_SEPARATOR;
		}

		StringBuilder sb = new StringBuilder();
		for (String value : values) {
			if (!first) {
				sb.append(separators);
			}
			if (customQuote == ' ') {
				sb.append(followCVSformat(value));
			} else {
				sb.append(customQuote).append(followCVSformat(value)).append(customQuote);
			}

			first = false;
		}
		sb.append(StringConst.NEXT_LINE);
		w.append(sb.toString());
	}
}
