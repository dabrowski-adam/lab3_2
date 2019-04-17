package edu.iis.mto.staticmock;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ConfigurationLoader.class,NewsReaderFactory.class})
public class NewsLoaderTest {

    private NewsLoader sut;

    private List<IncomingInfo> expectedPublicMessages;
    private List<IncomingInfo> expectedSubscriptions;

    @Before
    public void setUp() {
        expectedPublicMessages = Arrays.asList(new IncomingInfo("public1", SubsciptionType.NONE),
                new IncomingInfo("public2", SubsciptionType.NONE),
                new IncomingInfo("public3", SubsciptionType.NONE));

        expectedSubscriptions = Arrays.asList(new IncomingInfo("subscribtionA", SubsciptionType.A),
                new IncomingInfo("subscriptionB", SubsciptionType.B),
                new IncomingInfo("subscirptionC", SubsciptionType.C));

        Configuration stubConfigutarion = mock(Configuration.class);
        when(stubConfigutarion.getReaderType()).thenReturn("kowlaski");

        mockStatic(ConfigurationLoader.class);

        ConfigurationLoader configurationLoaderMock = mock(ConfigurationLoader.class);
        when(configurationLoaderMock.loadConfiguration()).thenReturn(stubConfigutarion);
        when(ConfigurationLoader.getInstance()).thenReturn(configurationLoaderMock);

        mockStatic(NewsReaderFactory.class);

        final IncomingNews incomingNews = new IncomingNews();
        expectedPublicMessages.stream().forEach(incomingNews::add);
        expectedSubscriptions.stream().forEach(incomingNews::add);

        when(NewsReaderFactory.getReader(any())).thenReturn(() -> incomingNews);

        sut = new NewsLoader();
    }

    @After
    public void tearDown() {
        sut = null;
    }

    @Test
    public void shouldSeparatePublicMessagesAndSubscriptions() {
        PublishableNews result = sut.loadNews();

        List<String> publicContent = (List<String>) Whitebox.getInternalState(result,"publicContent");
        List<String> expectedPublicContent = expectedPublicMessages.stream().map(x -> x.getContent()).collect(Collectors.toList());
        assertThat(publicContent, is(expectedPublicContent));

        List<String> subscribentContent = (List<String>)Whitebox.getInternalState(result,"subscribentContent");
        List<String> expectedSubscribentContent = expectedSubscriptions.stream().map(x -> x.getContent()).collect(Collectors.toList());
        assertThat(subscribentContent, is(expectedSubscribentContent));
    }
}
