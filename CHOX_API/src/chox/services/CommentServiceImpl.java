/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.model.Comment;
import chox.model.Claim;
import java.util.List;
import java.util.ArrayList;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

public class CommentServiceImpl extends DataService implements CommentService{
    
    public List<Comment> getCommentByClaim(int claimId)
    {
        Claim parentObject = new Claim();
        parentObject.setId(claimId);
        return getCommentByClaim(parentObject);
    }
    
    public List<Comment> getCommentByClaim(Claim claim){
    
        List comments = new ArrayList<Comment>();
        
        try {
            
            Criteria criteria = currentSession.createCriteria(Comment.class).add(Restrictions.eq("claim", claim));
            comments = criteria.list();
            
        } catch (Throwable e) {
           e.printStackTrace();
        }
        
        currentSession.clear();
        currentSession.disconnect();
        
        return comments;
    }
    
    public Comment getObject(int id)
    {
        return (Comment)currentSession.get(Comment.class, id);
    }
    
    public void createNewObject(Comment comment)
    {
        currentSession.beginTransaction();
        currentSession.update(comment);
        currentSession.getTransaction().commit();
    }
}
