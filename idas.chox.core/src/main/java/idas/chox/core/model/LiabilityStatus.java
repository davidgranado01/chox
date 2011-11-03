package idas.chox.core.model;

/**
 *
 * @author abrar
 */
public enum LiabilityStatus {

    LIABILITY_NULL{
        @Override
        public String toString(){
            return null;
        }
    },
    LIABILITY_ACCEPTED{
        @Override
        public String toString(){
            return "Full Liability Accepted";
        }

        public String description(){
            return "Indicates that the Third party Insurer is accepting 100% liability for the claim.";
        }
    },
    LIABILITY_DISPUTED{
        @Override
        public String toString(){
            return "Liability In Negotiation";
        }
    },
    LIABILITY_UNKNOWN{
        @Override
        public String toString(){
            return "Liability Unknown";
        }
    },
    LIABILITY_REPUDIATED{
        @Override
        public String toString(){
            return "Liability Repudiated";
        }
    },
    LIABILITY_SPLIT{
        @Override
        public String toString(){
            return "Liability Split";
        }
    },
    PROCEED_WITHOUT_PREJUDICE{
        @Override
        public String toString(){
            return "Proceed Without Prejudice";
        }
    };


    
     /*
    public Map getMap(){

         m= new HashMap();
        return m;
    }
      
      */
}
