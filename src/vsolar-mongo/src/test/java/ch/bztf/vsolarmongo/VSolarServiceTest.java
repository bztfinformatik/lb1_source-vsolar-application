package ch.bztf.vsolarmongo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.List;
import static org.mockito.Mockito.*;

public class VSolarServiceTest {

    @Mock
    private VSolarRepository vSolarRepositoryMock;

    private VSolarService vSolarService;

    @BeforeEach
    public void setup() {
        // Erstelle Mock
        MockitoAnnotations.openMocks(this);

        // Instanz für Service-Objekts -> Mock Objekt
        vSolarService = new VSolarService();

        vSolarService.vSolarRepository = vSolarRepositoryMock;
    }

    @Test
    public void testUpdateApiToken() {
        // Testdaten
        String apiToken = "newApiToken";
        VSolarENV env = new VSolarENV();

        // Mock-Verhalten
        when(vSolarRepositoryMock.findAll()).thenReturn(List.of(env));
        when(vSolarRepositoryMock.save(env)).thenReturn(env);

        // Methodenaufruf
        String result = vSolarService.updateApiToken(apiToken);

        // Assertions
        Assertions.assertEquals(apiToken, result);
        Assertions.assertEquals(apiToken, env.getApiToken());
        verify(vSolarRepositoryMock, times(1)).save(env);
    }
}
