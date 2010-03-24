package idas.chox.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;



public class ListUtils {
	
	public static <T> List<T> filter(Collection<T> target, Predicate<T> predicate) {
	    List<T> result = new ArrayList<T>();
	    for (T element: target) {
	        if (predicate.apply(element)) {
	            result.add(element);
	        }
	    }
	    return result;
	}
	public interface Predicate<T> { boolean apply(T type); }
}

