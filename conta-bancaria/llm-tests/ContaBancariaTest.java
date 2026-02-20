import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import static org.junit.Assert.*;

public class ContaBancariaTest {

    private ContaBancaria conta;
    private static final double DELTA = 0.001; // Necessário para comparação de double no JUnit 4

    @Before
    public void setUp() {
        // Inicializa uma conta padrão antes de cada teste
        conta = new ContaBancaria("João Silva", 1000.0);
    }

    // --- TESTES DE CONSTRUTOR E ATRIBUTOS ---

    @Test
    public void deveCriarContaComSucesso() {
        assertEquals("João Silva", conta.getTitular());
        assertEquals(1000.0, conta.getSaldo(), DELTA);
        assertTrue(conta.isAtiva());
    }

    @Test(expected = IllegalArgumentException.class)
    public void deveLancarExcecaoTitularNulo() {
        new ContaBancaria(null, 100.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deveLancarExcecaoTitularVazio() {
        new ContaBancaria("   ", 100.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deveLancarExcecaoSaldoInicialNegativo() {
        new ContaBancaria("João", -0.01);
    }

    // --- TESTES DE DEPÓSITO ---

    @Test
    public void deveDepositarValorPositivo() {
        conta.depositar(500.0);
        assertEquals(1500.0, conta.getSaldo(), DELTA);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deveLancarExcecaoAoDepositarValorZero() {
        conta.depositar(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deveLancarExcecaoAoDepositarValorNegativo() {
        conta.depositar(-100.0);
    }

    @Test(expected = IllegalStateException.class)
    public void deveLancarExcecaoAoDepositarEmContaInativa() {
        conta.sacar(1000.0);
        conta.desativar();
        conta.depositar(10.0);
    }

    // --- TESTES DE SAQUE ---

    @Test
    public void deveSacarValorPermitido() {
        conta.sacar(400.0);
        assertEquals(600.0, conta.getSaldo(), DELTA);
    }

    @Test
    public void devePermitirSacarSaldoTotal() {
        conta.sacar(1000.0);
        assertEquals(0.0, conta.getSaldo(), DELTA);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deveLancarExcecaoAoSaqueMaiorQueSaldo() {
        conta.sacar(1000.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deveLancarExcecaoAoSaqueNegativo() {
        conta.sacar(-1.0);
    }

    // --- TESTES DE STATUS (ATIVA/INATIVA) ---

    @Test(expected = IllegalStateException.class)
    public void naoDeveDesativarContaComSaldo() {
        conta.desativar();
    }

    @Test
    public void deveDesativarContaComSaldoZero() {
        conta.sacar(1000.0);
        conta.desativar();
        assertFalse(conta.isAtiva());
    }

    @Test
    public void deveReativarContaInativa() {
        conta.sacar(1000.0);
        conta.desativar();
        conta.ativar();
        assertTrue(conta.isAtiva());
    }

    // --- TESTES DE TRANSFERÊNCIA ---

    @Test
    public void deveTransferirEntreContasAtivas() {
        ContaBancaria destino = new ContaBancaria("Maria", 500.0);
        conta.transferir(300.0, destino);
        
        assertEquals(700.0, conta.getSaldo(), DELTA);
        assertEquals(800.0, destino.getSaldo(), DELTA);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deveLancarExcecaoTransferirParaMesmaConta() {
        conta.transferir(100.0, conta);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deveLancarExcecaoTransferirParaDestinoNulo() {
        conta.transferir(100.0, null);
    }

    @Test(expected = IllegalStateException.class)
    public void deveLancarExcecaoTransferirDeContaInativa() {
        ContaBancaria destino = new ContaBancaria("Maria", 500.0);
        conta.sacar(1000.0);
        conta.desativar();
        conta.transferir(0.01, destino);
    }

    @Test(expected = IllegalStateException.class)
    public void deveLancarExcecaoTransferirParaDestinoInativo() {
        ContaBancaria destino = new ContaBancaria("Maria", 0.0);
        destino.desativar();
        conta.transferir(100.0, destino);
    }

    // --- TESTE DE FORMATAÇÃO ---

    @Test
    public void deveValidarToString() {
        // Nota: O formato depende do Locale. Ajuste a vírgula/ponto se necessário.
        String resultado = conta.toString();
        assertTrue(resultado.contains("João Silva"));
        assertTrue(resultado.contains("1000"));
        assertTrue(resultado.contains("Ativa"));
    }
}