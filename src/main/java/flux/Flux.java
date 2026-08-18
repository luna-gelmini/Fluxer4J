package flux;

import flux.api.FluxClient;
import flux.gateway.client.RestClient;
import flux.gateway.client.impl.OkHttpRestClientImpl;
import flux.json.util.JsonEngine;
import flux.json.util.impl.JacksonJsonEngineImpl;
import okhttp3.OkHttpClient;

public final class Flux {
    static OkHttpClient sharedOkHttpClient = new OkHttpClient.Builder().build();
    RestClient restClient = new OkHttpRestClientImpl();

    private Flux() {

    }

    public static FluxClient createDefault() {
        JsonEngine jsonEngine = new JacksonJsonEngineImpl();
        RestClient restClient = new OkHttpRestClientImpl(sharedOkHttpClient);

        return new FluxClientImpl(restClient, jsonEngine, sharedOkHttpClient);
    }
}
