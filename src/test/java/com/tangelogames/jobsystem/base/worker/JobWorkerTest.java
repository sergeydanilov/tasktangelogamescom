package com.tangelogames.jobsystem.base.worker;

import com.tangelogames.jobsystem.base.AbstractScheduledJob;
import io.vertx.core.Context;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JobWorkerTest {
    JobWorker worker;
    private AbstractScheduledJob mockAbstractScheduledJob;
    private Vertx mockVertx;
    private Context mockContext;

    @BeforeEach
    void setUp() {
        mockAbstractScheduledJob = mock(AbstractScheduledJob.class);
        when(mockAbstractScheduledJob.getDelayInMiliSec()).thenReturn(10_000L);
        worker = new JobWorker(mockAbstractScheduledJob);
        mockVertx = mock(Vertx.class);
        mockContext = mock(Context.class);
        worker.init(mockVertx, mockContext);
    }

    @Test
    void start_callStartTimer() throws Exception {
        // setup
        worker = spy(worker);
        doNothing().when(worker).startTimer(anyLong());
        Promise mockPromise = mock(Promise.class);

        // act
        worker.start(mockPromise);

        // verify
//        assertThat(result, is(2));
        verify(worker).startTimer(10_000L);
    }

    @Test
    void startTimer_callVertxSetTimer() throws Exception {
        // setup

        // ac
        worker.startTimer(10_000L);

        // verify
//        assertThat(result, is(2));
//        Handler<Long> handler =  worker::handleTimer;
//        ArgumentCaptor<Handler<Long>> captor = ArgumentCaptor.forClass(Handler.class);
//        var captured  = captor.getValue();
//        verify(mockVertx).setTimer(eq(10_000L), captor.capture());
        verify(mockVertx).setTimer(eq(10_000L), any());
    }

    @Test
    void startTimer_delayLessZero_callStartTimeout() throws Exception {
        // setup
        Vertx mockVertx = mock(Vertx.class);
        Context mockContext = mock(Context.class);
        worker.init(mockVertx, mockContext);
        worker = spy(worker);
        doNothing().when(worker).startTimeout();

        // ac
        worker.startTimer(-1);

        // verify
        verify(worker).startTimeout();
    }

    @Test
    public void startTimeout_callVertxSetTimer() {
        // setup

        // act
        worker.startTimeout();

        // verify
        verify(mockVertx).setTimer(eq(60_000L), any());
    }

    @Test
    void stop_callVertxCancelTimer() throws Exception {
        // setup
        FieldUtils.writeField(worker, "timerID", 10L, true);

        // act
        worker.stop();

        // verify
        verify(mockVertx).cancelTimer(10L);
    }

    @Test
    void handleTimer_callVertxExecuteBlocking() {
        // setup

        // act
        worker.handleTimer(100L);

        // verify
        verify(mockVertx).executeBlocking(any(), eq(false), any());
    }

    @Test
    public void handleJobExecute_call() {
        // setup
        Promise mockPromise = mock(Promise.class);

        // act
        worker.handleJobExecute(mockPromise);

        // verify
        verify(mockAbstractScheduledJob).execute();
    }
}