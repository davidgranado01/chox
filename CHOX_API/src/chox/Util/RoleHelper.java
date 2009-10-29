/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.Util;

import chox.model.WebUser;
import chox.model.WebUserRole;
import java.util.Iterator;
import java.util.Set;


public class RoleHelper {

   public static boolean isOwnWorkgroupRolesOnly(WebUser user){

        boolean bFlag = false;
        if((isCheckSelectedRoleExist(user.getRoles(), WebUserRole.ROLE_CH) || isCheckSelectedRoleExist(user.getRoles(), WebUserRole.ROLE_COM))
            && !hasNoneWorkgroupEnableRole(user.getRoles())
        ){
            bFlag = true;
        }

        return bFlag;
   }
   
   public static boolean isClaimHandlerRoleOnly(WebUser user){

        boolean bFlag = false;

        if((user.getRoles().size()<=2)
            && isCheckSelectedRoleExist(user.getRoles(), WebUserRole.ROLE_CH)){
            bFlag = true;
        }

        return bFlag;
    }

   public static boolean isChoxAdmin(WebUser user){

        boolean bFlag = false;

        if(isCheckSelectedRoleExist(user.getRoles(), WebUserRole.ROLE_CHOX)){
            bFlag = true;
        }

        return bFlag;
    }

   public static boolean isInsurerUser(WebUser user){

        boolean bFlag = false;

        if(isCheckSelectedRoleExist(user.getRoles(), WebUserRole.ROLE_INS)){
            bFlag = true;
        }

        return bFlag;
    }

   public static boolean isCreditHireUser(WebUser user){

        boolean bFlag = false;

        if(isCheckSelectedRoleExist(user.getRoles(), WebUserRole.ROLE_CHO)){
            bFlag = true;
        }

        return bFlag;
    }

   public static boolean hasNoneWorkgroupEnableRole(Set roles){
       
        boolean bFlag = false;

        if(roles!=null){

            if (roles.size() > 0){

                Iterator itr = roles.iterator();

                while (itr.hasNext()) {

                    WebUserRole webUserrole = (WebUserRole) itr.next();

                    if (!webUserrole.getName().equalsIgnoreCase(WebUserRole.ROLE_CH)
                            && !webUserrole.getName().equalsIgnoreCase(WebUserRole.ROLE_COM)
                            && !webUserrole.getName().equalsIgnoreCase(WebUserRole.ROLE_INS)
                            ) {
                        bFlag = true;
                        break;
                    }
                }
            }
        }
        return bFlag;
        
   }
   
    public static boolean isCheckSelectedRoleExist(Set roles, String roleName){

        boolean bFlag = false;

        if(roles!=null){

            if (roles.size() > 0){

                Iterator itr = roles.iterator();

                while (itr.hasNext()) {

                    WebUserRole webUserrole = (WebUserRole) itr.next();

                    if (webUserrole.getName().equalsIgnoreCase(roleName)) {
                        bFlag = true;
                        break;
                    }

                }
            }
        }

        return bFlag;

    }
    
}
