package com.org.verdaflow.rest.csv.transform;

import java.io.IOException;
import java.util.List;

import org.milyn.Smooks;
import org.milyn.SmooksException;
import org.milyn.container.ExecutionContext;
import org.milyn.csv.CSVRecordParserConfigurator;
import org.milyn.flatfile.Binding;
import org.milyn.flatfile.BindingType;
import org.milyn.payload.JavaResult;
import org.milyn.payload.StringSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import com.org.verdaflow.rest.csv.util.CsvUtil;

@Service
public class CsvTransformService {

	private static final Logger logger = LoggerFactory.getLogger(CsvTransformService.class);

	@Autowired
	private Environment env;
	@Autowired
	private CsvUtil csvUtil;

	@SuppressWarnings("rawtypes")
	private List runSmooksTransformFromCsvToJava(String beanId, Class beanClass, String fields, String messageIn)
			throws IOException, SAXException, SmooksException {
		Smooks smooks = new Smooks();

		try {
			// Here's the configuration... configuring the CSV reader and the direct
			// binding config to create a List of Person objects (List<Person>)...
			smooks.setReaderConfig(new CSVRecordParserConfigurator(fields)
					.setBinding(new Binding(beanId, beanClass, BindingType.LIST)));

			// Configure the execution context to generate a report...
			ExecutionContext executionContext = smooks.createExecutionContext();

			JavaResult javaResult = new JavaResult();
			smooks.filterSource(executionContext, new StringSource(messageIn), javaResult);

			return (List) javaResult.getBean(beanId);
		} finally {
			smooks.close();
		}
	}

}
