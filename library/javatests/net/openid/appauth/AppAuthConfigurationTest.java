package net.openid.appauth;

import android.net.Uri;
import android.support.annotation.NonNull;

import net.openid.appauth.browser.AnyBrowserMatcher;
import net.openid.appauth.browser.BrowserMatcher;
import net.openid.appauth.browser.Browsers;
import net.openid.appauth.browser.ExactBrowserMatcher;
import net.openid.appauth.connectivity.ConnectionBuilder;
import net.openid.appauth.connectivity.DefaultConnectionBuilder;

import org.junit.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link AppAuthConfiguration}.
 */
public class AppAuthConfigurationTest {

    @Test
    public void testBuilder_defaults() {
        AppAuthConfiguration config = new AppAuthConfiguration.Builder().build();
        assertThat(config.getBrowserMatcher()).isEqualTo(AnyBrowserMatcher.INSTANCE);
        assertThat(config.getConnectionBuilder()).isEqualTo(DefaultConnectionBuilder.INSTANCE);
    }

    @Test
    public void testBuilder_customBrowserMatcher() {
        BrowserMatcher matcher = new ExactBrowserMatcher(Browsers.Chrome.customTab("55"));
        AppAuthConfiguration config = new AppAuthConfiguration.Builder()
                .setBrowserMatcher(matcher)
                .build();
        assertThat(config.getBrowserMatcher()).isSameAs(matcher);
    }

    @Test
    public void testBuilder_customConnectionBuilder() {
        ConnectionBuilder builder = new ConnectionBuilder() {
            @NonNull
            @Override
            public HttpURLConnection openConnection(@NonNull Uri uri) throws IOException {
                return (HttpURLConnection) new URL(uri.toString()).openConnection();
            }
        };

        AppAuthConfiguration config = new AppAuthConfiguration.Builder()
                .setConnectionBuilder(builder)
                .build();

        assertThat(config.getConnectionBuilder()).isSameAs(builder);
    }

    @Test(expected=NullPointerException.class)
    public void testBuilder_nullBrowserMatcher() {
        new AppAuthConfiguration.Builder().setBrowserMatcher(null);
    }

    @Test(expected=NullPointerException.class)
    public void testBuilder_nullConnectionBuilder() {
        new AppAuthConfiguration.Builder().setConnectionBuilder(null);
    }
}
