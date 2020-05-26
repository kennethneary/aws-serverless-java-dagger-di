package com.serverless.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.serverless.config.dagger.Injector;
import com.serverless.model.ApiGatewayResponse;
import com.serverless.service.ProductManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException;

import javax.inject.Inject;

public class DeleteProductHandler extends BaseEventHandler {

    private static final Logger LOG = LogManager.getLogger(DeleteProductHandler.class);

    @Inject
    public ProductManager productManager;

    @Override
    public ApiGatewayResponse processEvent(final APIGatewayProxyRequestEvent event, final Context context) {
        LOG.info("DeleteHandler...");
        Injector.getInjector().inject(this);

        try {
            final String id = event.getPathParameters().get("id");
            this.productManager.deleteProductById(id);
            return ApiGatewayResponse.builder()
                    .setStatusCode(200)
                    .build();
        } catch (ConditionalCheckFailedException ccfe) {
            LOG.info("DeleteHandler... ConditionalCheckFailedException");
            return ApiGatewayResponse.builder().setStatusCode(404).build();
        }
    }
}


