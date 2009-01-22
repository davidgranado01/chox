package chox.services;

import chox.Util.XmlHelper;
import chox.model.Insurer;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.w3c.dom.Element;

public class InsurerServiceImpl extends DataService implements InsurerService {

    public Insurer getInsurerByName(String s) {

        Insurer insurer = new Insurer();
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(Insurer.class);
            criteria.add(Restrictions.eq("name", s));
            insurer = (Insurer) getByCriteria(criteria);

        } catch (Throwable e) {
            e.printStackTrace();
        }

        return insurer;
    }

    public Insurer getInsurerByNodeName(Element thisElement, String nodeName) {
        Insurer insurer = new Insurer();

        if (XmlHelper.isNotNull(XmlHelper.getNodeValue(thisElement, nodeName))) {
            insurer = getInsurerByName(XmlHelper.getNodeValue(thisElement, nodeName));
        }

        return insurer;
    }

    public Insurer getObject(int id) {
        return (Insurer) get(Insurer.class, id);
    }
}
