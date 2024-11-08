package com.mateus.orderservice.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class JobScheduler {

    private final JobLauncher jobLauncher;
    private final Job orderJob;

    @Scheduled(cron = "0 * * * * *")
    public void orderScheduler() {
        log.info("Initializing job process");
        try {
            jobLauncher.run(orderJob,
                    new JobParametersBuilder()
                            .addString("processId", UUID.randomUUID().toString())
                            .toJobParameters());
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException ex) {
            log.error("Error executing job", ex);
        }

    }
}
