package com.mateus.orderservice.job.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;

@Slf4j
public class CustomJobListener extends JobExecutionListenerSupport {

    @Override
    public void afterJob(JobExecution jobExecution) {
        var jobName = jobExecution.getJobInstance().getJobName();
        var message = jobExecution.getStatus() == BatchStatus.COMPLETED ? "completed successfully" : "failed";

        log.info("[{}] - Batch job " +  message, jobName);

        final var stepExecutions = jobExecution.getStepExecutions();

        long totalProcessed = stepExecutions.stream().mapToLong(StepExecution::getWriteCount).sum();

        long totalSkipped = stepExecutions.stream().mapToLong(StepExecution::getFilterCount).sum();

        long totalRead = stepExecutions.stream().mapToLong(StepExecution::getReadCount).sum();

        log.info("[{}] - Total read={} processed={} ignored={}", jobName, totalRead, totalProcessed, totalSkipped);
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        var jobName = jobExecution.getJobInstance().getJobName();
        log.info("[{}] - Batch job started", jobName);
    }
}
