/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utillity;

/**
 *
 * @author Rober
 */
public class Timer {
    private long startTime;
    private long elapsedTime;
    
    public Timer(){
        startTime = System.nanoTime();
        elapsedTime = System.nanoTime();
    }
    
    public double secondsElapsed(){
        elapsedTime = System.nanoTime() - startTime;
        return elapsedTime / Math.pow(10, 9);
    }
}
