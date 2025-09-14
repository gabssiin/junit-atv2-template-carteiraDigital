import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.api.Test;

import com.example.DigitalWallet;

public class Pagamento {

    @ParameterizedTest
    @CsvSource({
            "100.0, 50.0, true",
            "50.0, 50.0, true",
            "100.0, 150.0, false",
            "0.0, 10.0, false"
    })
    void pagamentoComCarteiraVerificadaENaoBloqueada(double inicial, double valor, boolean esperado) {
        DigitalWallet wallet = new DigitalWallet("Test User", inicial);
        wallet.verify();
        assumeTrue(wallet.isVerified() && !wallet.isLocked(),
                "Pré-condição: a carteira deve estar verificada e não bloqueada.");

        boolean resultado = wallet.pay(valor);

        assertEquals(esperado, resultado, "O resultado do pagamento deve ser o esperado.");

        if (esperado) {
            assertEquals(inicial - valor, wallet.getBalance(), "O saldo deve ser debitado corretamente.");
        } else {
            assertEquals(inicial, wallet.getBalance(), "O saldo não deve ser alterado em caso de falha.");
        }
    }

    @ParameterizedTest
    @ValueSource(doubles = { 0.0, -10.0, -0.01 })
    void deveLancarExcecaoParaPagamentoInvalido(double valor) {
        DigitalWallet wallet = new DigitalWallet("Test User", 100.0);
        wallet.verify();

        assertThrows(IllegalArgumentException.class, () -> wallet.pay(valor),
                "Pagamento com valor <= 0 deve lançar IllegalArgumentException.");
        assertEquals(100.0, wallet.getBalance(), "O saldo não deve ser alterado.");
    }

    @Test
    void deveRetornarFalsoSeNaoVerificadaOuBloqueada() {
        DigitalWallet unverifiedWallet = new DigitalWallet("Test User", 100.0);
        unverifiedWallet.unlock();
    }

    @Test
    void deveLancarSeNaoVerificadaOuBloqueada() {
        DigitalWallet unverifiedWallet = new DigitalWallet("Test User", 100.0);
        unverifiedWallet.unlock();

        // Testa carteira não verificada
        assumeFalse(unverifiedWallet.isVerified());
        assertThrows(IllegalStateException.class, () -> unverifiedWallet.pay(10.0),
                "Pagamento em carteira não verificada deve lançar IllegalStateException.");
        assertEquals(100.0, unverifiedWallet.getBalance(), "O saldo não deve ser alterado.");

        // Testa carteira bloqueada
        DigitalWallet lockedWallet = new DigitalWallet("Test User", 100.0);
        lockedWallet.verify();
        lockedWallet.lock();

        assumeTrue(lockedWallet.isLocked());
        assertThrows(IllegalStateException.class, () -> lockedWallet.pay(10.0),
                "Pagamento em carteira bloqueada deve lançar IllegalStateException.");
        assertEquals(100.0, lockedWallet.getBalance(), "O saldo não deve ser alterado.");
    }
}
