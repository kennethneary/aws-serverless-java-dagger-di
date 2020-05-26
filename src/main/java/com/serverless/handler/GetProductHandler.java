package com.serverless.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.serverless.config.dagger.AppComponent;
import com.serverless.model.ApiGatewayResponse;
import com.serverless.model.Product;
import com.serverless.model.Response;
import com.serverless.service.ProductManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.util.Objects;

public class GetProductHandler extends BaseEventHandler {

    private static final Logger LOG = LogManager.getLogger(GetProductHandler.class);

    @Inject ProductManager productManager;

    @Override
    public void inject(final AppComponent appComponent) {
        appComponent.inject(this);
    }

    @Override
    public ApiGatewayResponse processEvent(final APIGatewayProxyRequestEvent event, final Context context) throws Exception {
        LOG.info("GetByIdHandler...");

        final String id = event.getPathParameters().get("id");
        final Product product = this.productManager.getProductById(id);

        if (Objects.isNull(product)) {
            return ApiGatewayResponse.builder().setStatusCode(404).build();
        }

        final Response response = Response.builder().data(product).build();
        return ApiGatewayResponse.builder()
                .setStatusCode(200)
                .setObjectBody(response)
                .build();
    }
}


