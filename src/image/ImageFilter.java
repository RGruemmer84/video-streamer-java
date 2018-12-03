package image;

import java.util.List;
import java.util.Set;
import org.bytedeco.javacpp.opencv_core.IplImage;
import utillity.FileUtility;

public abstract class ImageFilter {
    protected boolean filterEnabled = false;
    protected FilterTypes type = FilterTypes.PASS_THRU;
    protected List<ImageFilterParameter> parameters;
    
    public void toggleEnable() {filterEnabled = !filterEnabled;}
    public boolean isEnabled() {return filterEnabled;}
    public FilterTypes getFilterType() {return type;}
    public List<ImageFilterParameter> getParams() {return parameters;}
    
    public void setParam(ImageFilterParameter param, Object value) {
        parameters.stream()
            .filter(p -> p.equals(param))
            .findFirst()
            .get()
            .setCurVal(value);
    }
    
    
    public void loadStateFromFile(FileUtility reader) {
        
    }
    
    public void saveStateToFile(FileUtility writer) {
        
    }
    
    public ImageFilter copy() throws ClassNotFoundException, 
                                InstantiationException, IllegalAccessException {
        Class c = Class.forName(type.getClassName());
        return (ImageFilter) c.newInstance();
    }
    
    public abstract IplImage processFrame(IplImage mat);
    public abstract void initializeDefaults();
}
