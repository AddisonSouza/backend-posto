package br.com.jaas.backendposto.dao;

import br.com.jaas.backendposto.config.DBConnection;
import br.com.jaas.backendposto.model.Categoria;
import br.com.jaas.backendposto.model.Cliente;
import br.com.jaas.backendposto.model.Produto;
import br.com.jaas.backendposto.model.Venda;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class VendaDao extends GenericDao<Venda> {

    @Override
    protected String getTableName() {
        return "venda";
    }

    @Override
    protected String getIdColumnName() {
        return "idVenda";
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO venda (idCliente, idProduto, precoUnitario, quantidade, dataVenda) VALUES (?, ?, ?, ?, ?)";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE venda SET idCliente = ?, idProduto = ?, precoUnitario = ?, quantidade = ?, dataVenda = ? WHERE idVenda = ?";
    }

    @Override
    protected void setInsertParameters(PreparedStatement stmt, Venda venda) throws Exception {
        stmt.setLong(1, venda.getCliente().getIdCliente());
        stmt.setLong(2, venda.getProduto().getIdProduto());
        stmt.setDouble(3, venda.getPrecoUnitario());
        stmt.setInt(4, venda.getQuantidade());
        stmt.setTimestamp(5, Timestamp.valueOf(venda.getDataVenda()));
    }

    @Override
    protected void setUpdateParameters(PreparedStatement stmt, Venda venda) throws Exception {
        stmt.setLong(1, venda.getCliente().getIdCliente());
        stmt.setLong(2, venda.getProduto().getIdProduto());
        stmt.setDouble(3, venda.getPrecoUnitario());
        stmt.setInt(4, venda.getQuantidade());
        stmt.setTimestamp(5, Timestamp.valueOf(venda.getDataVenda()));
        stmt.setLong(6, venda.getIdVenda());
    }

    @Override
    protected Venda mapResultSetToEntity(ResultSet rs) throws Exception {
        Cliente cliente = new Cliente(
                rs.getLong("idCliente"),
                rs.getString("nome"),
                rs.getString("cpf"),
                rs.getString("telefone"),
                rs.getString("email"),
                rs.getString("endereco")
        );

        Categoria categoria = new Categoria(
                rs.getLong("id_categoria"),
                rs.getString("nome")
        );

        Produto produto = new Produto(
                rs.getLong("id_produto"),
                rs.getString("descricao"),
                rs.getInt("quantidade"),
                rs.getDouble("preco"),
                categoria
        );

        return new Venda(
                rs.getLong("idVenda"),
                cliente,
                produto,
                rs.getDouble("precoUnitario"),
                rs.getInt("quantidade"),
                rs.getTimestamp("dataVenda").toLocalDateTime()
        );
    }

    @Override
    public Venda findById(Long id) {
        String sql = "SELECT v.*, " +
                "c.idCliente, c.nome, c.cpf, c.telefone, c.email, c.endereco, " +
                "p.idProduto, p.descricao AS produtoDescricao, p.quantidade, p.preco, " +
                "cat.idCategoria, cat.nomeCategoria AS categoriaDescricao " +
                "FROM venda v " +
                "INNER JOIN cliente c ON v.idCliente = c.idCliente " +
                "INNER JOIN produto p ON v.idProduto = p.idProduto " +
                "INNER JOIN categoria cat ON p.idCategoria = cat.idCategoria " +
                "WHERE v.idVenda = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToEntity(rs);
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar venda por ID", e);
        }
    }


    @Override
    public List<Venda> findAll() {
        String sql = "SELECT v.*, " +
                "c.idCliente, c.nome, c.cpf, c.telefone, c.email, c.endereco, " +
                "p.idProduto, p.descricao AS produtoDescricao, p.quantidade, p.preco, " +
                "cat.idCategoria, cat.nomeCategoria AS categoriaDescricao " +
                "FROM venda v " +
                "INNER JOIN cliente c ON v.idCliente = c.idCliente " +
                "INNER JOIN produto p ON v.idProduto = p.idProduto " +
                "INNER JOIN categoria cat ON p.idCategoria = cat.idCategoria";
        List<Venda> vendas = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                vendas.add(mapResultSetToEntity(rs));
            }
            return vendas;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar todas as vendas", e);
        }
    }

}
