package ninja.onewaysidewalks.riotapi.guice;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import ninja.onewaysidewalks.riotapi.RiotClient;
import ninja.onewaysidewalks.riotapi.RiotClientImpl;

public class RiotModule extends AbstractModule {
    @Override
    protected void configure() {
        //Configure object mapper for riot api
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        bind(RiotClient.class).toInstance(new RiotClientImpl(objectMapper));
    }
}
