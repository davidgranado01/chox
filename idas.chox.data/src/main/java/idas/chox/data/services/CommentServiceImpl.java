package idas.chox.data.services;

import idas.chox.core.common.OrganisationType;
import idas.chox.core.model.Claim;
import idas.chox.core.model.Comment;
import idas.chox.core.services.CommentService;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Order;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class CommentServiceImpl extends SecureDataService implements CommentService {

    @Override
    public List<Comment> getCommentByClaimId(int claimId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Comment.class);
        criteria.add(Restrictions.eq("reverted", false));
        criteria.createCriteria("claim").add(Restrictions.eq("id", claimId));
        criteria.addOrder(Order.asc("createdDate"));
        criteria.addOrder(Order.asc("id"));
        return findByCriteria(criteria);
    }

    @Override
    public List<Comment> getCommentByClaimIdFilterByOrg(int claimId, String orgType) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Comment.class);
        criteria.add(Restrictions.eq("reverted", false));
        criteria.createCriteria("claim").add(Restrictions.eq("id", claimId));

        // 0 - ALL, 1 - INSURER ONLY, 2 - CREDIT HIRE ONLY
        if (orgType.equalsIgnoreCase(OrganisationType.INS)) {
            // INSURER User SHOULD ABLE TO SEE ALL NOT CREDITHIRE's Notes ONLY
            criteria.add(Restrictions.ne("visibilityType", 2));
        } else if (orgType.equalsIgnoreCase(OrganisationType.CHO)) {
            // CREDITHIRE User SHOULD ABLE TO SEE ALL NOT INSURER's Notes ONLY
            criteria.add(Restrictions.ne("visibilityType", 1));
        }

        criteria.addOrder(Order.asc("createdDate"));
        criteria.addOrder(Order.asc("id"));

        return findByCriteria(criteria);
    }

    @Override
    public List<Comment> getCommentByClaim(Claim claim) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Comment.class);//.add(Restrictions.eq("claimId", claim.getId()));
        criteria.add(Restrictions.eq("reverted", false));
        criteria.createCriteria("claim").add(Restrictions.eq("id", claim.getId()));
        criteria.addOrder(Order.asc("createdDate"));
        criteria.addOrder(Order.asc("id"));
        return findByCriteria(criteria);
    }

    @Override
    public Comment getComment(int id) {
        return (Comment) get(Comment.class, id);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    @Override
    public void createNewComment(Comment comment) {
        this.save(comment);
    }

    @Override
    public void deleteAllCommentsByClaimId(int claimId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Comment.class);
        criteria.createCriteria("claim").add(Restrictions.eq("id", claimId));
        List<Comment> comments = findByCriteria(criteria);
        if (comments.size() > 0) {
            for(Comment c : comments){
                c.setReverted(true);
            }
            this.saveCollections(comments);
        }
    }
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    @Override
    public void deleteCommentById(int commentId){
        Comment comment = getComment(commentId);
        comment.setReverted(true);
        this.save(comment);
    }
}
