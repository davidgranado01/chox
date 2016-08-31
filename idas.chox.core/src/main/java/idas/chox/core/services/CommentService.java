package idas.chox.core.services;

import java.util.List;

import idas.chox.core.model.Comment;
import idas.chox.core.model.Claim;

public interface CommentService {

    List<Comment> getCommentByClaimId(int claimId);

    List<Comment> getCommentByClaimIdFilterByOrg(int claimId, String orgType);

    List<Comment> getCommentByClaim(Claim claim);

    Comment getComment(int commentId);
    
    void deleteAllCommentsByClaimId(int claimId);
    
    void deleteCommentById(int commentId);

    void acknowledgeCommentById(int commentId);
}
