package lab3_2;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import edu.iis.mto.staticmock.Configuration;
import edu.iis.mto.staticmock.ConfigurationLoader;
import edu.iis.mto.staticmock.NewsReaderFactory;
import edu.iis.mto.staticmock.reader.NewsReader;


@RunWith(PowerMockRunner.class)
@PrepareForTest( { ConfigurationLoader.class, NewsReaderFactory.class} )
public class NewsLoaderTest {
	
	@Before
	public void initialize() {
		mockStatic(ConfigurationLoader.class);
		Configuration configuration = mock(Configuration.class);
		ConfigurationLoader configurationLoader = mock(ConfigurationLoader.class);
		when(ConfigurationLoader.getInstance()).thenReturn(configurationLoader);
		when(ConfigurationLoader.getInstance().loadConfiguration()).thenReturn(configuration);
		
		mockStatic(NewsReaderFactory.class);
		NewsReader reader = mock(NewsReader.class);
		when(NewsReaderFactory.getReader(any())).thenReturn(reader);		
	}
	
	@Test
	public void newsShouldBeDividedIntoPublicAndSubscriptionContent() {
		
		
		
		
	}
}
