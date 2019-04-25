package edu.iis.mto.staticmock;

import edu.iis.mto.staticmock.reader.NewsReader;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.Whitebox;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ConfigurationLoader.class, NewsReaderFactory.class})

public class NewsLoaderTest {

    @Test
    public void shouldKeepPublicAndSubscriberNewsSeparately() {

        PublishableNews publishableNews = new PublishableNews();
        publishableNews.addPublicInfo("public_news");
        publishableNews.addForSubscription("sub_newsA", SubsciptionType.A);
        publishableNews.addForSubscription("sub_newsB", SubsciptionType.B);
        publishableNews.addForSubscription("sub_newsC", SubsciptionType.C);


        List<String> publicContent = (List<String>) Whitebox.getInternalState(publishableNews,"publicContent");
        List<String> subscribentContent = (List<String>) Whitebox.getInternalState(publishableNews,"subscribentContent");

        assertThat(publicContent.get(0), Matchers.equalTo("public_news"));
        assertThat(subscribentContent.get(0), Matchers.equalTo("sub_newsA"));
        assertThat(subscribentContent.get(1), Matchers.equalTo("sub_newsB"));
        assertThat(subscribentContent.get(2), Matchers.equalTo("sub_newsC"));

    }

    @Test
    public void shouldCallReadMethodOnce() {

        mockStatic(ConfigurationLoader.class);
        ConfigurationLoader configurationLoader = mock(ConfigurationLoader.class);
        when(ConfigurationLoader.getInstance()).thenReturn(configurationLoader);

        Configuration configuration = mock(Configuration.class);
        when(configurationLoader.loadConfiguration()).thenReturn(configuration);

        mockStatic(NewsReaderFactory.class);
        NewsReader newsReader = mock(NewsReader.class);
        when(NewsReaderFactory.getReader(configuration.getReaderType())).thenReturn(newsReader);

        IncomingNews incomingNews = mock(IncomingNews.class);
        when(newsReader.read()).thenReturn(incomingNews);

        NewsLoader newsLoader = new NewsLoader();
        newsLoader.loadNews();

        verify(newsReader, times(1)).read();

    }

}