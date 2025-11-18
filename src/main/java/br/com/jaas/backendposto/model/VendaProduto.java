package br.com.jaas.backendposto.model;

public class VendaProduto {
    private Long idVendaProduto;
    private Produto produto;
    private Venda venda;

    public VendaProduto() {
    }

    public VendaProduto(Long idVendaProduto, Produto produto, Venda venda) {
        this.idVendaProduto = idVendaProduto;
        this.produto = produto;
        this.venda = venda;
    }

    public Long getIdVendaProduto() {
        return idVendaProduto;
    }

    public void setIdVendaProduto(Long idVendaProduto) {
        this.idVendaProduto = idVendaProduto;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Venda getVenda() {
        return venda;
    }

    public void setVenda(Venda venda) {
        this.venda = venda;
    }
}
