package com.serverless.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.serverless.config.dagger.AppComponent;
import com.serverless.model.ApiGatewayResponse;
import com.serverless.model.Product;
import com.serverless.service.ProductManager;
import com.serverless.util.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException;

import javax.inject.Inject;

public class UpdateProductHandler extends BaseEventHandler {

    private static final Logger LOG = LogManager.getLogger(UpdateProductHandler.class);

    @Inject ProductManager productManager;

    @Override
    public void inject(final AppComponent appComponent) {
        appComponent.inject(this);
    }

    @Override
    public ApiGatewayResponse processEvent(final APIGatewayProxyRequestEvent event, final Context context) {
        LOG.info("UpdateHandler...");

        try {
            final String id = event.getPathParameters().get("id");
            final Product product = Utils.getObject(event.getBody(), Product.class);
            this.productManager.updateProduct(id, product);
            return ApiGatewayResponse.builder()
                    .setStatusCode(200)
                    .build();
        } catch (ConditionalCheckFailedException ccfe) {
            LOG.info("UpdateHandler... ConditionalCheckFailedException");
            return ApiGatewayResponse.builder().setStatusCode(404).build();
        }
    }
}


