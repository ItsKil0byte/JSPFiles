package com.example.database;

import com.example.models.UserProfile;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class UserService {
    private final SessionFactory sessionFactory;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL Driver not found", e);
        }
    }

    public UserService() {
        this.sessionFactory = new Configuration().configure().buildSessionFactory();
    }

    public long addNewUser(String login, String password) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            UsersDAO usersDAO = new UsersDAO(session);
            UserProfile user = new UserProfile(login, password);
            usersDAO.insertUser(user);
            transaction.commit();

            return user.getId();
        }
    }

    public UserProfile getUser(String login) {
        try (Session session = sessionFactory.openSession()) {
            UsersDAO usersDAO = new UsersDAO(session);
            return usersDAO.get(login);
        }
    }

    public UserProfile getUser(long id) {
        try (Session session = sessionFactory.openSession()) {
            UsersDAO usersDAO = new UsersDAO(session);
            return usersDAO.get(id);
        }
    }
}
