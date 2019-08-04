package com.infotech.batch.config;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import com.google.gson.Gson;

public class CustomItemItemWriter implements ItemWriter {
	private static final Logger logger = LoggerFactory.getLogger(CustomItemItemWriter.class);
	@Override
	public void write(List list) throws Exception {
		Gson gson = new Gson();
			if(!list.isEmpty()){
				logger.info("List is not empty :: "+list.size());
				logger.info("List is not empty :: "+gson.toJson(list));
			}else{
				logger.info("List is empty :: ");
			}
	}

}
