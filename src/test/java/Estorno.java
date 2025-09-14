import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.api.Test;

import com.example.DigitalWallet;

class Estorno {
    static Stream<Arguments> valoresEstorno() {
        return Stream.of(
                Arguments.of(100.0, 10.0, 110.0),
                Arguments.of(0.0, 5.0, 5.0),
                Arguments.of(50.0, 0.01, 50.01));
    }

    @ParameterizedTest
    @MethodSource("valoresEstorno")
    void refundComCarteiraValida(double inicial, double valor, double saldoEsperado) {
        DigitalWallet wallet = new DigitalWallet("Test User", inicial);
        wallet.verify();

        // Pré-condição para o teste "feliz"
        assumeTrue(wallet.isVerified() && !wallet.isLocked());

        wallet.refund(valor);

        assertEquals(saldoEsperado, wallet.getBalance(), "O saldo deve ser aumentado pelo valor do estorno.");
    }

    @ParameterizedTest
    @ValueSource(doubles = { 0.0, -10.0, -0.01 })
    void deveLancarExcecaoParaRefundInvalido(double valor) {
        DigitalWallet wallet = new DigitalWallet("Test User", 100.0);
        wallet.verify();

        // Pré-condição
        assumeTrue(wallet.isVerified() && !wallet.isLocked());

        assertThrows(IllegalArgumentException.class, () -> wallet.refund(valor),
                "Estorno com valor <= 0 deve lançar IllegalArgumentException.");
        assertEquals(100.0, wallet.getBalance(), "O saldo não deve ser alterado.");
    }

    @Test
    void deveLancarSeNaoVerificadaOuBloqueada() {
        DigitalWallet unverifiedWallet = new DigitalWallet("Test User", 100.0);
        unverifiedWallet.unlock();

        // Testa carteira não verificada
        assumeFalse(unverifiedWallet.isVerified());
        assertThrows(IllegalStateException.class, () -> unverifiedWallet.refund(10.0),
                "Estorno em carteira não verificada deve lançar IllegalStateException.");
        assertEquals(100.0, unverifiedWallet.getBalance(), "O saldo não deve ser alterado.");

        // Testa carteira bloqueada
        DigitalWallet lockedWallet = new DigitalWallet("Test User", 100.0);
        lockedWallet.verify();
        lockedWallet.lock();

        assumeTrue(lockedWallet.isLocked());
        assertThrows(IllegalStateException.class, () -> lockedWallet.refund(10.0),
                "Estorno em carteira bloqueada deve lançar IllegalStateException.");
        assertEquals(100.0, lockedWallet.getBalance(), "O saldo não deve ser alterado.");
    }
}