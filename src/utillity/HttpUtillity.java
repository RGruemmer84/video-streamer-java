/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utillity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import video.VideoConfiguration;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


/**
 *
 * @author Rober
 */
public final class HttpUtillity {
    
    private static final String BACKENDLESS_APP_ID = "85CB16DE-B11F-D55B-FFDF-11D5902EB700";
    private static final String BACKENDLESS_APP_SECRET_KEY = "A6C0370B-5203-FA3B-FF3D-DE6F4184C400";
    private static final String BACKENDLESS_APP_VERSION = "v1";
    
    public HttpUtillity() {
        
    }
    
    public static void uploadFilterChain(VideoConfiguration params){
        StringBuilder sb = new StringBuilder();
        sb.append("https://api.backendless.com/");
        sb.append(BACKENDLESS_APP_VERSION);
        sb.append("/files/lynes/");
        sb.append(params.stream_id);
        sb.append("/filter_chain?overwrite=true");
        String url = sb.toString();
        
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost httppost = new HttpPost(url);
            
            httppost.setHeader("application-id", BACKENDLESS_APP_ID);
            httppost.setHeader("secret-key", BACKENDLESS_APP_SECRET_KEY);
            httppost.setHeader("application-type", "REST");
            FileBody bin = new FileBody(new File(params.file_output_path));

            HttpEntity reqEntity = MultipartEntityBuilder.create()
                    .addPart("bin", bin)
                    .build();


            httppost.setEntity(reqEntity);

            System.out.println("executing request " + httppost.getRequestLine());
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    System.out.println("Response content length: " + resEntity.getContentLength());
                }
                EntityUtils.consume(resEntity);
            } finally {
                response.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(HttpUtillity.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                httpclient.close();
            } catch (IOException ex) {
                Logger.getLogger(HttpUtillity.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    public static void downloadFilterChain(VideoConfiguration params){
        StringBuilder sb = new StringBuilder();
        sb.append("https://api.backendless.com/");
        sb.append(BACKENDLESS_APP_ID);
        sb.append("/" + BACKENDLESS_APP_VERSION + "/files/lynes/");
        sb.append(params.stream_id);
        sb.append("/filter_chain");
        String url = sb.toString();
        
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(url);
        try {
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                long len = entity.getContentLength();
                InputStream inputStream = entity.getContent();
                FileOutputStream fos = new FileOutputStream(new File(params.file_output_path));
                int inByte;
                while((inByte = inputStream.read()) != -1)
                    fos.write(inByte);
               inputStream.close();
               fos.close();
               
            }
        } catch (IOException ex) {
            Logger.getLogger(HttpUtillity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void uploadClip(VideoConfiguration params){
        StringBuilder sb = new StringBuilder();
        sb.append("https://api.backendless.com/");
        sb.append(BACKENDLESS_APP_VERSION);
        sb.append("/files/lynes/");
        sb.append(params.stream_id);
        sb.append("/cam_feed_");
        sb.append(params.stream_cam_num);
        sb.append("_retry.mp4?overwrite=true");
        String url = sb.toString();
        uploadClip(url, params);
        
        
        sb = new StringBuilder();
        sb.append("https://api.backendless.com/");
        sb.append(BACKENDLESS_APP_VERSION);
        sb.append("/files/lynes/");
        sb.append(params.stream_id);
        sb.append("/cam_feed_");
        sb.append(params.stream_cam_num);
        sb.append(".mp4?overwrite=true");
        String url2 = sb.toString();
        uploadClip(url2, params);
    }
    
    private static void uploadClip(String url, VideoConfiguration params){
        
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost httppost = new HttpPost(url);
            
            httppost.setHeader("application-id", BACKENDLESS_APP_ID);
            httppost.setHeader("secret-key", BACKENDLESS_APP_SECRET_KEY);
            httppost.setHeader("application-type", "REST");

            FileBody bin = new FileBody(new File(params.file_output_path));

            HttpEntity reqEntity = MultipartEntityBuilder.create()
                    .addPart("bin", bin)
                    .build();


            httppost.setEntity(reqEntity);

            System.out.println("executing request " + httppost.getRequestLine());
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    System.out.println("Response content length: " + resEntity.getContentLength());
                }
                EntityUtils.consume(resEntity);
            } finally {
                response.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(HttpUtillity.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                httpclient.close();
            } catch (IOException ex) {
                Logger.getLogger(HttpUtillity.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
    
    
    
}
