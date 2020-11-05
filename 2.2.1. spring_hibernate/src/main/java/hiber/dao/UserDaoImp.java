package hiber.dao;

import hiber.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDaoImp implements UserDao {

    @Autowired
    private SessionFactory sessionFactory;


    @Override
    public void add(User user) {
        sessionFactory.getCurrentSession().save(user);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> listUsers() {
        TypedQuery<User> query = sessionFactory.getCurrentSession().createQuery("from User");
        return query.getResultList();
    }

    @Override
    public User getUser(String model, int series) {
        User user = new User();
        TypedQuery<User> userQuery = sessionFactory.getCurrentSession()
                .createQuery("select new User(u.firstName,u.lastName,u.email) " +
                        "from User as u , Car as c " +
                        "where c.id = u.id " +
                        "and c.model = :model " +
                        "and c.series = :series", User.class)
                .setParameter("model", model)
                .setParameter("series", series);
        user.setFirstName(userQuery.getSingleResult().getFirstName());
        user.setLastName(userQuery.getSingleResult().getLastName());
        user.setEmail(userQuery.getSingleResult().getEmail());
        return user;
    }
}
