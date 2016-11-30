package moneytracker.configuration;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@ComponentScan(basePackages = {"moneytracker.services", "moneytracker.facades", "moneytracker.security"})
public class AppConfiguration {

    @Bean
    public static PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {
        PropertyPlaceholderConfigurer properties = new PropertyPlaceholderConfigurer();
        properties.setLocation(new ClassPathResource("config.properties"));

        return properties;
    }

}
