package moneytracker;

import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.sql.DataSource;

@SpringBootApplication
@EnableTransactionManagement
public class Application implements ApplicationRunner {

    public static void main(final String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

    private DataSource dataSource;
    private Client elasticsearch;

    @Autowired
    public Application(final DataSource dataSource, final Client elasticsearch) {
        this.dataSource = dataSource;
        this.elasticsearch = elasticsearch;
    }

    @Override
    public void run(final ApplicationArguments applicationArguments) throws Exception {
        final ResourceDatabasePopulator dbPopulator = new ResourceDatabasePopulator();
        dbPopulator.addScript(new ClassPathResource("destroy.sql"));
        dbPopulator.addScript(new ClassPathResource("init.sql"));
        dbPopulator.addScript(new ClassPathResource("data.sql"));
        dbPopulator.execute(dataSource);

        final IndicesExistsResponse existsResponse = elasticsearch.admin()
                .indices()
                .prepareExists("transactions")
                .get();

        if (!existsResponse.isExists()) {
            final Resource mappingResource = new ClassPathResource("es-mapping.json");
            final String mapping = new String(FileCopyUtils.copyToByteArray(mappingResource.getInputStream()));
            elasticsearch.admin()
                    .indices()
                    .prepareCreate("transactions")
                    .addMapping("transaction", mapping)
                    .get();
        }
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(final CorsRegistry registry) {
                registry.addMapping("/api/**").allowedHeaders("*").allowedMethods("*").allowedOrigins("*");
                registry.addMapping("/import").allowedHeaders("*").allowedMethods("*").allowedOrigins("*");
            }
        };
    }

}
