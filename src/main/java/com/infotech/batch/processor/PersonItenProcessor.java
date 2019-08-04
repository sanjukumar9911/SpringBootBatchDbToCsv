package com.infotech.batch.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.google.gson.Gson;
import com.infotech.batch.config.BatchConfig;
import com.infotech.batch.model.Person;

public class PersonItenProcessor implements ItemProcessor<Person, Person>{

	private static final Logger logger = LoggerFactory.getLogger(PersonItenProcessor.class);
	@Override
	public Person process(Person person) throws Exception {
		Gson gson = new Gson();
		logger.info("Person Data ::"+gson.toJson(person));
		return person;
	}
}
