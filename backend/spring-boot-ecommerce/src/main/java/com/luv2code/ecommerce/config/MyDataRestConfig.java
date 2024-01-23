package com.luv2code.ecommerce.config;

import com.luv2code.ecommerce.entity.*;

import jakarta.persistence.EntityManager;

import jakarta.persistence.metamodel.EntityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {

    @Value("${allowed.origins}")
    private String[] theAllowedOrigins;

    private EntityManager entityManager;

    @Autowired
    public MyDataRestConfig(EntityManager theEntityManager){
        entityManager = theEntityManager;
    }

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {

        HttpMethod[] theUnsuoppoertedActions = {HttpMethod.DELETE, HttpMethod.POST,
                                                HttpMethod.PUT, HttpMethod.PATCH};

        //disable Product and ProductCategory HTTP methods for PUT, Post, Delete
        disableHttpMethods(Product.class, config, theUnsuoppoertedActions);
        disableHttpMethods(ProductCategory.class, config, theUnsuoppoertedActions);
        disableHttpMethods(Country.class, config, theUnsuoppoertedActions);
        disableHttpMethods(State.class, config, theUnsuoppoertedActions);
        disableHttpMethods(Order.class, config, theUnsuoppoertedActions);



        // call an internal helper method
        exposeIds(config);

        //configure cors mapping
        cors.addMapping(config.getBasePath() + "/**").allowedOrigins(theAllowedOrigins);
    }

    private static void disableHttpMethods(Class theClass,RepositoryRestConfiguration config, HttpMethod[] theUnsuoppoertedActions) {
        config.getExposureConfiguration()
                .forDomainType(theClass)
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(theUnsuoppoertedActions))
                .withAssociationExposure((metdata, httpMethods) -> httpMethods.disable(theUnsuoppoertedActions));
    }

    private void exposeIds(RepositoryRestConfiguration config) {
        //expose entity ids

        //get a list of all entity classes
        Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();

        // creating array of entity type
        List<Class> entityClasses = new ArrayList<>();

        //get the entity types for the entities

        for (EntityType tempEntityType : entities){
            entityClasses.add(tempEntityType.getJavaType());
        }
        Class[] domainTypes = entityClasses.toArray(new Class[0]);
        config.exposeIdsFor(domainTypes);

    }
}
