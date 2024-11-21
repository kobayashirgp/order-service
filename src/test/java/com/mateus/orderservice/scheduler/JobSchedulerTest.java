package com.mateus.orderservice.scheduler;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JobSchedulerTest {

    @InjectMocks
    private JobScheduler jobScheduler;
    @Mock
    private JobLauncher jobLauncher;
    @Mock
    private Job job;

    @Test
    void shouldCallOrderScheduler() throws JobInstanceAlreadyCompleteException,
            JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {

        Assertions.assertThatCode(() -> jobScheduler.orderScheduler())
                .doesNotThrowAnyException();

        verify(jobLauncher).run(eq(job), any(JobParameters.class));
    }

    @Test
    void shouldNotThrowErrorWhenCallOrderScheduler() throws JobInstanceAlreadyCompleteException,
            JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {

        when(jobLauncher.run(any(), any())).thenThrow(JobExecutionAlreadyRunningException.class);

        Assertions.assertThatCode(() -> jobScheduler.orderScheduler())
                .doesNotThrowAnyException();

        verify(jobLauncher).run(eq(job), any(JobParameters.class));
    }
}