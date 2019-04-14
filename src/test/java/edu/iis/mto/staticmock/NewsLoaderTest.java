package edu.iis.mto.staticmock;

import edu.iis.mto.staticmock.reader.NewsReader;
import edu.iis.mto.staticmock.reader.WebServiceNewsReader;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.Whitebox;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;

import static org.mockito.Matchers.any;

import static org.powermock.api.mockito.PowerMockito.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ConfigurationLoader.class,NewsReaderFactory.class})

public class NewsLoaderTest {

    private NewsLoader newsLoader;
    private Configuration configuration;
    private ConfigurationLoader configurationLoader;
    private NewsReader newsReader;
    private IncomingNews incomingNews;
    private PublishableNews publishableNews;

    @Before
    public void setup() {

        mockStatic(NewsReaderFactory.class);
        mockStatic(ConfigurationLoader.class);

        newsLoader = new NewsLoader();

        configurationLoader = mock(ConfigurationLoader.class);
        when(configurationLoader.getInstance()).thenReturn(configurationLoader);

        configuration = new Configuration();
        Whitebox.setInternalState(configuration, "readerType", "WS");

        when(configurationLoader.loadConfiguration()).thenReturn(configuration);

        newsReader = mock(NewsReader.class);
        when(NewsReaderFactory.getReader("WS")).thenReturn(newsReader);

        incomingNews = mock(IncomingNews.class);
        when(newsReader.read()).thenReturn(incomingNews);

        publishableNews = PublishableNews.create();
    }

    @Test public void testMethodLoadConfigurationShouldBeCalled() {

        newsLoader.loadNews();
        verify(configurationLoader).loadConfiguration();
    }

    @Test public void testMethodReadShouldBeCalled() {

        newsLoader.loadNews();
        verify(newsReader).read();
    }

    @Test public void testCheckingDivisionOnPublicNewsAndSubsciptionNews() {

        publishableNews.addPublicInfo("publicNews");
        publishableNews.addForSubscription("subsciptionNewsTypeA", SubsciptionType.A);
        publishableNews.addPublicInfo("publicNews");
        publishableNews.addForSubscription("subsciptionNewsTypeB", SubsciptionType.B);
        publishableNews.addPublicInfo("publicNews");
        publishableNews.addForSubscription("subsciptionNewsTypeC", SubsciptionType.C);

        List<String> publicContent = (List<String>)Whitebox.getInternalState(publishableNews,"publicContent");
        List<String> subscribentContent = (List<String>)Whitebox.getInternalState(publishableNews,"subscribentContent");

        assertThat(publicContent.get(0), Matchers.equalTo("publicNews"));
        assertThat(publicContent.get(1), Matchers.equalTo("publicNews"));
        assertThat(publicContent.get(2), Matchers.equalTo("publicNews"));

        assertThat(subscribentContent.get(0), Matchers.equalTo("subsciptionNewsTypeA"));
        assertThat(subscribentContent.get(1), Matchers.equalTo("subsciptionNewsTypeB"));
        assertThat(subscribentContent.get(2), Matchers.equalTo("subsciptionNewsTypeC"));
    }
}
