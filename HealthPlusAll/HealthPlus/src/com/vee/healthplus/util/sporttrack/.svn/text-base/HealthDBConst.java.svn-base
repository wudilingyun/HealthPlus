package com.vee.healthplus.util.sporttrack;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class HealthDBConst {
    public static final String HEALTHDB_NAME = "healthplus.db";
    public static final int HEALTHDB_VERSION = 1;
    public static final String SPORTRECORD_INDEX_TABLE = "sportrecordindex";
    public static final String SPORTRECORD_TABLE = "sportrecordbygps";
    public static final String SPORTRECORD__PEDO_TABLE = "sportrecordbypedo";
    public static final String SPORTTYPE_TABLE = "sporttype";

    //添加心率表名称及结构
    public static final String HealthHeart_TABLE = "heartrate";
    public static final String HealthHeart_INDEX_ITEM = "("
            + "heartrate text,"
            + "time long,"
            + "userid text)";
    
    public static final String SPORTRECORD_INDEX_ITEM = "("
            + "id integer,"
            + "sportid integer,"
            + "time text,"
            + "modeid integer,"
            + "userid text,"
            +"sync integer)";

    public static final String SPORTRECORD_ITEM = "("
            + "id integer,"
            + "duration text,"
            + "distance text,"
            + "calory text,"
            + "velocity text,"
            + "lon text,"
            + "lat text,"
            +"userid text)";
    public static final String SPORTTYPE_ITEM = "("
            + "id integer primary key autoincrement,"
            + "name varchar(20))";

    public static final String CALORY_DB = "healthcalory.db";
    public static final String CALORY_TABLE = "calory";
    public static final String CALORY_ITEM = "("
            + "sportid integer primary key autoincrement,"
            + "value varchar(20))";

    public static void copyDatabase(Context context, String path, Handler handler) throws Exception {
        try {
            File dbfile = new File(path);

            File dir = dbfile.getParentFile();
            if (dir.exists() == false)
                dir.mkdirs();
            if (dbfile.exists())
                dbfile.delete();

            // AssetManager assetManager = context.getAssets();
            // assetManager.open(FilterConst.FILTERDB_NAME);
            // InputStream is =context.getResources().openRawResource(R.raw.harassfilter);
            InputStream is = context.getClass().getResourceAsStream("/assets/" + HealthDBConst.CALORY_DB);
            FileOutputStream fos = new FileOutputStream(dbfile);

            byte[] buffer = new byte[1024];
            int size = 0;
            int length = 0; // �ֽ�
            while ((length = is.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
                size += length;
            }
            fos.flush();
            fos.close();
            is.close();

            Message msg = new Message();
            msg.what = 1;
            msg.setTarget(handler);
            handler.sendMessage(msg);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
