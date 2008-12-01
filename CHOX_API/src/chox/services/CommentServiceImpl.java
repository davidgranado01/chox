/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.services;

import chox.Util.DateHelper;
import chox.model.Comment;
import chox.model.Claim;
import java.util.List;
import java.util.ArrayList;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Order;

public class CommentServiceImpl extends DataService implements CommentService {

    public List<Comment> getCommentByClaimId(int claimId) {

        List comments = new ArrayList<Comment>();

        try {
            //Criteria criteria = currentSession.createCriteria(Comment.class).add(Restrictions.eq("claim", claim));
            Criteria criteria = currentSession.createCriteria(Comment.class);//.add(Restrictions.eq("claimId", claim.getId()));
            criteria.createCriteria("claim").add(Restrictions.eq("id", claim.getId()));
            criteria.addOrder(Order.asc("claim.id"));           
            comments = criteria.list();

        } catch (Throwable e) {
            e.printStackTrace();
        }

        currentSession.clear();
        currentSession.disconnect();

        return comments;
    }

    public Comment getObject(int id) {
        return (Comment) currentSession.get(Comment.class, id);
    }

    public void createNewObject(Comment comment) {

        comment.setCreatedBy(getCurrentUser().getId());
        comment.setCreatedDate(DateHelper.getCurrentTimeStamp());
        comment.setLastModifiedBy(getCurrentUser().getId());
        comment.setLastModifiedDate(DateHelper.getCurrentTimeStamp());


        currentSession.beginTransaction();
        currentSession.saveOrUpdate(comment);
        currentSession.getTransaction().commit();
    }
}
