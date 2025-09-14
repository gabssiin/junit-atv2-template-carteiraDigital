import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.DigitalWallet;

class SaldoInicial {
    DigitalWallet dw;

    @BeforeEach
    public void setUp() {
        dw = new DigitalWallet("a", 0);
    }

    @Test // V
    void deveConfigurarSaldoInicialCorreto() {
        assertEquals(0, dw.getBalance());
    }

    @Test
    void deveLancarExcecaoParaSaldoInicialNegativo() {
        IllegalArgumentException excecao = assertThrows(IllegalArgumentException.class,
                () -> new DigitalWallet("a", -1));
        Assertions.assertEquals("Negative initial balance", excecao.getMessage());
    }

}