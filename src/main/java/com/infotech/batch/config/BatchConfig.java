package com.infotech.batch.config;



import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import com.infotech.batch.model.Person;
import com.infotech.batch.processor.PersonItenProcessor;

@Configuration
@EnableBatchProcessing
public class BatchConfig {
	private static final Logger logger = LoggerFactory.getLogger(BatchConfig.class);
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private DataSource dataSource;
	
	@Bean
	public JdbcCursorItemReader<Person> reader(){
		logger.info("READER START ");
		JdbcCursorItemReader<Person> cursorItemReader = new JdbcCursorItemReader<>();
		cursorItemReader.setDataSource(dataSource);
		cursorItemReader.setSql("SELECT person_id,first_name,last_name,email,age FROM person");
		cursorItemReader.setRowMapper(new PersonRowMapper());
		logger.info("READER END ");
		return cursorItemReader;
	}
	
	@Bean
	public PersonItenProcessor processor(){
		logger.info("Processor ");
		return new PersonItenProcessor();
	}
	
	@Bean
	public ItemWriter writer() {
	    return new CustomItemItemWriter();
	}
	
	
	@Bean
	public Step step1(){
		return stepBuilderFactory.get("step1").<Person,Person>chunk(1).reader(reader()).processor(processor()).writer(writer()).build();
	}

	@Bean
	public Step step2(){
		return stepBuilderFactory.get("step2").<Person,Person>chunk(2).reader(reader()).processor(processor()).writer(writer()).build();
	}

	@Bean
	public Step step3(){
		return stepBuilderFactory.get("step3").<Person,Person>chunk(7).reader(reader()).processor(processor()).writer(writer()).build();
	}

/*	 private TaskletStep taskletStep(String step) {
	        return stepBuilderFactory.get(step).tasklet((contribution, chunkContext) -> {
//	            IntStream.range(1, 2).forEach(token -> logger.info("Step:" + step + " token:" + token));
	            return RepeatStatus.FINISHED;
	        }).build();
	    }
	 
	 @Bean
	    public Job parallelStepsJob() {
	 
	        Flow masterFlow = (Flow) new FlowBuilder("masterFlow").start(taskletStep("step1")).build();
	 
	        Flow flowJob1 = (Flow) new FlowBuilder("flow1").start(taskletStep("step2")).build();
	        
	        Flow flowJob2 = (Flow) new FlowBuilder("flow1").start(taskletStep("step3")).build();
	 
	        Flow slaveFlow = (Flow) new FlowBuilder("slaveFlow")
	                .split(new SimpleAsyncTaskExecutor()).add(flowJob1,flowJob2).build();
	 
	        return (jobBuilderFactory.get("exportPeronJob")
	                .incrementer(new RunIdIncrementer())
	                .start(masterFlow)
	                .next(slaveFlow)
	                .build()).build();
	 
	    }*/
	
/*	@Bean
	public Job exportPerosnJob(){
		return jobBuilderFactory.get("exportPeronJob").incrementer(new RunIdIncrementer()).flow(step1()).end().build();
	}
*/
	@Bean
	public Job exportPerosnJob(){
		return jobBuilderFactory.get("exportPeronJob").incrementer(new RunIdIncrementer()).start(step1()).next(step2()).next(step3()).build();
	}
	
}
