package br.com.jaas.backendposto.dao;

import br.com.jaas.backendposto.model.Categoria;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CategoriaDao extends GenericDao<Categoria> {

    @Override
    protected String getTableName() {
        return "categoria";
    }

    @Override
    protected String getIdColumnName() {
        return "idCategoria";
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO categoria (nomeCategoria) VALUES (?)";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE categoria SET nomeCategoria = ? WHERE idCategoria = ?";
    }

    @Override
    protected void setInsertParameters(PreparedStatement stmt, Categoria categoria) throws Exception {
        stmt.setString(1, categoria.getNomeCategoria());
    }

    @Override
    protected void setUpdateParameters(PreparedStatement stmt, Categoria categoria) throws Exception {
        stmt.setString(1, categoria.getNomeCategoria());
        stmt.setLong(2, categoria.getIdCategoria());
    }

    @Override
    protected Categoria mapResultSetToEntity(ResultSet rs) throws Exception {
        return new Categoria(
            rs.getLong("idCategoria"),
            rs.getString("nomeCategoria")
        );
    }
}
