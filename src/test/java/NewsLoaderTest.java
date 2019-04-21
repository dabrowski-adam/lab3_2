import edu.iis.mto.staticmock.*;
import edu.iis.mto.staticmock.reader.NewsReader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.*;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ConfigurationLoader.class, NewsReaderFactory.class})
public class NewsLoaderTest {

    private NewsLoader newsLoader;
    private NewsReaderFactory newsReaderFactory;
    private ConfigurationLoader configurationLoader;

    @Before
    public void setup(){

        Configuration configuration = new Configuration();
        IncomingNews incomingNews = new IncomingNews();

        mockStatic(ConfigurationLoader.class);
        configurationLoader = mock(ConfigurationLoader.class);
        when(ConfigurationLoader.getInstance()).thenReturn(configurationLoader);
        when(configurationLoader.loadConfiguration()).thenReturn(configuration);

        NewsReader newsReader = mock(NewsReader.class);
        when(newsReader.read()).thenReturn(incomingNews);

        mockStatic(NewsReaderFactory.class);
        newsReaderFactory = mock(NewsReaderFactory.class);
        when(NewsReaderFactory.getReader(any())).thenReturn(newsReader);
    }


}
