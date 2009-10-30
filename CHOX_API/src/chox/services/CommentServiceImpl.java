/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.services;

import chox.model.Comment;
import chox.model.Claim;
import java.util.List;
import java.util.ArrayList;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Order;

public class CommentServiceImpl extends SecureDataService implements CommentService {

    public List<Comment> getCommentByClaimId(int claimId) {

        List comments = new ArrayList<Comment>();

        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(Comment.class);
            criteria.createCriteria("claim").add(Restrictions.eq("id", claimId));
            criteria.addOrder(Order.asc("createdDate"));           
            comments = findByCriteria(criteria);

        } catch (Throwable e) {
            e.printStackTrace();
        }

        return comments;
    }
     
    public List<Comment> getCommentByClaimIdFilterByOrg(int claimId, boolean isCreditHire) {

        List comments = new ArrayList<Comment>();

        try {
            
            DetachedCriteria criteria = DetachedCriteria.forClass(Comment.class);
            criteria.createCriteria("claim").add(Restrictions.eq("id", claimId));
            
            if(isCreditHire){
                criteria.add(Restrictions.eq("isPublic", true));
            }
            
            criteria.addOrder(Order.asc("createdDate"));
            comments = findByCriteria(criteria);

        } catch (Throwable e) {
            e.printStackTrace();
        }

        return comments;
    }
        
    public List<Comment> getCommentByClaim(Claim claim) {

        List comments = new ArrayList<Comment>();

        try {
            //Criteria criteria = getCurrentSession().createCriteria(Comment.class).add(Restrictions.eq("claim", claim));
            DetachedCriteria criteria = DetachedCriteria.forClass(Comment.class);//.add(Restrictions.eq("claimId", claim.getId()));
            criteria.createCriteria("claim").add(Restrictions.eq("id", claim.getId()));
            criteria.addOrder(Order.asc("claim.id"));           
            comments = findByCriteria(criteria);

        } catch (Throwable e) {
            e.printStackTrace();
        }

        
        

        return comments;
    }

    public Comment getObject(int id) {
        return (Comment) get(Comment.class, id);
    }

    public void createNewObject(Comment comment) {        
        this.save(comment);
    }
}
