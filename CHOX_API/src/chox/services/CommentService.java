/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.model.Comment;
import java.util.List;
import chox.model.Claim;

public interface CommentService {
    public List<Comment> getCommentByClaimId(int claimId);
    public List<Comment> getCommentByClaim(Claim claim);
    Comment getObject(int id);
    public void createNewObject(Comment comment);
}
