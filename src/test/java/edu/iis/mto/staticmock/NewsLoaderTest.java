package edu.iis.mto.staticmock;

import edu.iis.mto.staticmock.reader.NewsReader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.Whitebox;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

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
    private IncomingInfo incomingInfo = new IncomingInfo("contentA", SubsciptionType.A);
    private NewsReader newsReader;

    @Before
    public void setUp() {
        mockStatic(NewsReaderFactory.class);
        mockStatic(ConfigurationLoader.class);

        newsLoader = new NewsLoader();
        incomingNews = new IncomingNews();
        incomingNews.add(incomingInfo);

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

}
