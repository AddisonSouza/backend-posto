package br.com.jaas.backendposto.dao;


import br.com.jaas.backendposto.config.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class GenericDao<T> {

    protected abstract String getTableName();
    protected abstract String getIdColumnName();
    protected abstract String getInsertQuery();
    protected abstract String getUpdateQuery();
    protected abstract void setInsertParameters(PreparedStatement stmt, T entity) throws Exception;
    protected abstract void setUpdateParameters(PreparedStatement stmt, T entity) throws Exception;
    protected abstract T mapResultSetToEntity(ResultSet rs) throws Exception;

    public T save(T entity) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(getInsertQuery(), Statement.RETURN_GENERATED_KEYS)) {

            setInsertParameters(stmt, entity);
            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                return findById(generatedKeys.getLong(1));
            }
            return entity;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar " + getTableName(), e);
        }
    }

    public T findById(Long id) {
        String sql = "SELECT * FROM " + getTableName() + " WHERE " + getIdColumnName() + " = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToEntity(rs);
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar " + getTableName() + " por ID", e);
        }
    }

    public List<T> findAll() {
        String sql = "SELECT * FROM " + getTableName();
        List<T> entities = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                entities.add(mapResultSetToEntity(rs));
            }
            return entities;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar todos " + getTableName(), e);
        }
    }

    public void update(T entity) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(getUpdateQuery())) {

            setUpdateParameters(stmt, entity);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar " + getTableName(), e);
        }
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM " + getTableName() + " WHERE " + getIdColumnName() + " = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao deletar " + getTableName(), e);
        }
    }
}
