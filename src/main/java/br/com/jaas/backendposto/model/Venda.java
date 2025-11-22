package br.com.jaas.backendposto.model;

import java.time.LocalDateTime;

public class Venda {
    private Long idVenda;
    private Cliente cliente;
    private Produto produto;
    private double precoUnitario;
    private int quantidade;
    private LocalDateTime dataVenda;


    public Venda() {
    }

    public Venda(Long idVenda, Cliente cliente, Produto produto, double precoUnitario, int quantidade, LocalDateTime dataVenda) {
        this.idVenda = idVenda;
        this.cliente = cliente;
        this.produto = produto;
        this.precoUnitario = precoUnitario;
        this.quantidade = quantidade;
        this.dataVenda = dataVenda;
    }

    public Long getIdVenda() {
        return idVenda;
    }

    public void setIdVenda(Long idVenda) {
        this.idVenda = idVenda;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(double precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public LocalDateTime getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(LocalDateTime dataVenda) {
        this.dataVenda = dataVenda;
    }
}
