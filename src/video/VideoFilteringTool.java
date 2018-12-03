/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package video;

import test.TestVideoConfiguration;
import ui.VideoFilterTunerGUI;


public class VideoFilteringTool {

    public static void main(String[] args) {
        VideoStreamer stream = new VideoStreamer(
                VideoConfiguration.parseInputOrDefault(args));
        new Thread(stream).start();
        
        new VideoFilterTunerGUI(stream);
    }
    
}
