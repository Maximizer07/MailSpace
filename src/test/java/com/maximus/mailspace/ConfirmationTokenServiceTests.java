package com.maximus.mailspace;

import com.maximus.mailspace.ConfirmationToken.ConfirmationToken;
import com.maximus.mailspace.ConfirmationToken.ConfirmationTokenRepository;
import com.maximus.mailspace.ConfirmationToken.ConfirmationTokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
public class ConfirmationTokenServiceTests {
    @Mock
    private ConfirmationTokenRepository confirmationTokenRepository;
    @Captor
    ArgumentCaptor<ConfirmationToken> captor;
    @Test
    void getConfirmationTokens() {
        ConfirmationToken confirmationToken = new ConfirmationToken();
        String name1= UUID.randomUUID().toString();
        confirmationToken.setConfirmationToken(name1);
        ConfirmationToken confirmationToken2 = new ConfirmationToken();
        String name2= UUID.randomUUID().toString();
        confirmationToken2.setConfirmationToken(name2);
        Mockito.when(confirmationTokenRepository.findAll()).thenReturn(List.of(confirmationToken,confirmationToken2));
        ConfirmationTokenService confirmationTokenService = new ConfirmationTokenService(confirmationTokenRepository);
        assertEquals(2,
                confirmationTokenService.readAll().size());
        assertEquals(name2,
                confirmationTokenRepository.findAll().get(1).getConfirmationToken());
    }
    @Test
    void ConfirmationTokenExists() {
        ConfirmationToken confirmationToken = new ConfirmationToken();
        String name1= UUID.randomUUID().toString();
        confirmationToken.setConfirmationToken(name1);
        ConfirmationTokenService confirmationTokenService = new ConfirmationTokenService(confirmationTokenRepository);
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        Mockito.verify(confirmationTokenRepository).save(captor.capture());
        ConfirmationToken captured= captor.getValue();
        assertEquals(name1, captured.getConfirmationToken());
    }
    @Test
    void ConfirmationTokenDelete() {
        ConfirmationToken confirmationToken = new ConfirmationToken();
        String name1= UUID.randomUUID().toString();
        confirmationToken.setConfirmationToken(name1);
        confirmationToken.setId(1);
        ConfirmationTokenService confirmationTokenService = new ConfirmationTokenService(confirmationTokenRepository);
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        Mockito.verify(confirmationTokenRepository).save(captor.capture());
        ConfirmationToken captured= captor.getValue();
        assertEquals(name1, captured.getConfirmationToken());
        confirmationTokenService.deleteConfirmationToken(1);
        Mockito.verify(confirmationTokenRepository).deleteById(captor.getValue().getId());
        assertFalse(confirmationTokenService.findConfirmationTokenByToken(name1).isPresent());
    }
}
