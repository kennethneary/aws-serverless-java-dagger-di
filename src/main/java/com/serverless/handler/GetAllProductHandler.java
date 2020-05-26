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
import java.util.List;

public class GetAllProductHandler extends BaseEventHandler {

    private static final Logger LOG = LogManager.getLogger(GetAllProductHandler.class);

    @Inject ProductManager productManager;

    @Override
    public void inject(final AppComponent appComponent) {
        appComponent.inject(this);
    }

    @Override
    public ApiGatewayResponse processEvent(final APIGatewayProxyRequestEvent event, final Context context) {
        LOG.info("GetAllHandler...");

        final List<Product> products = this.productManager.getAllProducts();
        final Response response = Response.builder().data(products).build();
        return ApiGatewayResponse.builder()
                .setStatusCode(200)
                .setObjectBody(response)
                .build();
    }
}


