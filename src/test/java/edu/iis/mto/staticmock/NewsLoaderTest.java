package edu.iis.mto.staticmock;

import edu.iis.mto.staticmock.reader.NewsReader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.reflect.Whitebox;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ConfigurationLoader.class, NewsReaderFactory.class})
public class NewsLoaderTest {
    private ConfigurationLoader configurationLoader;
    private NewsReader newsReader;

    @Before
    public void setUp() {
        mockStatic(ConfigurationLoader.class);
        configurationLoader = mock(ConfigurationLoader.class);
        when(ConfigurationLoader.getInstance()).thenReturn(configurationLoader);

        Configuration configuration = new Configuration();
        when(configurationLoader.loadConfiguration()).thenReturn(configuration);

        IncomingInfo subscriberOnlyInfoA = new IncomingInfo("A", SubsciptionType.A);
        IncomingInfo subscriberOnlyInfoB = new IncomingInfo("B", SubsciptionType.B);
        IncomingInfo subscriberOnlyInfoC = new IncomingInfo("C", SubsciptionType.C);
        IncomingInfo publicInfo = new IncomingInfo("NONE", SubsciptionType.NONE);
        IncomingNews incomingNews = new IncomingNews();
        incomingNews.add(subscriberOnlyInfoA);
        incomingNews.add(subscriberOnlyInfoB);
        incomingNews.add(subscriberOnlyInfoC);
        incomingNews.add(publicInfo);

        mockStatic(NewsReaderFactory.class);
        newsReader = mock(NewsReader.class);
        when(newsReader.read()).thenReturn(incomingNews);
        when(NewsReaderFactory.getReader(any())).thenReturn(newsReader);
    }

    @Test
    public void loadNewsShouldSplitNewsIntoPublicAndSubscriberOnly() {
        NewsLoader newsLoader = new NewsLoader();
        PublishableNews publishableNews = newsLoader.loadNews();

        List<String> publicContent = Whitebox.getInternalState(publishableNews, "publicContent");
        List<String> subscribentContent = Whitebox.getInternalState(publishableNews, "subscribentContent");

        assertThat(publicContent, hasSize(1));
        assertThat(publicContent, contains("NONE"));

        assertThat(subscribentContent, hasSize(3));
        assertThat(subscribentContent, contains("A", "B", "C"));
    }
}
