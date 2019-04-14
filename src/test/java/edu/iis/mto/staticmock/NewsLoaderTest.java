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
    }

    @Test public void testMethod_loadConfigurationShouldBeCalled() {

        newsLoader.loadNews();
        verify(configurationLoader).loadConfiguration();
    }
}
