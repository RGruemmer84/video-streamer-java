package image;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Filters {
    private Filters() {}
    
    public static ImageFilter createFilter(FilterTypes t) throws 
            ClassNotFoundException, InstantiationException, IllegalAccessException{
        return (ImageFilter) Class.forName(t.getClassName()).newInstance();
    }
    
    public static List<ImageFilter> copySet(List<ImageFilter> oldSet){
        List<ImageFilter> newSet = new LinkedList<>();
        oldSet.stream().forEach((ImageFilter oldFilter) -> {
            try {
                newSet.add(oldFilter.copy());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(Filters.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        return newSet;
    }
}
