package com.axonactive.dojo.base.elastic_search;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.axonactive.dojo.base.config.JacksonConfig;
import lombok.Getter;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

import javax.annotation.PostConstruct;
import javax.ejb.DependsOn;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.ws.rs.ext.Provider;

@Startup
@Provider
@DependsOn({"JacksonConfig"})
public class ElasticSearchService {
    private final String urlServer = "http://localhost:9200";

    @Getter
    private ElasticsearchClient client;

    @Inject
    private JacksonConfig jacksonConfig;

    void init() {
        RestClient restClient = RestClient.builder(HttpHost.create(urlServer)).build();
        ElasticsearchTransport elasticsearchTransport = new RestClientTransport(restClient,
                new JacksonJsonpMapper(jacksonConfig.getObjectMapper())
        );
        client = new ElasticsearchClient(elasticsearchTransport);
    }

    public boolean getElasticSearchConnection() {
        try {
            return client.ping().value();
        } catch (Exception e) {
            return false;
        }
    }
}
