package ninja.onewaysidewalks.utilities;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ScheduledThreadExecutor extends java.util.concurrent.ScheduledThreadPoolExecutor {

    private final boolean failOnException;

    private static ScheduledThreadExecutor DEFAULT_INSTANCE = new ScheduledThreadExecutor(1, false);

    public static ScheduledThreadExecutor getInstance() {
        return DEFAULT_INSTANCE;
    }

    public ScheduledThreadExecutor(int poolSize) {
        this(poolSize, true);
    }

    public ScheduledThreadExecutor(int poolSize, boolean failOnException) {
        super(poolSize);

        this.failOnException = failOnException;
    }

    @Override
    public ScheduledFuture scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        return super.scheduleAtFixedRate(wrapRunnable(command), initialDelay, period, unit);
    }

    @Override
    public ScheduledFuture scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        return super.scheduleWithFixedDelay(wrapRunnable(command), initialDelay, delay, unit);
    }

    private Runnable wrapRunnable(Runnable command) {
        return new LogOnExceptionRunnable(command, failOnException);
    }

    private static class LogOnExceptionRunnable implements Runnable {
        private Runnable theRunnable;
        private boolean failOnException;

        public LogOnExceptionRunnable(Runnable theRunnable, boolean failOnException) {
            super();
            this.theRunnable = theRunnable;
            this.failOnException = failOnException;
        }

        @Override
        public void run() {
            try {
                theRunnable.run();
            } catch (Exception e) {
                // LOG IT HERE!!!
                log.error("error in executing: " + theRunnable + ". If this is unintended, " +
                        "wrap the Runnable logic in a try/catch, and swallow the exception.", e);

                // and re throw it so that the Executor also gets this error so that it can do what it would
                // usually do
                if (failOnException) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
