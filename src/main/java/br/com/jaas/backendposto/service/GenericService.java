package br.com.jaas.backendposto.service;

import java.util.List;

@SuppressWarnings("unchecked")

public abstract class GenericService<T, D> {

    protected D dao;

    public GenericService() {
        try {
            String serviceName = this.getClass().getSimpleName();

            String daoName = serviceName.replace("Service", "Dao");

            String daoClassName = "br.com.jaas.backendposto.dao." + daoName;

            Class<?> daoClass = Class.forName(daoClassName);
            this.dao = (D) daoClass.getDeclaredConstructor().newInstance();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao instanciar DAO automaticamente", e);
        }
    }

    public T save(T entity) {
        try {
            return (T) dao.getClass().getMethod("save", Object.class).invoke(dao, entity);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar", e);
        }
    }

    public T findById(Long id) {
        try {
            return (T) dao.getClass().getMethod("findById", Long.class).invoke(dao, id);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar por ID", e);
        }
    }

    public List<T> findAll() {
        try {
            return (List<T>) dao.getClass().getMethod("findAll").invoke(dao);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar todos", e);
        }
    }

    public void update(T entity) {
        try {
            dao.getClass().getMethod("update", Object.class).invoke(dao, entity);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar", e);
        }
    }

    public void deleteById(Long id) {
        try {
            dao.getClass().getMethod("deleteById", Long.class).invoke(dao, id);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao deletar", e);
        }
    }
}

