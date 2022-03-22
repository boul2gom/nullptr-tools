package io.github.yggdrasil80.yggtools.docker.utils;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.model.ResponseItem;
import org.slf4j.Logger;

import java.io.Closeable;

public abstract class DockerCallbackCreator {

    protected static <R extends ResponseItem> ResultCallback<R> create(Logger logger, String start, String error, String complete) {
        return new ResultCallback.Adapter<R>() {
            @Override
            public void onStart(Closeable stream) {
                super.onStart(stream);

                logger.info(start);
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);

                logger.error(error, throwable);
            }

            @Override
            public void onComplete() {
                super.onComplete();

                logger.info(complete);
            }
        };
    }
}
