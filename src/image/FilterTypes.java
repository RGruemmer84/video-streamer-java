/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package image;

/**
 *
 * @author home
 */
public enum FilterTypes {
    PASS_THRU       ("PASS_THRU", "PassThruFilter"),
    BLUR            ("BLUR", "BlurFilter"),
    GAUSS_BLUR      ("GAUSS_BLUR", "GaussianFilter"),
    MEDIAN_BLUR     ("MEDIAN_BLUR", "MedianFilter"),
    BILATERAL       ("BILATERAL", "BilateralFilter"),
    SOBEL           ("SOBEL", "SobelFilter"),
    LAPLACIAN       ("LAPLACIAN", "LaplaicianFilter"),
    MORPH           ("MORPH", "MorphFilter"),
    ESSENTIAL       ("ESSENTIAL", "EssentialFilter");

    private final String type;
    private final String className;

    FilterTypes(String t, String cn){
        type = t;
        className = cn;
    }

    @Override
    public String toString() {return type;}
    public String getClassName() {return className;} 
}