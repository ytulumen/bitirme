package sample;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import sample.Model.Candidate;
import sample.Model.Voter;

public class HibernateTutorialTest {
    private static SessionFactory factory;
    private static ServiceRegistry serviceRegistry;

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        Configuration config = new Configuration();
        config.configure();
        config.addAnnotatedClass(Candidate.class);
        serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();
        factory = config.buildSessionFactory(serviceRegistry);

        HibernateTutorialTest hbTest = new HibernateTutorialTest();
        hbTest.insertCandidate(new Candidate("can1", "cansur", 12345, "12345", "asdsa", "asdsa", "asdsad", "asdasd", "asdasd", "asdasd", 4, 0));

/*        List<Voter> voters = hbTest.listVoters();
        for(Voter voter : voters){
            System.out.print(voter.getId() + " ");
            System.out.print(voter.getName() + " ");
            System.out.print(voter.getNumber() + " ");
            System.out.print(voter.getPassword() + " ");
            System.out.print(voter.getTown() + " ");
            System.out.println();
        }*/

    }

    private int insertVoter(Voter voter)
    {
        Session session = factory.openSession();
        Transaction tx = null;
        Integer userIdSaved = null;
        try {
            tx = session.beginTransaction();
            userIdSaved = (Integer) session.save(voter);
            tx.commit();
        } catch(HibernateException ex) {
            if(tx != null)
                tx.rollback();
            ex.printStackTrace();
        } finally {
            session.close();
        }

        return userIdSaved;

    }
    private int insertCandidate(Candidate candidate)
    {
        Session session = factory.openSession();
        Transaction tx = null;
        Integer userIdSaved = null;
        try {
            tx = session.beginTransaction();
            userIdSaved = (Integer) session.save(candidate);
            tx.commit();
        } catch(HibernateException ex) {
            if(tx != null)
                tx.rollback();
            ex.printStackTrace();
        } finally {
            session.close();
        }

        return userIdSaved;

    }
    private List<Voter> listVoters()
    {
        Session sesn = factory.openSession();
        Transaction tx = null;
        List<Voter> voters = new ArrayList<Voter>();
        try{
            tx = sesn.beginTransaction();
            voters = (List)sesn.createQuery("from Voter").list();
            tx.commit();
        } catch(HibernateException e) {
            e.printStackTrace();
        } finally {
            sesn.close();
        }

        return voters;
    }

}

