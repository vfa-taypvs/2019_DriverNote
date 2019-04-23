package app.camnanglaixe.com.android.Common;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintManager;
import android.provider.Settings;
import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * Created by taypham on 30/11/2016.
 */
public class CommonUtils {

    public static String loadJSONFromAsset(Context context, String fileName) {
        String json = null;
        try {

            InputStream is = context.getAssets().open(fileName);

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static boolean isOnline(Context context) {
        if (context == null) {
            return false;
        }
        final ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnectedOrConnecting()) {
            return false;
        }
        return true;
    }

    public static void saveObjectToFile(Context context, Object object, String fileName) {
        // Convert topic to String and save
        Gson gson = new Gson();
        String json = gson.toJson(object);
        // write text to file
        try {
            FileOutputStream fileout = context.openFileOutput(fileName, context.MODE_PRIVATE);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
            outputWriter.write(json);
            outputWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getColorResourceByName(Context context, String colorName) {
        return context.getResources().getColor(context.getResources().getIdentifier(colorName, "color", context.getPackageName()));
    }

    public static Drawable getDrawableResourceByName(Context context, String fileName) {
        try {
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                return context.getResources().getDrawable(context.getResources().getIdentifier(fileName, "drawable", context.getPackageName()));
            } else {
                return context.getResources().getDrawable(context.getResources().getIdentifier(fileName, "drawable", context.getPackageName()), null);
            }
        }catch (NoSuchMethodError e){
            e.printStackTrace();
            return null;
        }catch (Resources.NotFoundException e){
            e.printStackTrace();
            return null;
        }
    }

    public static void clearPreferencesTopics(Context context){
        for(int i=0; i<Constanst.NUM_OF_TOPICS; i++)
            PreferenceUtils.clearKeyPreferences(context, PreferenceUtils.TOPIC_NUMBER+i);
    }

    public static String getFileNameFromUrl(String url){
        String[] splitLink = url.split("/");
        return splitLink[splitLink.length-1];
    }

    public static String createFilePathFromUrl(String url, String branch, boolean isIncludeFile){
        if(isIncludeFile)
            return Environment.getExternalStorageDirectory().getAbsolutePath()
                    + branch +  File.separator + getFileNameFromUrl(url);
        else
            return Environment.getExternalStorageDirectory().getAbsolutePath()
                    + branch;
    }

    public static String createFilePath(String fileName, String branch, boolean isIncludeFile){
        if(isIncludeFile)
            return Environment.getExternalStorageDirectory().getAbsolutePath()
                    + branch +  File.separator + fileName;
        else
            return Environment.getExternalStorageDirectory().getAbsolutePath()
                    + branch;
    }

    public static boolean isSavedTopics(Context context){
        for(int i = 0; i < Constanst.NUM_OF_TOPICS; i++){
            if(PreferenceUtils.getString(context, PreferenceUtils.TOPIC_NUMBER + i).equals("")){
                Log.d("TayPVS", "TayPVS -  false" );
                return false;
            }
        }
        return true;
    }

    public static String getDeviceId(Context context){
        String android_id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.d("TayPVS", "TayPVS - getDeviceId : " + android_id) ;
        return android_id;
    }

    public static void openWebPage(Context context, String url) {
        Uri webpage = Uri.parse(url);
        Log.d("TayPVS", "TayPVS - webpage : " + webpage.toString());
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    public static void clearFolderApp(){
        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + Constanst.FILE_DRIVER_DOWNLOAD_MAIN_PDF);
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++)
            {
                new File(dir, children[i]).delete();
            }
        }
    }

    public static String getAppVersion(Context context){
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            String version = pInfo.versionName;
            return version;
        } catch (Exception e){
            return  "";
        }
    }

    public static boolean isHaveNewVersion(String googleVersion, String currentVersion){
        try{
            String[] gVerionSpl = googleVersion.split("\\.");
            String[] cVersionSpl = currentVersion.split("\\.");

            for(int i = 0; i < 3; i++){
                int gVersionInt = Integer.parseInt(gVerionSpl[i]);
                int cVersionInt = Integer.parseInt(cVersionSpl[i]);

                if(gVersionInt > cVersionInt)
                    return true;
                else if (gVersionInt < cVersionInt)
                    return false;
            }

        } catch (Exception e){

        }
        return false;
    }

    public static void openAppRating(Context context) {
        Intent rateIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("market://details?id=" + context.getPackageName()));
        boolean marketFound = false;

        // find all applications able to handle our rateIntent
        final List<ResolveInfo> otherApps = context.getPackageManager().queryIntentActivities(rateIntent, 0);
        for (ResolveInfo otherApp : otherApps) {
            // look for Google Play application
            if (otherApp.activityInfo.applicationInfo.packageName.equals("com.android.vending")) {

                ActivityInfo otherAppActivity = otherApp.activityInfo;
                ComponentName componentName = new ComponentName(otherAppActivity.applicationInfo.packageName,
                        otherAppActivity.name);
                rateIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                rateIntent.setComponent(componentName);
                context.startActivity(rateIntent);
                marketFound = true;
                break;
            }
        }

        // if GP not present on device, open web browser
        if (!marketFound) {
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + context.getPackageName()));
            context.startActivity(webIntent);
        }
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public static void printPDFFile(final Context context, final String printFile){
        try {
            PrintManager printManager = (PrintManager) context.getSystemService(Context.PRINT_SERVICE);
            String jobName = "Cam Nang Nguoi Lai Xe";
            PrintDocumentAdapter pda = new PrintDocumentAdapter() {

                @Override
                public void onWrite(PageRange[] pages, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, WriteResultCallback callback) {
                    InputStream input = null;
                    OutputStream output = null;

                    try {
                        input = context.getAssets().open(printFile);
                        output = new FileOutputStream(destination.getFileDescriptor());

                        byte[] buf = new byte[1024];
                        int bytesRead;

                        while ((bytesRead = input.read(buf)) > 0) {
                            output.write(buf, 0, bytesRead);
                        }
                        callback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});

                    } catch (FileNotFoundException e) {
                        //Catch exception
                        Log.d("TayPVS" ,"TayPVS - FileNotFoundException : " + e.getMessage());
                    } catch (Exception e) {
                        //Catch exception'
                        e.getStackTrace();
                    } finally {
                        try {
                            input.close();
                            output.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, LayoutResultCallback callback, Bundle extras) {

                    if (cancellationSignal.isCanceled()) {
                        callback.onLayoutCancelled();
                        return;
                    }

//                int pages = computePageCount(newAttributes);

                    PrintDocumentInfo pdi = new PrintDocumentInfo.Builder(printFile).setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT).build();
                    callback.onLayoutFinished(pdi, true);
                }
            };
            printManager.print(jobName, pda, null);
        }
        catch (Exception e){

        }
    }
}
