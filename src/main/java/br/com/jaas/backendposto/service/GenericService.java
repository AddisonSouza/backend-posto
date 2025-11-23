package br.com.jaas.backendposto.service;

import br.com.jaas.backendposto.dao.GenericDao;
import java.util.List;

@SuppressWarnings("unchecked")
public abstract class GenericService<T, D extends GenericDao<T>> {

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
        return dao.save(entity);
    }

    public T findById(Long id) {
        return dao.findById(id);
    }

    public List<T> findAll() {
        return dao.findAll();
    }

    public void update(T entity) {
        dao.update(entity);
    }

    public void deleteById(Long id) {
        dao.deleteById(id);
    }
}

