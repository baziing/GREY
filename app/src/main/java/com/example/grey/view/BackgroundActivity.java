package com.example.grey.view;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.grey.R;
import com.example.grey.model.Background;
import com.example.grey.sensor.ChangeOrientationHandler;
import com.example.grey.sensor.OrientationSensorListener;
import com.example.grey.model.PathGetter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class BackgroundActivity extends AppCompatActivity {

    private ImageView picture;
    private Uri imageUri;
    private Uri cropImgUri;
    public static final int TAKE_PHOTO = 1;
    public static final int CROP_PHOTO = 2;
    public static final int GET_PERMISSION = 3;
    public static final int CHOOSE_PHOTO=4;
    private Toolbar toolbar;
    private Handler handler;
    private OrientationSensorListener listener;
    private SensorManager sm;
    private Sensor sensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background);

        handler = new ChangeOrientationHandler(this);
        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        listener = new OrientationSensorListener(handler);
        sm.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_keyboard_backspace_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                BackgroundActivity.this.finish();
            }
        });

        picture=(ImageView)findViewById(R.id.picture);
        //查询数据
        BmobQuery<Background> backgroundBmobQuery=new BmobQuery<>();
        backgroundBmobQuery.addWhereEqualTo("user",BmobUser.getCurrentUser());
        backgroundBmobQuery.findObjects(new FindListener<Background>() {
            @Override
            public void done(List<Background> list, BmobException e) {
                if(e==null){
                    String imageUrl=list.get(0).getUrl();
                    Toast.makeText(BackgroundActivity.this, imageUrl, Toast.LENGTH_SHORT).show();
                    Glide.with(BackgroundActivity.this).load(imageUrl).into(picture);
                }else{
                    Toast.makeText(BackgroundActivity.this, "进行添加", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //屏蔽7.0中使用 Uri.fromFile爆出的FileUriExposureException
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= 24) {
            builder.detectFileUriExposure();
        }
        picture = findViewById(R.id.picture);

        Button chooseFromAlbum=(Button)findViewById(R.id.choose_from_album);

        chooseFromAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(BackgroundActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(BackgroundActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }else{
                    openAlbum();
                }
            }
        });

    }

    private void openAlbum(){
        Intent intent=new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);
    }


    public void takePhoto(View view) {
       /* if (Build.VERSION.SDK_INT >= 23) {
            boolean hasPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED;
            if (hasPermission ) {
                openCamera();
            } else {
                showDialog("拍照需要获取存储权限", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(MainActivity2.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GET_PERMISSION);
                    }
                });
            }
        } else {
            openCamera();
        }*/
        //如果操作的是私有目录,可以不用申请权限
        openCamera();

    }

    private void openCamera() {
//        File outputImage = new File(Environment.getExternalStorageDirectory(), "output_image.jpg");
        File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
        try {
            if (outputImage.exists()) {
                outputImage.delete();
                outputImage.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageUri = Uri.fromFile(outputImage);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TAKE_PHOTO: //处理拍照返回结果
                    startPhotoCrop();
                    break;
                case CROP_PHOTO://处理裁剪返回结果
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(cropImgUri));
                        picture.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
                case CHOOSE_PHOTO:
                    if(resultCode==RESULT_OK){
                        if(resultCode==RESULT_OK){
                            handleImageOnKitKat(data);
                        }else{
                            handleImageBeforeKitKat(data);
                        }
                    }
                    break;
            }
        }
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data){
        String imagePath=null;
        Uri uri=data.getData();
        if(DocumentsContract.isDocumentUri(this,uri)){
            String docId=DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id=docId.split(":")[1];
                String selection=MediaStore.Images.Media._ID+"="+id;
                imagePath=getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri= ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath=getImagePath(contentUri,null);
            }
        }else if("content".equalsIgnoreCase(uri.getScheme())){
            imagePath=getImagePath(uri,null);
        }else if("file".equalsIgnoreCase(uri.getScheme())){
            imagePath=uri.getPath();
        }

        File file = new File(PathGetter.getPath(this, uri));

        //查找相册内的图片
        if (file.exists() && file.isFile()){
            if(file.length()>524288){
                Toast.makeText(BackgroundActivity.this, "要求图片小于500KB", Toast.LENGTH_SHORT).show();
                return;//>500KB
            }
        }else{
            Toast.makeText(BackgroundActivity.this, "文件不存在", Toast.LENGTH_SHORT).show();
        }

        //查询数据
        BmobQuery<Background>backgroundBmobQuery=new BmobQuery<>();
        backgroundBmobQuery.addWhereEqualTo("user",BmobUser.getCurrentUser());
        backgroundBmobQuery.findObjects(new FindListener<Background>() {
            @Override
            public void done(List<Background> list, BmobException e) {
                if(e==null){
                    Toast.makeText(BackgroundActivity.this, "查询成功", Toast.LENGTH_SHORT).show();
                    String mobject=list.get(0).getObjectId();
                    deleteBackeground(mobject);
                }else{
                    Toast.makeText(BackgroundActivity.this, "进行添加", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //压缩图片
        Luban.with(this)
                .load(file)
                .ignoreBy(50)
                .setTargetDir(Environment.getExternalStorageDirectory().getAbsolutePath() + "/grey/compress/" )
                .filter(new CompressionPredicate() {
                    @Override
                    public boolean apply(String path) {
                        return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                    }
                })
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                        // TODO 压缩开始前调用，可以在方法内启动 loading UI
                        Toast.makeText(BackgroundActivity.this, "开始", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(File file) {
                        // TODO 压缩成功后调用，返回压缩后的图片文件
                        Toast.makeText(BackgroundActivity.this, "压缩成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO 当压缩过程出现问题时调用
                    }
                }).launch();





        //保存数据new BmobFile(file);
        final BmobFile bmobFile=new BmobFile(file);
        bmobFile.uploadblock(new UploadFileListener() {

            @Override
            public void done(BmobException e) {
                if(e==null){
                    //bmobFile.getFileUrl()--返回的上传文件的完整地址
                    Toast.makeText(BackgroundActivity.this, "上传文件成功:" + bmobFile.getFileUrl(), Toast.LENGTH_SHORT).show();
                    final Background background = new Background(bmobFile,BmobUser.getCurrentUser());//是继承了BmobObject的一个类
                    background.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if(e==null){
                                Toast.makeText(BackgroundActivity.this, "成功", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(BackgroundActivity.this, "失败：", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(BackgroundActivity.this, "上传文件失败：", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onProgress(Integer value) {
                // 返回的上传进度（百分比）
            }
        });


        displayImage(imagePath);

    }

    private void deleteBackeground(String mobject){
        Background background=new Background();
        background.delete(mobject, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Toast.makeText(BackgroundActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(BackgroundActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void handleImageBeforeKitKat(Intent data){
        Uri uri=data.getData();
        String imagePath=getImagePath(uri,null);

        File file = new File(PathGetter.getPath(this, uri));

        if (file.exists() && file.isFile()){
            if(file.length()>524288){
                Toast.makeText(BackgroundActivity.this, "要求图片小于500KB", Toast.LENGTH_SHORT).show();
                return;//>500KB
            }
        }else{
            Toast.makeText(BackgroundActivity.this, "文件不存在", Toast.LENGTH_SHORT).show();
            return;
        }


        BmobQuery<Background>backgroundBmobQuery=new BmobQuery<>();
        backgroundBmobQuery.addWhereEqualTo("user",BmobUser.getCurrentUser());
        backgroundBmobQuery.findObjects(new FindListener<Background>() {
            @Override
            public void done(List<Background> list, BmobException e) {
                if(e==null){
                    Toast.makeText(BackgroundActivity.this, "查询成功", Toast.LENGTH_SHORT).show();
                    String mobject=list.get(0).getObjectId();
                    deleteBackeground(mobject);
                }else{
                    Toast.makeText(BackgroundActivity.this, "进行添加", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final BmobFile bmobFile=new BmobFile(file);
        bmobFile.uploadblock(new UploadFileListener() {

            @Override
            public void done(BmobException e) {
                if(e==null){
                    //bmobFile.getFileUrl()--返回的上传文件的完整地址
                    Toast.makeText(BackgroundActivity.this, "上传文件成功:" + bmobFile.getFileUrl(), Toast.LENGTH_SHORT).show();
                    final Background background = new Background(bmobFile,BmobUser.getCurrentUser());//是继承了BmobObject的一个类
                    background.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if(e==null){
                                Toast.makeText(BackgroundActivity.this, "成功", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(BackgroundActivity.this, "失败：", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(BackgroundActivity.this, "上传文件失败：", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onProgress(Integer value) {
                // 返回的上传进度（百分比）
            }
        });

        displayImage(imagePath);
    }

    private String getImagePath(Uri uri,String selection){
        String path=null;
        Cursor cursor=getContentResolver().query(uri,null,selection,null,null);
        if(cursor!=null){
            if(cursor.moveToFirst()){
                path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath){
        if(imagePath!=null){
            Bitmap bitmap=BitmapFactory.decodeFile(imagePath);
            picture.setImageBitmap(bitmap);
        }else{
            Toast.makeText(this,"显示不出",Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 开启裁剪相片
     */
    public void startPhotoCrop() {
        //创建file文件，用于存储剪裁后的照片
//        File cropImage = new File(Environment.getExternalStorageDirectory(), "crop_image.jpg");
        File cropImage = new File(getExternalCacheDir(), "crop_image.jpg");
        try {
            if (cropImage.exists()) {
                cropImage.delete();
            }
            cropImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        cropImgUri = Uri.fromFile(cropImage);
        Intent intent = new Intent("com.android.camera.action.CROP");
        //设置源地址uri
        intent.setDataAndType(imageUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 500);
        intent.putExtra("outputY", 500);
        intent.putExtra("scale", true);
        //设置目的地址uri
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropImgUri);
        //设置图片格式
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("return-data", false);//data不需要返回,避免图片太大异常
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, CROP_PHOTO);
    }

    //弹窗提示
    private void showDialog(String text, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("权限申请")
                .setMessage(text)
                .setPositiveButton("确定", listener)
                .show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case 1:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }else{
                    Toast.makeText(BackgroundActivity.this,"拒绝请求",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                if (requestCode == GET_PERMISSION &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                    openCamera();
                }
        }
    }

    @Override
    protected void onPause() {
        sm.unregisterListener(listener);
        super.onPause();
    }

    @Override
    protected void onResume() {
        sm.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI);
        super.onResume();
    }
}
