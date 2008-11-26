/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.model.Comment;
import java.util.List;
import chox.model.Claim;
import java.util.ArrayList;

public interface CommentService {
    
    public List<Comment> getCommentByClaim(Claim claim);
}
