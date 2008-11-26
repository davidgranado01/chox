/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.Util.DateHelper;
import chox.model.Comment;
import chox.model.Claim;
import scsbre.engine.*;
import java.util.List;
import java.util.ArrayList;
import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Restrictions;

public class CommentServiceImpl extends DataService implements CommentService{
    
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
}
