import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;

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
    @ValueSource(doubles = { 10.0, 50.0, 100.0, 0.01 })
    void deveDepositarValoresValidos(double amount) {
        double saldoInicial = dw.getBalance();
        dw.deposit(amount);

        double saldoEsperado = saldoInicial + amount;
        assertEquals(saldoEsperado, dw.getBalance(), 0.001,
                "O saldo deve ser atualizado corretamente após o depósito.");
    }

    @ParameterizedTest
    @ValueSource(doubles = { 0.0, -10.0, -0.01 })
    void deveLancarExcecaoParaDepositoInvalido(double amount) {
        double saldoInicial = dw.getBalance();

        IllegalArgumentException excecao = assertThrows(IllegalArgumentException.class,
                () -> dw.deposit(amount),
                "Depósitos com valor menor ou igual a zero devem lançar IllegalArgumentException.");

        assertEquals("Amount must be > 0", excecao.getMessage(),
                "A mensagem da exceção deve ser 'Amount must be > 0'.");
        assertEquals(saldoInicial, dw.getBalance(), "O saldo não deve ser alterado após uma exceção.");
    }
}