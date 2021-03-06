package com.serverless.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.serverless.config.dagger.AppComponent;
import com.serverless.config.dagger.Injector;
import com.serverless.model.ApiGatewayResponse;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.amazon.awssdk.services.s3.model.S3Exception;

public abstract class BaseEventHandler implements RequestHandler<APIGatewayProxyRequestEvent, ApiGatewayResponse> {

    private static final Logger LOG = LogManager.getLogger(BaseEventHandler.class);

    public abstract ApiGatewayResponse processEvent(final APIGatewayProxyRequestEvent event, final Context context) throws Exception;

    public abstract void inject(final AppComponent appComponent);

    @Override
    public ApiGatewayResponse handleRequest(final APIGatewayProxyRequestEvent event, final Context context) {
        try {
            LOG.error("Processing Event...");
            this.inject(Injector.getInjector());
            return this.processEvent(event, context);
        }
        catch (S3Exception s3e) {
            LOG.error("S3Exception {}", s3e);
            if (s3e.statusCode() == 404) {
                return ApiGatewayResponse.builder().setStatusCode(404).build();
            }
            throw s3e;
        }
        catch (Exception ex) {
            LOG.error("Exception: {}", ex);
            LOG.error("Exception trace: {}", ExceptionUtils.getStackTrace(ex));
            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .build();
        }
    }
}
