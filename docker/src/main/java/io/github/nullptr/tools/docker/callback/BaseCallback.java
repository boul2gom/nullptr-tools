package io.github.nullptr.tools.docker.callback;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.async.ResultCallbackTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;

/**
 * Base class for all callbacks.
 * @param <Callback> The type of the callback.
 * @param <Result> The type of the result.
 */
public abstract class BaseCallback<Callback extends ResultCallback<Result>, Result> extends ResultCallbackTemplate<Callback, Result> {

    /**
     * The logger.
     */
    protected final Logger logger;

    /**
     * The message to use when starting the operation.
     */
    protected String startMessage;
    /**
     * The message to use when an error occurs.
     */
    protected String errorMessage;
    /**
     * The message to use when finishing the operation.
     */
    protected String finishMessage;

    /**
     * The last result.
     */
    protected Result lastResult;
    /**
     * The list of results.
     */
    protected List<Result> results;

    /**
     * The base callback constructor.
     * @param logger The name of the logger to use.
     * @param startMessage The message to use when starting the operation.
     * @param errorMessage The message to use when an error occurs.
     * @param finishMessage The message to use when finishing the operation.
     */
    public BaseCallback(String logger, String startMessage, String errorMessage, String finishMessage) {
        this.logger = LoggerFactory.getLogger(logger);
        this.startMessage = startMessage;
        this.errorMessage = errorMessage;
        this.finishMessage = finishMessage;
        this.results = new ArrayList<>();
    }

    /**
     * The method called when the operation is started.
     * @param stream The stream of the operation.
     */
    @Override
    public void onStart(Closeable stream) {
        super.onStart(stream);

        this.logger.info(this.startMessage + "...");
    }

    /**
     * The method called when an error occurs.
     * @param throwable The throwable that occurred.
     */
    @Override
    public void onError(Throwable throwable) {
        super.onError(throwable);

        this.logger.error("Failed to " + this.errorMessage + " !", throwable);
    }

    /**
     * The method called when the operation is finished.
     */
    @Override
    public void onComplete() {
        super.onComplete();

        this.logger.info("Successfully " + this.finishMessage + ".");
    }

    /**
     * The method called when a result is received.
     * @param object The result.
     */
    @Override
    public void onNext(Result object) {
        this.lastResult = object;
        this.results.add(object);
    }

    /**
     * Get the last result.
     * @return The last result.
     */
    public Result getLastResult() {
        return this.lastResult;
    }

    /**
     * Get the list of results.
     * @return The list of results.
     */
    public List<Result> getResults() {
        return this.results;
    }
}
