package flux.rest;

import flux.gateway.client.RestClient;
import flux.rest.api.*;

public final class FluxRest {
    public final AuthApi auth;
    public final BillingApi billing;
    public final ChannelsApi channels;
    public final ConnectionsApi connections;
    public final DebugApi debug;
    public final DiscoveryApi discovery;
    public final DonationsApi donations;
    public final EmojisApi emojis;
    public final GatewayApi gateway;
    public final GeolocationApi geolocation;
    public final GiftsApi gifts;
    public final GuildsApi guilds;
    public final InstanceApi instance;
    public final InvitesApi invites;
    public final Oauth2Api oauth2;
    public final PacksApi packs;
    public final PremiumApi premium;
    public final ReadStatesApi readStates;
    public final ReportsApi reports;
    public final SavedMediaApi savedMedia;
    public final SearchApi search;
    public final StickersApi stickers;
    public final ThemesApi themes;
    public final UsersApi users;
    public final VoiceApi voice;
    public final WebhooksApi webhooks;

    public FluxRest(RestClient http) {
        this.auth = new AuthApi(http);
        this.billing = new BillingApi(http);
        this.channels = new ChannelsApi(http);
        this.connections = new ConnectionsApi(http);
        this.debug = new DebugApi(http);
        this.discovery = new DiscoveryApi(http);
        this.donations = new DonationsApi(http);
        this.emojis = new EmojisApi(http);
        this.gateway = new GatewayApi(http);
        this.geolocation = new GeolocationApi(http);
        this.gifts = new GiftsApi(http);
        this.guilds = new GuildsApi(http);
        this.instance = new InstanceApi(http);
        this.invites = new InvitesApi(http);
        this.oauth2 = new Oauth2Api(http);
        this.packs = new PacksApi(http);
        this.premium = new PremiumApi(http);
        this.readStates = new ReadStatesApi(http);
        this.reports = new ReportsApi(http);
        this.savedMedia = new SavedMediaApi(http);
        this.search = new SearchApi(http);
        this.stickers = new StickersApi(http);
        this.themes = new ThemesApi(http);
        this.users = new UsersApi(http);
        this.voice = new VoiceApi(http);
        this.webhooks = new WebhooksApi(http);
    }
}
