/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.core.services;

import idas.chox.core.model.Comment;
import java.util.List;
import idas.chox.core.model.Claim;

public interface CommentService {

    public List<Comment> getCommentByClaimId(int claimId);

    public List<Comment> getCommentByClaimIdFilterByOrg(int claimId, String orgType);

    public List<Comment> getCommentByClaim(Claim claim);

    Comment getObject(int id);

    public void createNewObject(Comment comment);
}
