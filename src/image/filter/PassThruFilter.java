/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package image.filter;

import image.ImageFilter;
import java.util.Collections;
import org.bytedeco.javacpp.opencv_core.IplImage;


/**
 *
 * @author home
 */
public class PassThruFilter extends ImageFilter{
    
    public PassThruFilter() {
        filterEnabled = true;
        parameters = Collections.emptyList();
    }

    @Override
    public IplImage processFrame(IplImage mat) {
        return mat;
    }

    @Override
    public void initializeDefaults() {
        
    }
    
    
}
