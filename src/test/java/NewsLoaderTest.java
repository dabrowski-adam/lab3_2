import edu.iis.mto.staticmock.*;
import edu.iis.mto.staticmock.reader.NewsReader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.*;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.reflect.Whitebox;

import java.util.List;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ConfigurationLoader.class, NewsReaderFactory.class})
public class NewsLoaderTest {

    private NewsLoader newsLoader;
    private NewsReaderFactory newsReaderFactory;
    private ConfigurationLoader configurationLoader;
    private PublishableNews publishableNews;

    @Before
    public void setup(){
        newsLoader = new NewsLoader();
        Configuration configuration = new Configuration();
        IncomingNews incomingNews = new IncomingNews();

        incomingNews.add(new IncomingInfo("first",SubsciptionType.A));
        incomingNews.add(new IncomingInfo("second",SubsciptionType.B));
        incomingNews.add(new IncomingInfo("third",SubsciptionType.C));
        incomingNews.add(new IncomingInfo("none",SubsciptionType.NONE));

        mockStatic(ConfigurationLoader.class);
        configurationLoader = mock(ConfigurationLoader.class);
        when(ConfigurationLoader.getInstance()).thenReturn(configurationLoader);
        when(configurationLoader.loadConfiguration()).thenReturn(configuration);

        NewsReader newsReader = mock(NewsReader.class);
        when(newsReader.read()).thenReturn(incomingNews);

        mockStatic(NewsReaderFactory.class);
        newsReaderFactory = mock(NewsReaderFactory.class);
        when(NewsReaderFactory.getReader(anyString())).thenReturn(newsReader);
    }

    @Test
    public void loadConfigurationMethodShouldBeCalledOnce(){
        publishableNews = newsLoader.loadNews();
        verify(configurationLoader, Mockito.times(1)).loadConfiguration();
    }

    @Test
    public void incommingNewsShouldBeCorrectlyDevided(){
        publishableNews = newsLoader.loadNews();
        List<String> publicMessages = Whitebox.getInternalState(publishableNews,"publicContent");
        List<String> subscribeMessages = Whitebox.getInternalState(publishableNews, "subscribentContent");

        Assert.assertEquals(1,publicMessages.size());
        Assert.assertEquals(3,subscribeMessages.size());

        Assert.assertEquals("none",publicMessages.get(0));
        Assert.assertEquals("first",subscribeMessages.get(0));
        Assert.assertEquals("second",subscribeMessages.get(1));
        Assert.assertEquals("third",subscribeMessages.get(2));
    }


}
