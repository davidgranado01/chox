/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.data.services;

import idas.chox.core.common.OrganisationType;
import idas.chox.core.model.Claim;
import idas.chox.core.model.Comment;
import idas.chox.core.services.CommentService;
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
     
    public List<Comment> getCommentByClaimIdFilterByOrg(int claimId, String orgType) {

        List comments = new ArrayList<Comment>();

        try {
            
            DetachedCriteria criteria = DetachedCriteria.forClass(Comment.class);
            criteria.createCriteria("claim").add(Restrictions.eq("id", claimId));

            // 0 - ALL, 1 - INSURER ONLY, 2 - CREDIT HIRE ONLY
            if(orgType.equalsIgnoreCase(OrganisationType.INS)){
                // INSURER User SHOULD ABLE TO SEE ALL NOT CREDITHIRE's Notes ONLY
                criteria.add(Restrictions.ne("visibilityType", 2));
            }else if(orgType.equalsIgnoreCase(OrganisationType.CHO)){
                // CREDITHIRE User SHOULD ABLE TO SEE ALL NOT INSURER's Notes ONLY
                criteria.add(Restrictions.ne("visibilityType", 1));
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
