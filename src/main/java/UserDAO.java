import db.WordEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.swing.*;
import java.util.List;

public class UserDAO {

    private static String[] columnDescriptions = {
            "Word",
            "Translation",
            "Successful attempts (word -> translation)",
            "Successful attempts (translation -> word)"};

    public void save(WordEntity word) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.save(word);
        tx.commit();
        session.close();
    }

    public void delete(String word) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        Query query = session.createQuery("delete from WordsEntity where word = :word");
        query.setParameter("word", word);
        query.executeUpdate();
        tx.commit();
        session.close();
    }

    public List<WordEntity> getRows(WordEntity t){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        String hql = "from " + t.getClass().getSimpleName();
        Query query = session.createQuery(hql);
        return query.getResultList();
    }

    public void increaseAttempts_t(WordEntity wordEntity){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        wordEntity.increaseAttempts_t();
        session.update(wordEntity);
        tx.commit();
        session.close();
    }

    public void decreaseAttempts_t(WordEntity wordEntity){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        wordEntity.decreaseAttempts_t();
        session.update(wordEntity);
        tx.commit();
        session.close();
    }

    public void increaseAttempts_w(WordEntity wordEntity){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        wordEntity.increaseAttempts_w();
        session.update(wordEntity);
        tx.commit();
        session.close();
    }

    public void decreaseAttempts_w(WordEntity wordEntity){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        wordEntity.decreaseAttempts_w();
        session.update(wordEntity);
        tx.commit();
        session.close();
    }

    public void showTable(WordEntity w){
        List<WordEntity> list = this.getRows(w);
        Object[][] data = new Object[list.toArray().length][4];
        for (int i = 0; i < list.toArray().length; i++){
            data[i][0] = list.get(i).getWord();
            data[i][1] = list.get(i).getTranslation();
            data[i][2] = list.get(i).getAttempts_w();
            data[i][3] = list.get(i).getAttempts_t();
        }
        JTable table = new JTable(data, columnDescriptions);
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);
        JFrame tableWindow = new JFrame();
        tableWindow.setTitle("List of words in archive");
        tableWindow.setSize(800, 680);
        tableWindow.add(scrollPane);
        tableWindow.setResizable(false);
        tableWindow.setVisible(true);
    }

    public boolean isInDB(String s){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Query query = session.createQuery("select word from WordsEntity where word = :word");
        query.setParameter("word", s);
        if (query.list().isEmpty()) {
            return false;
        }
        return true;
    }

}
