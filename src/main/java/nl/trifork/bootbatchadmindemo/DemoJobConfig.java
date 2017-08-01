package nl.trifork.bootbatchadmindemo;

import static java.util.Arrays.asList;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DemoJobConfig {

    @Autowired JobBuilderFactory jobBuilderFactory;
    @Autowired StepBuilderFactory stepBuilderFactory;

    @Bean
    Job simpleJob() {
        return jobBuilderFactory.get("demo job")
                .start(simpleStep())
                .build();
    }

    Step simpleStep() {
        return stepBuilderFactory.get("demo step")
                .chunk(1)
                .reader(new ListItemReader(asList("foo", "bar")))
                .writer(list -> list.forEach(System.out::println))
                .build();
    }
}
