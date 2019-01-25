package lab.ourteam.newlab;

import android.content.ContentValues;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class postToTomcat {
    private String Url;
    private HttpURLConnection connection = null;
    private static final String BOUNDARY = UUID.randomUUID().toString();
    public postToTomcat(String Url) {
        this.Url = Url;
    }

    private void doPost() {
        try {
            URL url = new URL(Url);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setReadTimeout(80000);
            connection.setConnectTimeout(80000);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            //post方式不能设置缓存，需手动设置为false
            connection.setUseCaches(false);
            connection.setRequestProperty("Charset", "UTF-8");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary="+BOUNDARY);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void uploadPhoto(final  String filePath,final String tag) {//tag=isPortrait
        new Thread(new Runnable() {
            @Override
            public void run() {
                doPost();
                String rsp = "";
                File file = new File(filePath);
                String filename = file.getName();
                String contentType = "";
                if (filename.endsWith(".png")) {
                    contentType = "image/png";
                }
                if (filename.endsWith(".jpg")) {
                    contentType = "image/jpg";
                }
                if (filename.endsWith(".gif")) {
                    contentType = "image/gif";
                }
                if (filename.endsWith(".bmp")) {
                    contentType = "image/bmp";
                }
                if (contentType == null || contentType.equals("")) {
                    contentType = "application/octet-stream";
                }
                StringBuffer strBuff = new StringBuffer();
                strBuff.append("--").append(BOUNDARY).append("\r\n")
                        .append("Content-Disposition: form-data;name=\"isPortrait\"").append("\r\n")
                        .append("Content-Type: text/plain;charset=utf-8").append("\r\n")
                        .append("Content-Transfer-Encoding: 8bit").append("\r\n").append("\r\n");
                strBuff.append(tag)
                       .append("\r\n");
                strBuff.append("--").append(BOUNDARY).append("\r\n");
                strBuff.append("Content-Dispositon: form-data; name=\"" + filePath
                        + "\"; filename=\"" + filename + "\"\r\n");
                strBuff.append("Content-Type:" + contentType + "\"\r\n\r\n");
                StringBuffer response = new StringBuffer();
                try {
                    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                    out.write(strBuff.toString().getBytes());
                    DataInputStream file_in = new DataInputStream(new FileInputStream(file));
                    int bytes = 0;
                    byte[] bufferOut = new byte[1024];
                    while ((bytes = file_in.read(bufferOut)) != -1) {
                        out.write(bufferOut, 0, bytes);
                    }
                    file_in.close();
                    out.write(("\r\n--"+BOUNDARY+"--\r\n").getBytes());
                    out.flush();
                    out.close();
                    connection.connect();
                    //读取返回信息
                    if (connection.getResponseCode() == 200) {
                        //  Toast.makeText(getApplicationContext(),"请求成功",Toast.LENGTH_SHORT).show();
                        InputStream in = connection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line).append("\n");
                        }
                        rsp = response.toString();
                        reader.close();
                        reader = null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                        connection = null;
                    }
                }
            }
        }).start();
    }
}
