package moneytracker.configuration;

import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.InetAddress;

@Configuration
public class IndexingConfiguration {

    @Value("${elasticsearch.hostname}")
    private String hostname;

    @Value("${elasticsearch.port}")
    private short port;

    @Bean
    public Client client() throws IOException {

        Client client = TransportClient.builder().build()
            .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(hostname), port));

        IndicesExistsResponse existsResponse = client.admin().indices()
            .prepareExists("transactions").get();

        if (!existsResponse.isExists()) {
            client.admin().indices().prepareCreate("transactions")
                .addMapping("transaction", XContentFactory.jsonBuilder()
                    .startObject()
                        .startObject("transaction")
                            .startObject("properties")
                                .startObject("date")
                                    .field("type", "date")
                                    .field("format", "strict_date_optional_time||epoch_millis")
                                .endObject()
                                .startObject("amount")
                                    .field("type", "double")
                                .endObject()
                                .startObject("name")
                                    .field("type", "string")
                                .endObject()
                                .startObject("description")
                                    .field("type", "string")
                                .endObject()
                                .startObject("accountHolder")
                                    .field("type", "string")
                                .endObject()
                                .startObject("account")
                                    .field("type", "string")
                                .endObject()
                                .startObject("offsetAccount")
                                    .field("type", "string")
                                .endObject()
                                .startObject("tags")
                                    .field("type", "nested")
                                    .startObject("properties")
                                        .startObject("id")
                                            .field("type", "long")
                                        .endObject()
                                        .startObject("name")
                                            .field("type", "string")
                                        .endObject()
                                    .endObject()
                                .endObject()
                                .startObject("locked")
                                    .field("type", "boolean")
                                .endObject()
                                .startObject("archived")
                                    .field("type", "boolean")
                                .endObject()
                                .startObject("owner")
                                    .field("type", "long")
                                .endObject()
                            .endObject()
                        .endObject()
                    .endObject()
                ).get();
        }

        return client;
    }

}
