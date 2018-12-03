package image.filter;

import image.FilterTypes;
import image.ImageFilter;
import image.ImageFilterParameter;
import image.ImageFilterParameter.PrimitiveType;
import static image.ImageFilterParameter.PrimitiveType.DOUBLE;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.Frame;

public final class EssentialFilter extends ImageFilter{
    private static final int                CONTRAST_ALPHA               = 0;
    private static final String             CONTRAST_ALPHA_PARAM         = "contrast_alpha";
    private static final PrimitiveType      CONTRAST_ALPHA_DATA_TYPE     = DOUBLE;
    private static final double             CONTRAST_ALPHA_DEFAULT_VAL   = 1.5;
    private static final double             CONTRAST_ALPHA_START_VAL     = 0;
    private static final double             CONTRAST_ALPHA_STEP_SIZE     = 0.1;
    private static final double             CONTRAST_ALPHA_END_VAL       = 5;
    
    private static final int                BRIGHTNESS_BETA              = 1;
    private static final String             BRIGHTNESS_BETA_PARAM        = "brightness_beta";
    private static final PrimitiveType      BRIGHTNESS_BETA_DATA_TYPE    = DOUBLE;
    private static final double             BRIGHTNESS_BETA_DEFAULT_VAL  = 0.5;
    private static final double             BRIGHTNESS_BETA_START_VAL    = 0;
    private static final double             BRIGHTNESS_BETA_STEP_SIZE    = 1;
    private static final double             BRIGHTNESS_BETA_END_VAL      = 50;
    
    public EssentialFilter(){
        filterEnabled = true;
        type = FilterTypes.ESSENTIAL;
        initializeDefaults();
    }
    
    @Override
    public IplImage processFrame(IplImage mat) {
        /*ConvertScale(mat, mat, 
                parameters.get(CONTRAST_ALPHA).getCurVal(), 
                parameters.get(BRIGHTNESS_BETA).getCurVal());*/
        return mat;
    }

    @Override
    public void initializeDefaults() {
        parameters = new LinkedList<>();
        parameters.add(new ImageFilterParameter<Double>(
                            CONTRAST_ALPHA_PARAM,
                            CONTRAST_ALPHA_DATA_TYPE,
                            CONTRAST_ALPHA_DEFAULT_VAL,
                            CONTRAST_ALPHA_START_VAL,
                            CONTRAST_ALPHA_STEP_SIZE,
                            CONTRAST_ALPHA_END_VAL));
        
        parameters.add(new ImageFilterParameter<Double>(
                            BRIGHTNESS_BETA_PARAM,
                            BRIGHTNESS_BETA_DATA_TYPE,
                            BRIGHTNESS_BETA_DEFAULT_VAL,
                            BRIGHTNESS_BETA_START_VAL,
                            BRIGHTNESS_BETA_STEP_SIZE,
                            BRIGHTNESS_BETA_END_VAL));
    }
    
}
