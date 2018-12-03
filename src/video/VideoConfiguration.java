package video;

import test.TestVideoConfiguration;

public class VideoConfiguration {
    public static final int NUM_PARAMS = 10;

    public String file_output_path;
    
    public String stream_id;
    public String stream_name;
    public int stream_cam_num;
    public String stream_cam_url;
    
    public int video_length_sec;
    public int video_fps;
    public int video_width;
    public int video_height;
    public boolean video_gray_scale;
    public boolean video_inverted;
    
    public static boolean areParamsValid(String[] args) {
        if (args.length != NUM_PARAMS) return false;
        //TODO: Add parameter checking. (ranges, etc.)
        return true;
    }
    
    public static VideoConfiguration parseInputOrDefault(String[] args){
        return (VideoConfiguration.areParamsValid(args)) 
          ? new VideoConfiguration(args) : TestVideoConfiguration.getInstance();

    }
    
    public VideoConfiguration(String[] args){
        this.stream_id = args[0];
        this.stream_name = args[1];
        this.stream_cam_num = Integer.parseInt(args[2]);
        this.stream_cam_url = args[3];
        this.video_length_sec = Integer.parseInt(args[4]);
        this.video_fps = Integer.parseInt(args[5]);
        this.video_width = Integer.parseInt(args[6]);
        this.video_height = Integer.parseInt(args[7]);
        this.video_gray_scale = Boolean.getBoolean(args[8]);
        this.video_inverted = Boolean.getBoolean(args[9]);
        
        file_output_path = System.getProperty("user.dir") + "\\" + stream_id;
    }
    
    public VideoConfiguration(String id,
                    String name,
                    int camNum,
                    String camUrl,
                    int videoLengthInSec,
                    int videoFps,
                    Integer xPixels,
                    int yPixels,
                    boolean isGrayScale,
                    boolean isInverted){
        
        this.stream_id = id;
        this.stream_name = name;
        this.stream_cam_num = camNum;
        this.stream_cam_url = camUrl;
        this.video_length_sec = videoLengthInSec;
        this.video_fps = videoFps;
        this.video_width = xPixels;
        this.video_height = yPixels;
        this.video_gray_scale = isGrayScale;
        this.video_inverted = isInverted;
        
        file_output_path = System.getProperty("user.dir") + "\\" + id;
    }
}
