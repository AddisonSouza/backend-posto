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
        return "INSERT INTO venda (idCliente, valorTotal, dataVenda) VALUES (?, ?, ?)";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE venda SET idCliente = ?, valorTotal = ?, dataVenda = ? WHERE idVenda = ?";
    }

    @Override
    protected void setInsertParameters(PreparedStatement stmt, Venda venda) throws Exception {
        stmt.setLong(1, venda.getCliente().getIdCliente());
        stmt.setDouble(2, venda.getPrecoUnitario());
        stmt.setTimestamp(3, Timestamp.valueOf(venda.getDataVenda()));
    }

    @Override
    protected void setUpdateParameters(PreparedStatement stmt, Venda venda) throws Exception {
        stmt.setLong(1, venda.getCliente().getIdCliente());
        stmt.setDouble(2, venda.getPrecoUnitario());
        stmt.setTimestamp(3, Timestamp.valueOf(venda.getDataVenda()));
        stmt.setLong(4, venda.getIdVenda());
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
            rs.getLong("idCategoria"),
            rs.getString("descricao")
        );

        Produto produto = new Produto(
            rs.getLong("idProduto"),
            rs.getString("descricao"),
            rs.getInt("qauntidade"),
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
        String sql = "SELECT v.*, c.idCliente, c.nome, c.cpf, c.telefone, c.email, c.endereco " +
                     "FROM venda v " +
                     "INNER JOIN cliente c ON v.idCliente = c.idCliente " +
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
        String sql = "SELECT v.*, c.idCliente, c.nome, c.cpf, c.telefone, c.email, c.endereco " +
                     "FROM venda v " +
                     "INNER JOIN cliente c ON v.idCliente = c.idCliente";
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
