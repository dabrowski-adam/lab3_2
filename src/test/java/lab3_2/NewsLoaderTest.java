package lab3_2;

import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.hamcrest.Matchers.is;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import edu.iis.mto.staticmock.Configuration;
import edu.iis.mto.staticmock.ConfigurationLoader;
import edu.iis.mto.staticmock.IncomingInfo;
import edu.iis.mto.staticmock.IncomingNews;
import edu.iis.mto.staticmock.NewsLoader;
import edu.iis.mto.staticmock.NewsReaderFactory;
import edu.iis.mto.staticmock.PublishableNews;
import edu.iis.mto.staticmock.SubsciptionType;
import edu.iis.mto.staticmock.reader.NewsReader;


@RunWith(PowerMockRunner.class)
@PrepareForTest( { ConfigurationLoader.class, NewsReaderFactory.class} )
public class NewsLoaderTest {
	
	NewsLoader loader;
	IncomingNews incomingNews;
	
	@Before
	public void initialize() {
		mockStatic(ConfigurationLoader.class);
		Configuration configuration = mock(Configuration.class);
		ConfigurationLoader configurationLoader = mock(ConfigurationLoader.class);
		when(ConfigurationLoader.getInstance()).thenReturn(configurationLoader);
		when(ConfigurationLoader.getInstance().loadConfiguration()).thenReturn(configuration);
		
		mockStatic(NewsReaderFactory.class);
		
		loader = new NewsLoader();
		incomingNews = new IncomingNews();
		NewsReader reader = mock(NewsReader.class);
		when(NewsReaderFactory.getReader(any())).thenReturn(reader);
		when(reader.read()).thenReturn(incomingNews);
	}
	
	@Test
	public void newsShouldBeDividedIntoPublicAndSubscriptionContent() {
		incomingNews.add(new IncomingInfo("PUBLIC INFO", SubsciptionType.NONE));
		incomingNews.add(new IncomingInfo("SUB INFO", SubsciptionType.A));
		incomingNews.add(new IncomingInfo("PUBLIC INFO 2", SubsciptionType.NONE));
		incomingNews.add(new IncomingInfo("SUB INFO 2", SubsciptionType.B));
		PublishableNews publishableNews = loader.loadNews();
		List <String> expectedPublicNews = Whitebox.getInternalState(publishableNews, "publicContent");
		List <String> expectedSubNews = Whitebox.getInternalState(publishableNews, "subscribentContent");
		
		assertThat(expectedPublicNews.get(0), is(incomingNews.get(0).getContent()));
		assertThat(expectedSubNews.get(0), is(incomingNews.get(1).getContent()));
	}
	
	@Test
	public void loaderShouldReturnEmptyListWhenThereAreNoNews() {
		PublishableNews publishableNews = loader.loadNews();
		List <String> expectedPublicNews = Whitebox.getInternalState(publishableNews, "publicContent");
		List <String> expectedSubNews = Whitebox.getInternalState(publishableNews, "subscribentContent");
		
		assertThat(expectedPublicNews.size(),is(0));
		assertThat(expectedSubNews.size(),is(0));
	}
}
