package edu.iis.mto.staticmock;

import edu.iis.mto.staticmock.reader.NewsReader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.Whitebox;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ConfigurationLoader.class, NewsReaderFactory.class})
public class NewsLoaderTest {

    private ConfigurationLoader configurationLoader;
    private NewsReaderFactory newsReaderFactory;
    private Configuration configuration;
    private IncomingNews incomingNews;
    private NewsLoader newsLoader;
    private NewsReader newsReader;

    private void addIncomingInfoToIncomingNews() {
        incomingNews.add(new IncomingInfo("subscriberContentA", SubsciptionType.A));
        incomingNews.add(new IncomingInfo("subscriberContentB", SubsciptionType.B));
        incomingNews.add(new IncomingInfo("subscriberContentC", SubsciptionType.C));
        incomingNews.add(new IncomingInfo("publicContent", SubsciptionType.NONE));
    }

    @Before
    public void setUp() {
        mockStatic(NewsReaderFactory.class);
        mockStatic(ConfigurationLoader.class);

        newsLoader = new NewsLoader();
        incomingNews = new IncomingNews();
        addIncomingInfoToIncomingNews();

        configuration = new Configuration();
        Whitebox.setInternalState(configuration, "readerType", "WS");

        configurationLoader = mock(ConfigurationLoader.class);
        when(ConfigurationLoader.getInstance()).thenReturn(configurationLoader);
        when(configurationLoader.loadConfiguration()).thenReturn(configuration);

        newsReader = mock(NewsReader.class);
        when(newsReader.read()).thenReturn(incomingNews);
        when(NewsReaderFactory.getReader("WS")).thenReturn(newsReader);
    }

    @Test
    public void testConfigurationLoaderCallsLoadConfigurationOnce() {
        newsLoader.loadNews();
        verify(configurationLoader, times(1)).loadConfiguration();
    }

    @Test
    public void testArePublishableNewsDividedIntoPublicAndSubscriber() {
        PublishableNews publishableNews = newsLoader.loadNews();
        List<String> publicNews = (List<String>) Whitebox.getInternalState(publishableNews, "publicContent");
        List<String> subscriberNews = (List<String>) Whitebox.getInternalState(publishableNews, "subscribentContent");

        Assert.assertThat(publicNews.size(), is(1));

        Assert.assertThat(publicNews.get(0), is("publicContent"));

        Assert.assertThat(subscriberNews.size(), is(3));

        Assert.assertThat(subscriberNews.get(0), is("subscriberContentA"));
        Assert.assertThat(subscriberNews.get(1), is("subscriberContentB"));
        Assert.assertThat(subscriberNews.get(2), is("subscriberContentC"));
    }

}
