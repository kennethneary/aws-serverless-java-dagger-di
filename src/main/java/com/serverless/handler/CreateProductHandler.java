package com.serverless.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.serverless.config.dagger.AppComponent;
import com.serverless.model.ApiGatewayResponse;
import com.serverless.model.Product;
import com.serverless.model.Response;
import com.serverless.service.ProductManager;
import com.serverless.util.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException;
import software.amazon.awssdk.utils.ImmutableMap;

import javax.inject.Inject;
import java.util.Map;

public class CreateProductHandler extends BaseEventHandler {

    private static final Logger LOG = LogManager.getLogger(CreateProductHandler.class);

    @Inject ProductManager productManager;

    @Override
    public void inject(final AppComponent appComponent) {
        appComponent.inject(this);
    }

    @Override
    public ApiGatewayResponse processEvent(final APIGatewayProxyRequestEvent event, final Context context) {
        LOG.info("CreateHandler...");

        try {
            final Product product = Utils.getObject(event.getBody(), Product.class);
            final String id = this.productManager.saveProduct(product);
            final Map<String, Object> data = ImmutableMap.<String, Object>builder()
                    .put("id", id)
                    .build();
            final Response response = Response.builder().data(data).build();
            return ApiGatewayResponse.builder()
                    .setStatusCode(201)
                    .setObjectBody(response)
                    .build();
        } catch (ConditionalCheckFailedException ccfe) {
            LOG.info("CreateHandler... ConditionalCheckFailedException");
            return ApiGatewayResponse.builder().setStatusCode(409).build();
        }
    }
}


