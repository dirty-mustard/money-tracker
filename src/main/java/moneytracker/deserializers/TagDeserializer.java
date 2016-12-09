package moneytracker.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import moneytracker.security.SecurityContext;
import moneytracker.model.Tag;
import moneytracker.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import java.io.IOException;

public class TagDeserializer extends JsonDeserializer<Tag> {

    @Autowired
    private TagService tagService;

    @Autowired
    private SecurityContext securityContext;

    public TagDeserializer() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @Override
    public Tag deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        try {
            return tagService.get(securityContext.getAuthenticatedUser(), jsonParser.getValueAsLong());
        } catch (IOException e) {
            throw new IOException("Unable to parse tag");
        }
    }

}
