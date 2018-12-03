/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package video;

import image.Filters;
import image.ImageFilter;
import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacpp.avutil;
import org.bytedeco.javacpp.opencv_core.IplImage;
import static org.bytedeco.javacpp.opencv_core.cvCreateImage;
import static org.bytedeco.javacpp.opencv_core.cvSize;
import static org.bytedeco.javacpp.opencv_imgproc.cvResize;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameRecorder;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import utillity.HttpUtillity;
import utillity.Timer;

public class VideoStreamer implements Runnable{
    protected VideoConfiguration configuration;
    protected Queue<Frame> imageBuffer = new LinkedList<>();
    protected List<ImageFilter> filterSet = Collections.emptyList();
    protected List<ImageFilter> filterSetBuffer = Collections.emptyList();
    
    public VideoStreamer(VideoConfiguration p){
        configuration = p;
        new File(p.file_output_path).mkdir();
        initializeFilters();
    }
    
    private void initializeFilters() {
        //TODO:  builder filter sets from parsed files
        //        or a default set.
    }
    
    public Queue<Frame> getFrameBuffer() {
        return imageBuffer;
    }
    
    public List<ImageFilter> getFilterSetBuffer() {
        return filterSetBuffer;
    }
    
     public VideoConfiguration getParams() {
        return configuration;
    }
    
    @Override
    public void run() {
        OpenCVFrameGrabber frameGrabber;
        FFmpegFrameRecorder frameRecorder;
        OpenCVFrameConverter.ToIplImage frameToImage = new OpenCVFrameConverter.ToIplImage();
        
        IplImage grabbedImage, resizedImage;
        
        boolean streamActive = true;
        boolean filterSetLock = false;
        
        while (streamActive){
            try {
                frameGrabber = new OpenCVFrameGrabber(configuration.stream_cam_url);
                frameGrabber.start();
                grabbedImage = frameToImage.convert(frameGrabber.grab());
                resizedImage = cvCreateImage(
                        cvSize(configuration.video_width, configuration.video_height),
                        grabbedImage.depth(), grabbedImage.nChannels());
                cvResize(grabbedImage, resizedImage);
                
                frameRecorder = new FFmpegFrameRecorder(
                        configuration.file_output_path,
                        resizedImage.width(), 
                        resizedImage.height());
                
                frameRecorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
                frameRecorder.setPixelFormat(avutil.AV_PIX_FMT_YUV420P);
                frameRecorder.setFormat("mp4");
                frameRecorder.setFrameRate(configuration.video_fps);
                frameRecorder.setVideoBitrate(10 * 1024 * 1024);
                frameRecorder.start();

                Timer fpsTimer = new Timer();
                

                while ( (grabbedImage = frameToImage.convert(frameGrabber.grab())) != null 
                  && fpsTimer.secondsElapsed() < configuration.video_length_sec + 10) {
                    
                    resizedImage = cvCreateImage(
                        cvSize(configuration.video_width, configuration.video_height),
                        grabbedImage.depth(), grabbedImage.nChannels());
                    cvResize(grabbedImage, resizedImage);
                    
                    filterSetLock = true;
                    filterSet = Filters.copySet(filterSetBuffer);
                    filterSetLock = false;

                    for (ImageFilter f : filterSet){
                        if (f.isEnabled())
                            resizedImage = f.processFrame(resizedImage);
                    }
                    Frame outputFrame = frameToImage.convert(resizedImage);
                    imageBuffer.offer(outputFrame);
                    frameRecorder.record(outputFrame);
                    Thread.sleep((long) 1000 / configuration.video_fps );
                }
                frameRecorder.stop();
                frameGrabber.stop();

                HttpUtillity.uploadClip(configuration); //TODO change to string parameters
                
            } catch (FrameGrabber.Exception | FrameRecorder.Exception | InterruptedException ex) {
                Logger.getLogger(VideoStreamer.class.getName()).log(Level.SEVERE, null, ex);
            }
                
            
        }
        
        
    }

}
