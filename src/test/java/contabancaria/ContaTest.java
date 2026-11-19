package contabancaria;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ContaTest {

    // =======================================================
    // PARTE 1 — EXEMPLO GUIADO: Testes do Construtor
    // =======================================================

    @Test
    void construtor_DadosValidos_CriaContaCorretamente() {
        var conta = new Conta("Maria", 100);

        assertEquals("Maria", conta.getTitular());
        assertEquals(100, conta.getSaldo());
        assertTrue(conta.isAtiva());
    }

    @Test
    void construtor_SemSaldoInicial_CriaContaComSaldoZero() {
        var conta = new Conta("João");

        assertEquals("João", conta.getTitular());
        assertEquals(0, conta.getSaldo());
        assertTrue(conta.isAtiva());
    }

    @Test
    void construtor_TitularNulo_LancaIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Conta(null));
    }

    @Test
    void construtor_TitularVazio_LancaIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Conta(""));
    }

    @Test
    void construtor_SaldoNegativo_LancaIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Conta("Maria", -50));
    }

    @ParameterizedTest
    @CsvSource({
            "Ana,    0",
            "Carlos, 1000",
            "Beatriz, 0.01"
    })
    void construtor_VariosValoresValidos_CriaContaCorretamente(String titular, double saldo) {
        var conta = new Conta(titular, saldo);

        assertEquals(titular, conta.getTitular());
        assertEquals(saldo, conta.getSaldo(), 0.001);
        assertTrue(conta.isAtiva());
    }

    // =======================================================
    // PARTE 2 — TESTES DOS DEMAIS MÉTODOS
    // =======================================================

    // =======================================================
    // Testes para depositar
    // =======================================================

    @Test
    void depositar_ValorValido_AtualizaSaldo() {
        var conta = new Conta("Maria", 100);

        conta.depositar(50);

        assertEquals(150, conta.getSaldo(), 0.001);
    }

    @Test
    void depositar_ValorZero_LancaIllegalArgumentException() {
        var conta = new Conta("Maria", 100);

        assertThrows(IllegalArgumentException.class, () -> conta.depositar(0));
        assertEquals(100, conta.getSaldo(), 0.001);
    }

    @Test
    void depositar_ValorNegativo_LancaIllegalArgumentException() {
        var conta = new Conta("Maria", 100);

        assertThrows(IllegalArgumentException.class, () -> conta.depositar(-10));
        assertEquals(100, conta.getSaldo(), 0.001);
    }

    @Test
    void depositar_ContaInativa_LancaIllegalStateException() {
        var conta = new Conta("Maria", 0);
        conta.encerrar();

        assertThrows(IllegalStateException.class, () -> conta.depositar(50));
        assertEquals(0, conta.getSaldo(), 0.001);
    }

    // =======================================================
    // Testes para sacar
    // =======================================================

    @Test
    void sacar_ValorValido_AtualizaSaldo() {
        var conta = new Conta("Maria", 100);

        conta.sacar(40);

        assertEquals(60, conta.getSaldo(), 0.001);
    }

    @Test
    void sacar_ValorMaiorQueSaldo_LancaIllegalStateException() {
        var conta = new Conta("Maria", 100);

        assertThrows(IllegalStateException.class, () -> conta.sacar(101));
        assertEquals(100, conta.getSaldo(), 0.001);
    }

    @Test
    void sacar_ValorZero_LancaIllegalArgumentException() {
        var conta = new Conta("Maria", 100);

        assertThrows(IllegalArgumentException.class, () -> conta.sacar(0));
        assertEquals(100, conta.getSaldo(), 0.001);
    }

    @Test
    void sacar_ValorNegativo_LancaIllegalArgumentException() {
        var conta = new Conta("Maria", 100);

        assertThrows(IllegalArgumentException.class, () -> conta.sacar(-10));
        assertEquals(100, conta.getSaldo(), 0.001);
    }

    @Test
    void sacar_ContaInativa_LancaIllegalStateException() {
        var conta = new Conta("Maria", 0);
        conta.encerrar();

        assertThrows(IllegalStateException.class, () -> conta.sacar(10));
        assertEquals(0, conta.getSaldo(), 0.001);
    }

    // =======================================================
    // Testes para encerrar
    // =======================================================

    @Test
    void encerrar_SaldoZero_DesativaConta() {
        var conta = new Conta("Maria");

        conta.encerrar();

        assertFalse(conta.isAtiva());
    }

    @Test
    void encerrar_ComSaldoPositivo_LancaIllegalStateException() {
        var conta = new Conta("Maria", 1);

        assertThrows(IllegalStateException.class, conta::encerrar);
        assertTrue(conta.isAtiva());
        assertEquals(1, conta.getSaldo(), 0.001);
    }

    @Test
    void encerrar_ContaJaInativa_LancaIllegalStateException() {
        var conta = new Conta("Maria");
        conta.encerrar();

        assertThrows(IllegalStateException.class, conta::encerrar);
        assertFalse(conta.isAtiva());
    }

    // =======================================================
    // Testes para transferir
    // =======================================================

    @Test
    void transferir_ValorValido_AtualizaSaldoDeAmbasContas() {
        var origem = new Conta("Maria", 200);
        var destino = new Conta("João", 100);

        origem.transferir(destino, 50);

        assertEquals(150, origem.getSaldo(), 0.001);
        assertEquals(150, destino.getSaldo(), 0.001);
    }

    @Test
    void transferir_SaldoInsuficiente_LancaIllegalStateException() {
        var origem = new Conta("Maria", 200);
        var destino = new Conta("João", 100);

        assertThrows(IllegalStateException.class, () -> origem.transferir(destino, 201));
        assertEquals(200, origem.getSaldo(), 0.001);
        assertEquals(100, destino.getSaldo(), 0.001);
    }

    @Test
    void transferir_ValorZero_LancaIllegalArgumentException() {
        var origem = new Conta("Maria", 200);
        var destino = new Conta("João", 100);

        assertThrows(IllegalArgumentException.class, () -> origem.transferir(destino, 0));
        assertEquals(200, origem.getSaldo(), 0.001);
        assertEquals(100, destino.getSaldo(), 0.001);
    }

    @Test
    void transferir_ValorNegativo_LancaIllegalArgumentException() {
        var origem = new Conta("Maria", 200);
        var destino = new Conta("João", 100);

        assertThrows(IllegalArgumentException.class, () -> origem.transferir(destino, -10));
        assertEquals(200, origem.getSaldo(), 0.001);
        assertEquals(100, destino.getSaldo(), 0.001);
    }

    @Test
    void transferir_ContaOrigemInativa_LancaIllegalStateException() {
        var origem = new Conta("Maria", 0);
        var destino = new Conta("João", 100);
        origem.encerrar();

        assertThrows(IllegalStateException.class, () -> origem.transferir(destino, 50));
        assertEquals(0, origem.getSaldo(), 0.001);
        assertEquals(100, destino.getSaldo(), 0.001);
    }

    @Test
    void transferir_ContaDestinoInativa_LancaIllegalStateException() {
        var origem = new Conta("Maria", 200);
        var destino = new Conta("João", 0);
        destino.encerrar();

        assertThrows(IllegalStateException.class, () -> origem.transferir(destino, 50));
        assertEquals(200, origem.getSaldo(), 0.001);
        assertEquals(0, destino.getSaldo(), 0.001);
    }
}