package com.tuempresa.proyectohibernate.Dao;

import com.tuempresa.proyectohibernate.Database.JPAUtil;
import com.tuempresa.proyectohibernate.Model.Libro;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class LibroDAO {
    public void guardar(Libro libro){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try{
            tx.begin();
            em.persist(libro);
            tx.commit();
        }catch(Exception e){
            if(tx.isActive()){
                tx.rollback();
            }
            throw e;
        }finally{
            em.close();
        }
    }

    public boolean modificar(Libro libro) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(libro);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            return false;
        } finally {
            em.close();
        }
    }

    public boolean eliminar(Long id) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Libro libro = em.find(Libro.class, id);
            if (libro != null) {
                em.remove(libro);
                tx.commit();
                return true;
            }
            tx.rollback();
            return false;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            return false;
        } finally {
            em.close();
        }
    }

    public List<Libro> listar(){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try{
            return em.createQuery("SELECT l FROM Libro l", Libro.class).getResultList();
        }finally {
            em.close();
        }
    }
}