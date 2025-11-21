package br.com.jaas.backendposto.repository;

import br.com.jaas.backendposto.config.DBConnection;
import br.com.jaas.backendposto.model.Produto;
import br.com.jaas.backendposto.model.Venda;
import br.com.jaas.backendposto.model.VendaProduto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class VendaProdutoDao extends GenericDao<VendaProduto> {

    @Override
    protected String getTableName() {
        return "vendaProduto";
    }

    @Override
    protected String getIdColumnName() {
        return "idVendaProduto";
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO vendaProduto (idProduto, idVenda) VALUES (?, ?)";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE vendaProduto SET idProduto = ?, idVenda = ? WHERE idVendaProduto = ?";
    }

    @Override
    protected void setInsertParameters(PreparedStatement stmt, VendaProduto vendaProduto) throws Exception {
        stmt.setLong(1, vendaProduto.getProduto().getIdProduto());
        stmt.setLong(2, vendaProduto.getVenda().getIdVenda());
    }

    @Override
    protected void setUpdateParameters(PreparedStatement stmt, VendaProduto vendaProduto) throws Exception {
        stmt.setLong(1, vendaProduto.getProduto().getIdProduto());
        stmt.setLong(2, vendaProduto.getVenda().getIdVenda());
        stmt.setLong(3, vendaProduto.getIdVendaProduto());
    }

    @Override
    protected VendaProduto mapResultSetToEntity(ResultSet rs) throws Exception {
        ProdutoDao produtoDao = new ProdutoDao();
        VendaDao vendaDao = new VendaDao();

        Produto produto = produtoDao.findById(rs.getLong("idProduto"));
        Venda venda = vendaDao.findById(rs.getLong("idVenda"));

        return new VendaProduto(
            rs.getLong("idVendaProduto"),
            produto,
            venda
        );
    }

    public List<VendaProduto> findByVendaId(Long idVenda) {
        String sql = "SELECT * FROM vendaProduto WHERE idVenda = ?";
        List<VendaProduto> vendaProdutos = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idVenda);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                vendaProdutos.add(mapResultSetToEntity(rs));
            }
            return vendaProdutos;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar produtos da venda", e);
        }
    }
}
