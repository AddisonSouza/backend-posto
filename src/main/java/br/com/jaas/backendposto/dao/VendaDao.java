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
        return "id_venda";
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO venda (id_cliente, id_produto, preco, quantidade, data_venda) VALUES (?, ?, ?, ?, ?)";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE venda SET id_cliente = ?, id_produto = ?, preco = ?, quantidade = ?, data_venda = ? WHERE id_venda = ?";
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
                rs.getLong("id_cliente"),
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
                rs.getString("nome"),
                rs.getInt("quantidade"),
                rs.getDouble("preco"),
                categoria
        );

        return new Venda(
                rs.getLong("id_venda"),
                cliente,
                produto,
                rs.getDouble("preco"),
                rs.getInt("quantidade"),
                rs.getTimestamp("data_venda").toLocalDateTime()
        );
    }

    @Override
    public Venda findById(Long id) {
        String sql = "SELECT v.*, " +
                "c.id_cliente, c.nome, c.cpf, c.telefone, c.email, c.endereco, " +
                "p.id_produto, p.nome AS produtoDescricao, p.quantidade, p.preco, " +
                "cat.id_categoria, cat.nome AS categoriaDescricao " +
                "FROM venda v " +
                "INNER JOIN cliente c ON v.id_cliente = c.id_cliente " +
                "INNER JOIN produto p ON v.id_produto = p.id_produto " +
                "INNER JOIN categoria cat ON p.id_categoria = cat.id_categoria " +
                "WHERE v.id_venda = ?";
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
                "c.id_cliente, c.nome, c.cpf, c.telefone, c.email, c.endereco, " +
                "p.id_produto, p.nome AS produtoDescricao, p.quantidade, p.preco, " +
                "cat.id_categoria, cat.nome AS categoriaDescricao " +
                "FROM venda v " +
                "INNER JOIN cliente c ON v.id_cliente = c.id_cliente " +
                "INNER JOIN produto p ON v.id_produto = p.id_produto " +
                "INNER JOIN categoria cat ON p.id_categoria = cat.id_categoria";
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
