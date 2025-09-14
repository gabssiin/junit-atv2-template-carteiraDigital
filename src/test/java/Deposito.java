import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.example.DigitalWallet;

class Deposito {

    DigitalWallet dw;

    @BeforeEach
    public void setUp() {
        dw = new DigitalWallet("a", 0);
    }

    @ParameterizedTest
    @ValueSource(doubles = { 10.0, 50.0, 100.0 })
    void deveDepositarValoresValidos(double amount) {
        double saldoInicial = dw.getBalance();
        dw.deposit(amount);

        double saldoEsperado = saldoInicial + amount;
        Assertions.assertEquals(saldoEsperado, dw.getBalance(), 0.001,
                "Amount must be > 0");
    }

    @Test
    void deveLancarExcecaoParaDepositoInvalido() {
        IllegalArgumentException excecao = assertThrows(IllegalArgumentException.class,
                () -> dw.deposit(-100.0));
        assertEquals("Amount must be > 0", excecao.getMessage());
    }
}