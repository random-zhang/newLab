package lab.ourteam.newlab;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * AsyncTask类的三个泛型参数：
 * （1）Param 在执行AsyncTask是需要传入的参数，可用于后台任务中使用
 * （2）后台任务执行过程中，如果需要在UI上先是当前任务进度，则使用这里指定的泛型作为进度单位
 * （3）任务执行完毕后，如果需要对结果进行返回，则这里指定返回的数据类型
 * AsyncTask<Params,Progress,Result>
 * Params:启动任务时输入的参数类型.
 * Progress:后台任务执行中返回进度值的类型.
 * Result:后台任务执行完成后返回结果的类型.
 */
 public  class MyAsyncTask extends AsyncTask<Integer, Integer, String> {
    private String Url;
    private Context context;
    private String phone;
    private String password;

    public  MyAsyncTask(String url, Context context) {
        this.Url = url;
        this.context = context;
    }

    MyAsyncTask(String url, Context context, String phone, String password) {
        this.Url = url;
        this.context = context;
        this.phone = phone;
        this.password = password;
    }

    @Override
    protected void onPreExecute() {//执行后台耗时操作前被调用,通常用于进行初始化操作.
        super.onPreExecute();
        //time_start=System.currentTimeMillis();
    }

    /**
     * @param params 这里的params是一个数组，即AsyncTask在激活运行时调用execute()方法传入的参数
     */
    @Override
    protected String doInBackground(Integer... params) { //异步执行后台线程要完成的任务,耗时操作将在此方法中完成.
        switch (params[0]) {
            case 1: {
                Log.w("AsyncTask", "doInBackground");
                HttpURLConnection connection = null;
                StringBuilder response = new StringBuilder();
                /**
                 *      openConnection→setRequsetMrthod
                 *      InputStream保存从url获取的流
                 *      InputStream→InputStreamRead→BufferedReader
                 */
                try {
                    URL url = new URL(Url);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(80000);
                    connection.setReadTimeout(80000);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return response.toString();// 这里返回的结果就作为onPostExecute方法的入参
            }
            case 2: {
                HttpURLConnection connection = null;
                StringBuilder response = new StringBuilder();
                //Toast.makeText(getApplicationContext(),"开始发送请求",Toast.LENGTH_SHORT).show();
                //Post方式提交
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
                    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                    String str = "phone=" + URLEncoder.encode(phone, "UTF-8") + "&is_use_password=true" + "password=" + URLEncoder.encode(password, "UTF-8");
                    out.writeBytes(str);
                    out.flush();
                    out.close();
                    connection.connect();
                    if (connection.getResponseCode() == 200) {
                        //  Toast.makeText(getApplicationContext(),"请求成功",Toast.LENGTH_SHORT).show();
                        InputStream in = connection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        in.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return response.toString();
            }
        }
            return "";
    }
    @Override
    protected void onProgressUpdate(Integer... values) {
        // 如果在doInBackground方法，那么就会立刻执行本方法
        // 本方法在UI线程中执行，可以更新UI元素，典型的就是更新进度条进度，一般是在下载时候使用
        //当在doInBackground方法中调用publishProgress方法更新任务执行进度后,将调用此方法.通过此方法我们可以知晓任务的完成进度.
    }
    /**
     * 运行在UI线程中，所以可以直接操作UI元素
     * @param s
     */
    @Override
    protected void onPostExecute(String s) {//当doInBackground方法完成后,系统将自动调用此方法,
        // 并将doInBackground方法返回的值传入此方法.通过此方法进行UI的更新.
        Log.w("WangJ", "task onPostExecute()");
        Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
        //Toast.makeText(register_Activity.this,s,Toast.LENGTH_SHORT).show();
        //register_finish_button.setEnabled(true);
        //  tv.setText(s);
    }

}
