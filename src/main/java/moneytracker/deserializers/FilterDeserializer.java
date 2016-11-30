package moneytracker.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import moneytracker.security.SecurityContext;
import moneytracker.model.Filter;
import moneytracker.services.FilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import java.io.IOException;

public final class FilterDeserializer extends JsonDeserializer<Filter> {

    @Autowired
    private FilterService filterService;

    @Autowired
    private SecurityContext securityContext;

    public FilterDeserializer() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @Override
    public Filter deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        try {
            return filterService.get(securityContext.getAuthenticatedUser(), jsonParser.getValueAsLong());
        } catch (IOException e) {
            throw new IOException("Unable to parse filter");
        }
    }

}
