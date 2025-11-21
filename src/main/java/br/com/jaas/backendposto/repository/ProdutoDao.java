package br.com.jaas.backendposto.repository;

import br.com.jaas.backendposto.config.DBConnection;
import br.com.jaas.backendposto.model.Categoria;
import br.com.jaas.backendposto.model.Produto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDao extends GenericDao<Produto> {

    @Override
    protected String getTableName() {
        return "produto";
    }

    @Override
    protected String getIdColumnName() {
        return "idProduto";
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO produto (descricao, quantidade, preco, idCategoria) VALUES (?, ?, ?, ?)";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE produto SET descricao = ?, quantidade = ?, preco = ?, idCategoria = ? WHERE idProduto = ?";
    }

    @Override
    protected void setInsertParameters(PreparedStatement stmt, Produto produto) throws Exception {
        stmt.setString(1, produto.getDescricao());
        stmt.setInt(2, produto.getQuantidade());
        stmt.setDouble(3, produto.getPreco());
        stmt.setLong(4, produto.getCategoria().getIdCategoria());
    }

    @Override
    protected void setUpdateParameters(PreparedStatement stmt, Produto produto) throws Exception {
        stmt.setString(1, produto.getDescricao());
        stmt.setInt(2, produto.getQuantidade());
        stmt.setDouble(3, produto.getPreco());
        stmt.setLong(4, produto.getCategoria().getIdCategoria());
        stmt.setLong(5, produto.getIdProduto());
    }

    @Override
    protected Produto mapResultSetToEntity(ResultSet rs) throws Exception {
        Categoria categoria = new Categoria(
            rs.getLong("idCategoria"),
            rs.getString("nomeCategoria")
        );
        return new Produto(
            rs.getLong("idProduto"),
            rs.getString("descricao"),
            rs.getInt("quantidade"),
            rs.getDouble("preco"),
            categoria
        );
    }

    @Override
    public Produto findById(Long id) {
        String sql = "SELECT p.*, c.idCategoria, c.nomeCategoria FROM produto p " +
                     "INNER JOIN categoria c ON p.idCategoria = c.idCategoria " +
                     "WHERE p.idProduto = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToEntity(rs);
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar produto por ID", e);
        }
    }

    @Override
    public List<Produto> findAll() {
        String sql = "SELECT p.*, c.idCategoria, c.nomeCategoria FROM produto p " +
                     "INNER JOIN categoria c ON p.idCategoria = c.idCategoria";
        List<Produto> produtos = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                produtos.add(mapResultSetToEntity(rs));
            }
            return produtos;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar todos os produtos", e);
        }
    }
}
