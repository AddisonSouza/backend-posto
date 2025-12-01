package br.com.jaas.backendposto.dao;

import br.com.jaas.backendposto.model.Cliente;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ClienteDao extends GenericDao<Cliente> {

    @Override
    protected String getTableName() {
        return "cliente";
    }

    @Override
    protected String getIdColumnName() {
        return "idCliente";
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO cliente (nome, cpf, telefone, email, endereco) VALUES (?, ?, ?, ?, ?)";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE cliente SET nome = ?, cpf = ?, telefone = ?, email = ?, endereco = ? WHERE idCliente = ?";
    }

    @Override
    protected void setInsertParameters(PreparedStatement stmt, Cliente cliente) throws Exception {
        stmt.setString(1, cliente.getNome());
        stmt.setString(2, cliente.getCpf());
        stmt.setString(3, cliente.getTelefone());
        stmt.setString(4, cliente.getEmail());
        stmt.setString(5, cliente.getEndereco());
    }

    @Override
    protected void setUpdateParameters(PreparedStatement stmt, Cliente cliente) throws Exception {
        stmt.setString(1, cliente.getNome());
        stmt.setString(2, cliente.getCpf());
        stmt.setString(3, cliente.getTelefone());
        stmt.setString(4, cliente.getEmail());
        stmt.setString(5, cliente.getEndereco());
        stmt.setLong(6, cliente.getIdCliente());
    }

    @Override
    protected Cliente mapResultSetToEntity(ResultSet rs) throws Exception {
        return new Cliente(
            rs.getLong("id_cliente"),
            rs.getString("nome"),
            rs.getString("cpf"),
            rs.getString("telefone"),
            rs.getString("email"),
            rs.getString("endereco")
        );
    }
}
