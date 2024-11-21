package com.mateus.orderservice.job.config;

import com.mateus.orderservice.enums.OrderStatus;
import com.mateus.orderservice.model.Order;
import com.mateus.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Collections;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class JobOrderConfig {

    private final ItemProcessor<Order, Order> processor;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final OrderRepository orderRepository;

    @Bean
    public Job job(Step step) {
        return new JobBuilder("orderJob", jobRepository)
                .preventRestart()
                .incrementer(new RunIdIncrementer())
                .listener(new CustomJobListener())
                .flow(step)
                .end()
                .build();
    }

    @Bean
    public Step step(ItemReader<Order> reader, ItemWriter<Order> writer) {
        return new StepBuilder("orderStep", jobRepository)
                .<Order, Order>chunk(10, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public ItemReader<Order> orderReader() {
        return new RepositoryItemReaderBuilder()
                .repository(orderRepository)
                .name("reader")
                .arguments(OrderStatus.RECEIVED)
                .methodName("findAllByOrderStatus")
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .build();
    }

    @Bean
    public ItemWriter<Order> orderWriter() {
        RepositoryItemWriter orderWriter = new RepositoryItemWriterBuilder()
                .methodName("save")
                .repository(orderRepository)
                .build();
        return orderWriter;
    }


}
