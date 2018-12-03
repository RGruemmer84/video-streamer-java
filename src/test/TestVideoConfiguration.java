package test;

import video.VideoConfiguration;

public abstract class TestVideoConfiguration {
    
    private static final String   TEST_ID =              "15C6DF5C-3967-A3AE-FFCE-E60C2DF0B300";
    private static final String   TEST_NAME =            "Hogs Breath Saloon";
    private static final int      TEST_CAM_NUM =         0;
    private static final String   TEST_CAM_URL =         "http://wse1.resortcams.com:80/live-audio/hogsbreath1.stream_aac/playlist.m3u8";
    private static final int      TEST_VID_LEN_SEC =     30;
    private static final int      TEST_VID_FPS =         25;
    private static final int      TEST_X_PIXEL =         384;
    private static final int      TEST_Y_PIXEL =         218;
    private static final boolean  TEST_GRAY_SCALE =      false;
    private static final boolean  TEST_IS_INVERTED =     false;
    
    public static VideoConfiguration getInstance() {
        return new VideoConfiguration(
                TEST_ID,
                TEST_NAME,
                TEST_CAM_NUM,
                TEST_CAM_URL,
                TEST_VID_LEN_SEC,
                TEST_VID_FPS,
                TEST_X_PIXEL,
                TEST_Y_PIXEL,
                TEST_GRAY_SCALE,
                TEST_IS_INVERTED);
    }
}
