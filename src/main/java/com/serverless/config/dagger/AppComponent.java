package com.serverless.config.dagger;

import com.serverless.handler.*;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(CreateProductHandler createProductHandler);
    void inject(DeleteProductHandler deleteProductHandler);
    void inject(GetAllProductHandler getAllProductHandler);
    void inject(GetProductHandler getProductHandler);
    void inject(QueryProductHandler queryProductHandler);
    void inject(UpdateProductHandler updateProductHandler);
}
