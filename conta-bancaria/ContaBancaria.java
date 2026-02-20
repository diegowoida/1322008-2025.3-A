/**
 * Classe simples de Conta Bancária para testes com EvoSuite
 */
public class ContaBancaria {
    private String titular;
    private double saldo;
    private boolean ativa;
    
    /**
     * Construtor para criar uma nova conta
     * @param titular Nome do titular (não pode ser nulo ou vazio)
     * @param saldoInicial Saldo inicial (não pode ser negativo)
     */
    public ContaBancaria(String titular, double saldoInicial) {
        if (titular == null || titular.trim().isEmpty()) {
            throw new IllegalArgumentException("Titular inválido");
        }
        if (saldoInicial < 0) {
            throw new IllegalArgumentException("Saldo inicial não pode ser negativo");
        }
        this.titular = titular;
        this.saldo = saldoInicial;
        this.ativa = true;
    }
    
    /**
     * @return Nome do titular
     */
    public String getTitular() {
        return titular;
    }
    
    /**
     * Altera o nome do titular
     * @param titular Novo nome (não pode ser vazio)
     */
    public void setTitular(String titular) {
        if (titular == null || titular.trim().isEmpty()) {
            throw new IllegalArgumentException("Titular inválido");
        }
        this.titular = titular;
    }
    
    /**
     * @return Saldo atual
     */
    public double getSaldo() {
        return saldo;
    }
    
    /**
     * @return true se a conta está ativa
     */
    public boolean isAtiva() {
        return ativa;
    }
    
    /**
     * Ativa a conta
     */
    public void ativar() {
        this.ativa = true;
    }
    
    /**
     * Desativa a conta (apenas se saldo for zero)
     */
    public void desativar() {
        if (saldo > 0) {
            throw new IllegalStateException("Não é possível desativar conta com saldo positivo");
        }
        this.ativa = false;
    }
    
    /**
     * Realiza um depósito
     * @param valor Valor a depositar (deve ser positivo)
     */
    public void depositar(double valor) {
        if (!ativa) {
            throw new IllegalStateException("Conta inativa");
        }
        if (valor <= 0) {
            throw new IllegalArgumentException("Valor do depósito deve ser positivo");
        }
        saldo += valor;
    }
    
    /**
     * Realiza um saque
     * @param valor Valor a sacar (deve ser positivo e menor ou igual ao saldo)
     */
    public void sacar(double valor) {
        if (!ativa) {
            throw new IllegalStateException("Conta inativa");
        }
        if (valor <= 0) {
            throw new IllegalArgumentException("Valor do saque deve ser positivo");
        }
        if (valor > saldo) {
            throw new IllegalArgumentException("Saldo insuficiente");
        }
        saldo -= valor;
    }
    
    /**
     * Transfere valor para outra conta
     * @param valor Valor a transferir
     * @param destino Conta de destino
     */
    public void transferir(double valor, ContaBancaria destino) {
        if (destino == null) {
            throw new IllegalArgumentException("Conta destino não pode ser nula");
        }
        if (!ativa) {
            throw new IllegalStateException("Conta origem inativa");
        }
        if (!destino.isAtiva()) {
            throw new IllegalStateException("Conta destino inativa");
        }
        if (this == destino) {
            throw new IllegalArgumentException("Não é possível transferir para a mesma conta");
        }
        
        // Usa o método sacar da própria conta
        this.sacar(valor);
        // Usa o método depositar da conta destino
        destino.depositar(valor);
    }
    
    @Override
    public String toString() {
        return String.format("Conta de %s - Saldo: R$ %.2f - %s", 
                titular, saldo, ativa ? "Ativa" : "Inativa");
    }
}

// java -cp "target/classes;target/test-classes;../evosuite-standalone-runtime-1.1.0.jar;target/dependency/junit-4.12.jar;target/dependency/hamcrest-core-1.3.jar" org.junit.runner.JUnitCore ContaBancaria_ESTest