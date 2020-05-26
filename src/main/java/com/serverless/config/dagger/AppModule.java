package com.serverless.config.dagger;

import com.serverless.Constants;
import com.serverless.model.Product;
import com.serverless.service.DbManager;
import com.serverless.service.ObjectStorageManager;
import com.serverless.service.ProductManager;
import com.serverless.service.impl.DynamoDbManager;
import com.serverless.service.impl.ObjectStorageService;
import com.serverless.service.impl.ProductService;
import dagger.Module;
import dagger.Provides;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.BeanTableSchema;
import software.amazon.awssdk.services.s3.S3Client;

import javax.inject.Named;
import javax.inject.Singleton;

@Module
public class AppModule {

    @Provides
    @Singleton
    public ProductManager providesProductManager() {
        return new ProductService();
    }

    @Provides
    @Singleton
    public ObjectStorageManager providesObjectStorageManager() {
        return new ObjectStorageService();
    }

    @Provides
    @Singleton
    public S3Client providesS3Client() {
        return S3Client.create();
    }

    @Provides
    @Singleton
    public DbManager<Product> providesDbManagerProduct() {
        return new DynamoDbManager<>();
    }

    @Provides
    @Singleton
    public DynamoDbEnhancedClient providesDynamoDbEnhancedClient() {
        return DynamoDbEnhancedClient.create();
    }

    @Provides
    @Singleton
    public Class<Product> providesClassProduct() {
        return Product.class;
    }

    @Provides
    @Singleton
    public BeanTableSchema<Product> providesBeanTableSchemaProduct(Class<Product> classType) {
        return TableSchema.fromBean(classType);
    }

    @Provides
    @Singleton
    @Named(DaggerConstants.PRODUCTS_TABLE_NAME)
    public String providesProductsTableName() {
        return Constants.PRODUCTS_TABLE_NAME.getValue();
    }

    @Provides
    @Singleton
    public DynamoDbTable<Product> providesDynamoDbTableProduct(DynamoDbEnhancedClient ddbEnhancedClient,
                                                                 @Named(DaggerConstants.PRODUCTS_TABLE_NAME) String tableName,
                                                                 BeanTableSchema<Product> beanTableSchema) {
        return ddbEnhancedClient.table(tableName, beanTableSchema);
    }
}