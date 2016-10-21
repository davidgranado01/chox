package idas.chox.data.services;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import idas.chox.core.common.OrganisationType;
import idas.chox.core.model.Claim;
import idas.chox.core.model.Comment;
import idas.chox.core.model.Task;
import idas.chox.core.services.CommentService;

public class CommentServiceImpl extends SecureDataService implements CommentService {
    private static final Logger LOG = LoggerFactory.getLogger(CommentServiceImpl.class);
    
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
        
        if (comment.getReviewRequired() != null) {
            // Delete task for comment review
            Task task = comment.getTask();
            comment.setTask(null);
            delete(task);
        }
        this.save(comment);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    @Override
    public void acknowledgeCommentById(int commentId){
        Comment comment = getComment(commentId);
        if (comment.getReviewRequired() != null && comment.getReviewRequired() && comment.getTask() != null) {
            comment.getTask().setComplete(Boolean.TRUE);
            comment.getTask().setCompletedDate(new Date());
            comment.getTask().setCompletedBy(getCurrentUser());
            Task relatedTask = comment.getTask().getRelatedTask();
            if (relatedTask != null) {
                relatedTask.setComplete(Boolean.TRUE);
                relatedTask.setCompletedDate(new Date());
                relatedTask.setCompletedBy(getCurrentUser());
                save(relatedTask);
            }
            save(comment.getTask());
        }
        comment.setReviewRequired(false);
        this.save(comment);
    }
}
