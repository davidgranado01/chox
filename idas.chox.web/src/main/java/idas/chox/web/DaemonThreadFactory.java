package idas.chox.web;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Hands out threads from the wrapped threadfactory with setDeamon(true), so the
 * threads won't keep the JVM alive when it should otherwise exit.
 */
public class DaemonThreadFactory implements ThreadFactory {

    private final ThreadFactory factory;
    static int i = 0;

    /**
     * Construct a ThreadFactory with setDeamon(true) using
     * Executors.defaultThreadFactory()
     */
    public DaemonThreadFactory() {
        this(Executors.defaultThreadFactory());
    }

    /**
     * Construct a ThreadFactory with setDeamon(true) wrapping the given factory
     * 
     * @param factory
     *            factory to wrap
     */
    public DaemonThreadFactory(ThreadFactory factory) {
        if (factory == null)
            throw new NullPointerException("factory cannot be null");
        this.factory = factory;
    }

    @Override
    public Thread newThread(Runnable r) {
        final Thread t = factory.newThread(r);
        t.setDaemon(true);
        t.setName("chox-executorThread-" + ++i);
        return t;
    }
}