package image;

public class ImageFilterParameter<T> {
    
    public enum PrimitiveType{INTEGER, DOUBLE;}
      
    private final String name;
    private final PrimitiveType type;
    private final T startVal, stepSize, endVal;
    
    private T curVal;
    
    public ImageFilterParameter(String n, PrimitiveType type, T curVal, T startVal, T stepSize, T endVal){
        name = n;
        this.type = type;
        this.curVal = curVal;
        this.startVal = startVal;
        this.stepSize = stepSize;
        this.endVal = endVal;
    }

    public T getCurVal() {return curVal;}
    public void setCurVal(T curVal) {this.curVal = curVal;}

    public String getName() {return name;}
    public PrimitiveType getType() {return type;}
    public T getStartVal() {return startVal;}
    public T getStepSize() {return stepSize;}
    public T getEndVal() {return endVal;} 
    
}
