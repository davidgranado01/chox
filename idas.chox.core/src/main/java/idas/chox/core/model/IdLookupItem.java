package idas.chox.core.model;

/**
 *
 * @author Emmanuel
 */
public class IdLookupItem {

    private Integer id;
    private String name;

    public IdLookupItem() {
        this.id = -1;
        this.name = "";
    }

    public IdLookupItem(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    

}
