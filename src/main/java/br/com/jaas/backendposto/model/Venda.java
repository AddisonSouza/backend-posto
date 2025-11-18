package br.com.jaas.backendposto.model;

import java.time.LocalDateTime;
import java.util.List;

public class Venda {
    private Long idVenda;
    private Cliente cliente;
    private double valorTotal;
    private LocalDateTime dataVenda;
    private List<VendaProduto> produtos;


    public Venda() {
    }

    public Venda(Long idVenda, Cliente cliente, double valorTotal, LocalDateTime dataVenda, List<VendaProduto> produtos) {
        this.idVenda = idVenda;
        this.cliente = cliente;
        this.valorTotal = valorTotal;
        this.dataVenda = dataVenda;
        this.produtos = produtos;
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

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public LocalDateTime getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(LocalDateTime dataVenda) {
        this.dataVenda = dataVenda;
    }

    public List<VendaProduto> getProdutos() {
        return produtos;
    }
    public void setProdutos(List<VendaProduto> produtos) {
        this.produtos = produtos;
    }
}
