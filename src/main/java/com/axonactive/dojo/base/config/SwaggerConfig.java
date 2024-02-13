package com.axonactive.dojo.base.config;

import io.swagger.annotations.ApiKeyAuthDefinition;
import io.swagger.annotations.SecurityDefinition;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.jaxrs.config.BeanConfig;

import javax.servlet.ServletConfig;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import java.util.ResourceBundle;

@ApplicationPath("/swagger")
@SwaggerDefinition(securityDefinition = @SecurityDefinition(
        apiKeyAuthDefinitions = {
                @ApiKeyAuthDefinition(
                        key = HttpHeaders.AUTHORIZATION,
                        name = HttpHeaders.AUTHORIZATION,
                        in = ApiKeyAuthDefinition.ApiKeyLocation.HEADER
                )
        }
))
public class SwaggerConfig extends Application {

    private static final ResourceBundle bundle = ResourceBundle.getBundle("swagger");
    private static final String APPLICATION_VERSION = bundle.getString("application.version");
    private static final String APPLICATION_HOST_DOMAIN = bundle.getString("application.host-domain");
    private static final String APPLICATION_BASE_PATH = bundle.getString("application.base-path");
    private static final String APPLICATION_RESOURCE_PACKAGE = bundle.getString("application.resource-package");
    private static final String APPLICATION_SWAGGER_TITLE = bundle.getString("application.swagger.title");
    private static final String APPLICATION_SWAGGER_DESCRIPTION = bundle.getString("application.swagger.description");

    public SwaggerConfig(@Context ServletConfig servletConfig) {
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion(APPLICATION_VERSION);
        beanConfig.setSchemes(new String[]{"http"});
        beanConfig.setHost(APPLICATION_HOST_DOMAIN);
        beanConfig.setBasePath(APPLICATION_BASE_PATH);
        beanConfig.setResourcePackage(APPLICATION_RESOURCE_PACKAGE);
        beanConfig.setTitle(APPLICATION_SWAGGER_TITLE);
        beanConfig.setDescription(APPLICATION_SWAGGER_DESCRIPTION);
        beanConfig.setScan(true);
        servletConfig.getServletName();
    }
}
