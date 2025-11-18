package br.com.jaas.backendposto.repository;

import br.com.jaas.backendposto.config.DBConnection;
import br.com.jaas.backendposto.model.Categoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDao extends DBConnection {

    public static boolean save(Categoria categoria) {
        String sql = "INSERT INTO categoria (nomeCategoria) VALUES (?)";
        try (Connection conn = getConnection()){
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, categoria.getNomeCategoria());
            return stmt.executeUpdate() > 0;
        }  catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Categoria findById(Long id) {
        String sql = "SELECT * FROM categoria WHERE idCategoria = ?";
        try (Connection conn = getConnection()){
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Categoria categoria = new Categoria(rs.getLong("idCategoria"), rs.getString("nomeCategoria"));
                return categoria;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static List<Categoria> findAll() {
        String sql = "SELECT * FROM categoria";
        List<Categoria> categorias = new ArrayList<>();
        try (Connection conn = getConnection()){
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                categorias.add(new Categoria(rs.getLong("idCategoria"), rs.getString("nomeCategoria")));
            }
            return categorias;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean update(Categoria categoria) {
        String sql = "UPDATE categoria SET nomeCategoria = ? WHERE idCategoria = ?";
        try(Connection conn  = getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, categoria.getNomeCategoria());
            stmt.setLong(2, categoria.getIdCategoria());
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean deleteById(Long id) {
        String sql = "DELETE FROM categoria WHERE idCategoria = ?";
        try(Connection conn = getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
