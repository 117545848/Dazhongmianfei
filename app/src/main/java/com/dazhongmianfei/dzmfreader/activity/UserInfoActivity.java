package com.dazhongmianfei.dzmfreader.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.R2;
import com.dazhongmianfei.dzmfreader.bean.UserInfoItem;
import com.dazhongmianfei.dzmfreader.config.ReaderApplication;
import com.dazhongmianfei.dzmfreader.config.ReaderConfig;
import com.dazhongmianfei.dzmfreader.eventbus.RefreshMine;
import com.dazhongmianfei.dzmfreader.eventbus.RefreshUserInfo;
import com.dazhongmianfei.dzmfreader.http.ReaderParams;
import com.dazhongmianfei.dzmfreader.utils.HttpUtils;
import com.dazhongmianfei.dzmfreader.utils.ImageUtil;
import com.dazhongmianfei.dzmfreader.utils.LanguageUtil;
import com.dazhongmianfei.dzmfreader.utils.MyPicasso;
import com.dazhongmianfei.dzmfreader.utils.MyToash;
import com.dazhongmianfei.dzmfreader.utils.ScreenSizeUtils;
import com.dazhongmianfei.dzmfreader.utils.ShareUitls;
import com.dazhongmianfei.dzmfreader.view.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;



//.http.RequestParams;


public class UserInfoActivity extends BaseButterKnifeActivity {
    @BindView(R2.id.user_info_avatar_container)
    View user_info_avatar_container;
    @BindView(R2.id.user_info_avatar)
    CircleImageView user_info_avatar;

    @BindView(R2.id.user_info_nickname_container)
    View user_info_nickname_container;
    @BindView(R2.id.user_info_nickname)
    TextView user_info_nickname;

    @BindView(R2.id.user_info_uid)
    TextView user_info_uid;

    @BindView(R2.id.user_info_phone_container)
    View user_info_phone_container;
    @BindView(R2.id.user_info_phone)
    TextView user_info_phone;

    @BindView(R2.id.user_info_weixin_container)
    View user_info_weixin_container;
    @BindView(R2.id.user_info_weixin)
    TextView user_info_weixin;
    @BindView(R2.id.user_info_phone_jiantou)
    ImageView user_info_phone_jiantou;
    @BindView(R2.id.user_info_weixin_jiantou)
    ImageView user_info_weixin_jiantou;


    @BindView(R2.id.titlebar_text)
    TextView titlebar_text;

    @BindView(R2.id.user_info_sex)
    TextView user_info_sex;
    @BindView(R2.id.user_info_nickname_sex)
    RelativeLayout user_info_nickname_sex;

    private EditText mEdit;
    private UserInfoItem mUserInfo;

    @OnClick(value = {R.id.titlebar_back, R.id.user_info_avatar_container,
            R.id.user_info_nickname_sex, R.id.user_info_nickname_container,
            R.id.user_info_phone_container, R.id.user_info_weixin_container})
    public void getEvent(View view) {
        switch (view.getId()) {
            case R.id.titlebar_back:
                finish();
                break;
            case R.id.user_info_avatar_container:
                //拍照  相册 上传图片
                checkUserImg(true);
                break;
            case R.id.user_info_nickname_sex:

                checkUserImg(false);
                break;
            case R.id.user_info_nickname_container:
                //修改昵称
                modifyNicknameDialog();
                break;
            case R.id.user_info_phone_container:
                //绑定手机号
                if (mUserInfo != null && mUserInfo.getBind_list() != null && mUserInfo.getBind_list().size() >= 1 && mUserInfo.getBind_list().get(0).getStatus() == 0) {
                    bindPhone();
                }
                break;
            case R.id.user_info_weixin_container:
                if (mUserInfo != null && mUserInfo.getBind_list() != null && mUserInfo.getBind_list().size() >= 2 && mUserInfo.getBind_list().get(1).getStatus() == 0) {
                    ShareUitls.putBoolean(activity, "BANGDINGWEIXIN", true);
                    UMShareAPI.get(activity).deleteOauth(activity, SHARE_MEDIA.WEIXIN, authListener);
                }
                break;

        }
    }

    private final int GALLERY = 1077;
    private final int CAMERA = 1078;
    private final int REQUEST_CROP = 1079;
    public static Activity activity;

    @Override
    public int initContentView() {
        return R.layout.activity_user_info;
    }

    Gson gson = new Gson();


    public void initView() {

        titlebar_text.setText(LanguageUtil.getString(activity, R.string.UserInfoActivity_title));

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }
    }


    public void initData() {
        initData(false);
    }


    public void initInfo(String json) {

        try {
            mUserInfo = gson.fromJson(json, UserInfoItem.class);
            //头像
            if (mUserInfo.getAvatar() != null) {
                MyPicasso.GlideImageHeadNoSize(activity, mUserInfo.getAvatar(), user_info_avatar);
                //ImageLoader.getInstance().displayImage(mUserInfo.getAvatar(), user_info_avatar, ReaderApplication.getOptions());
            }
            //昵称
            user_info_nickname.setText(mUserInfo.getNickname());
            if (mUserInfo.getGender() == 0) {
                user_info_sex.setText(LanguageUtil.getString(activity, R.string.UserInfoActivity_weizhi));
            } else if (mUserInfo.getGender() == 2) {
                user_info_sex.setText(LanguageUtil.getString(activity, R.string.UserInfoActivity_boy));
            } else {
                user_info_sex.setText(LanguageUtil.getString(activity, R.string.UserInfoActivity_gril));
            }
            user_info_uid.setText(mUserInfo.getUid() + "");
            int size = mUserInfo.getBind_list().size();
            if (size >= 1) {
                if (mUserInfo.getBind_list().get(0).getStatus() == 1) {
                    user_info_phone_jiantou.setVisibility(View.GONE);
                    user_info_phone.setText(mUserInfo.getBind_list().get(0).getDisplay());
                }
                if (size > 1) {
                    if (mUserInfo.getBind_list().get(1).getStatus() == 1) {
                        user_info_weixin_jiantou.setVisibility(View.GONE);
                        user_info_weixin.setText(mUserInfo.getBind_list().get(1).getDisplay());
                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void initData(final boolean flag) {

        ReaderParams params = new ReaderParams(this);
        String json = params.generateParamsJson();
        HttpUtils.getInstance(this).sendRequestRequestParams3(ReaderConfig.mUserInfoUrl, json, false, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(final String result) {
                        initInfo(result);
                        if (flag) {
                            EventBus.getDefault().post(new RefreshMine(gson.fromJson(result, UserInfoItem.class)));
                        }
                    }

                    @Override
                    public void onErrorResponse(String ex) {

                    }
                }

        );

    }


    private void checkUserImg(final boolean flag) {

        final Dialog dialog = new Dialog(this, R.style.userInfo_avatar);
        View view = View.inflate(this, R.layout.user_img_dialog, null);


        TextView checkImgGallery = view.findViewById(R.id.checkimg_gallery);


        TextView checkImgCamera = view.findViewById(R.id.checkimg_camera);


        View checkImgCancel = view.findViewById(R.id.checkimg_cancel);

        if (!flag) {
            checkImgGallery.setText(LanguageUtil.getString(activity, R.string.UserInfoActivity_gril));
            checkImgCamera.setText(LanguageUtil.getString(activity, R.string.UserInfoActivity_boy));
        }
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);


        checkImgGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (flag) {
                    String status = Environment.getExternalStorageState();
                    if (status.equals(Environment.MEDIA_MOUNTED)) {
                        checkFromGallery();
                    }
                } else {
                    modifyNickname(1);
                }
            }
        });
        checkImgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (flag) {
                    checkFromCamera();
                } else {
                    modifyNickname(2);
                }
            }
        });
        checkImgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }


    protected void checkFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        startActivityForResult(intent, GALLERY);
    }


    private File cameraSavePath;//拍照照片路径
    private Uri uri;//照片uri

    //激活相机操作
    private void checkFromCamera() {
        cameraSavePath = new File(getPath() + System.currentTimeMillis() + ".jpg");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(activity, "com.dazhongmianfei.dzmfreader.fileProvider", cameraSavePath);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(cameraSavePath);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, CAMERA);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        activity = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        MyToash.Log("onActivityResult", requestCode + "  " + resultCode);
        if (requestCode == 111) {
            initData();
        } else {//uri
            if (resultCode == RESULT_OK) {
                if (requestCode == CAMERA) {
                  //  Handle(cameraSavePath.getAbsolutePath());
                    cropPhoto(uri, true);
                } else if (requestCode == GALLERY) {
                    Uri uri = data.getData();
                    cropPhoto(uri, false);
                    //mCutUri
                    // Handle(getImagePath(uri));
                }else if (requestCode == REQUEST_CROP) {
                    Glide.with(activity).load(mCutUri).into(user_info_avatar);
                    uploadImg(uriToFile(mCutUri,activity));
                   // Handle(getImagePath(mCutUri));
                   // Uri uri = data.getData();
                   // cropPhoto(uri, false);
                    //mCutUri
                    // Handle(getImagePath(uri));
                }
            }
        }

    }
    public static File uriToFile(Uri uri,Context context) {
        String path = null;
        if ("file".equals(uri.getScheme())) {
            path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = context.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=").append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[] { MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DATA }, buff.toString(), null, null);
                int index = 0;
                int dataIdx = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    index = cur.getInt(index);
                    dataIdx = cur.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    path = cur.getString(dataIdx);
                }
                cur.close();
                if (index == 0) {
                } else {
                    Uri u = Uri.parse("content://media/external/images/media/" + index);
                    System.out.println("temp uri is :" + u);
                }
            }
            if (path != null) {
                return new File(path);
            }
        } else if ("content".equals(uri.getScheme())) {
            // 4.2.2以后
            String[] proj = { MediaStore.Images.Media.DATA };
            Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                path = cursor.getString(columnIndex);
            }
            cursor.close();

            return new File(path);
        } else {
            //Log.i(TAG, "Uri Scheme:" + uri.getScheme());
        }
        return null;
    }




    private String getPath() {
        String path = Environment.getExternalStorageDirectory() + "/manquyuedu/image/";
        File file = new File(path);
        if (file.mkdirs()) {
            return path;
        }
        return path;
    }


    public void uploadImg(File file) {
        String info = "data:image/jpeg;base64," + ImageUtil.imageToBase64(file);
        ReaderParams params = new ReaderParams(this);
        params.putExtraParams("avatar", info);
        String json = params.generateParamsJson();
        HttpUtils.getInstance(this).sendRequestRequestParams3(ReaderConfig.mUserSetAvatarUrl, json, true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(final String result) {
                        //  initData(true);
                        EventBus.getDefault().post(new RefreshMine(null));
                    }

                    @Override
                    public void onErrorResponse(String ex) {

                    }
                }

        );
    }


    private void modifyNicknameDialog() {
        final Dialog dialog = new Dialog(this, R.style.NormalDialogStyle);
        View view = View.inflate(this, R.layout.dialog_modify_nickname, null);
        TextView cancel = view.findViewById(R.id.cancel);
        TextView confirm = view.findViewById(R.id.confirm);
        mEdit = view.findViewById(R.id.modify_nickname_edit);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        //设置对话框的大小
        view.setMinimumHeight((int) (ScreenSizeUtils.getInstance(this).getScreenHeight() * 0.23f));
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (ScreenSizeUtils.getInstance(this).getScreenWidth() * 0.75f);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialogWindow.setAttributes(lp);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(mEdit.getText().toString())) {

                    MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.UserInfoActivity_namenonull));

                    return;
                }

                modifyNickname(0);
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    public void modifyNickname(final int flag) {
        String requestParams;
        if (flag == 0) {
            requestParams = ReaderConfig.mUserSetNicknameUrl;
        } else {
            requestParams = ReaderConfig.mUserSetGender;
        }
        ReaderParams params = new ReaderParams(this);
        if (flag == 0) {
            params.putExtraParams("nickname", mEdit.getText().toString());
        } else {
            params.putExtraParams("gender", flag + "");
        }
        String json = params.generateParamsJson();

        HttpUtils.getInstance(this).sendRequestRequestParams3(requestParams, json, true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(final String result) {
                        initData(true);

                    }

                    @Override
                    public void onErrorResponse(String ex) {

                    }
                }

        );
    }


    public void bindPhone() {
        startActivityForResult(new Intent(this, BindPhoneActivity.class), 111);
    }


    public IWXAPI iwxapi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=this;
        iwxapi = WXAPIFactory.createWXAPI(this, ReaderConfig.WEIXIN_PAY_APPID, true);
        iwxapi.registerApp(ReaderConfig.WEIXIN_PAY_APPID);
        initView();
        initData();
    }

    UMAuthListener authListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            MyToash.Log("SHARE_MEDIA1   " + platform.toString());
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            if (iwxapi == null) {
                iwxapi = WXAPIFactory.createWXAPI(activity, ReaderConfig.WEIXIN_PAY_APPID, true);
            }
            if (!iwxapi.isWXAppInstalled()) {
                //  ToastUtils.toast("您手机尚未安装微信，请安装后再登录");
                return;
            }
            iwxapi.registerApp(ReaderConfig.WEIXIN_PAY_APPID);
            SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "wechat_sdk_xb_live_state";//官方说明：用于保持请求和回调的状态，授权请求后原样带回给第三方。该参数可用于防止csrf攻击（跨站请求伪造攻击），建议第三方带上该参数，可设置为简单的随机数加session进行校验

            iwxapi.sendReq(req);
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            //  Toast.makeText(LoginActivity.this, "失败：" + t.getMessage(), Toast.LENGTH_LONG).show();
            MyToash.Log("SHARE_MEDIA 2   " + t.getMessage());
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            //   Toast.makeText(LoginActivity.this, "取消了", Toast.LENGTH_LONG).show();
        }
    };


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshUserInfo refreshUserInfo) {
        mUserInfo = refreshUserInfo.UserInfo;
        new UpdateData().invoke();
    }


    private class UpdateData {
        public void invoke() {
            if (mUserInfo.getAvatar() != null) {
                ImageLoader.getInstance().displayImage(mUserInfo.getAvatar(), user_info_avatar, ReaderApplication.getOptions());
            }
            //昵称
            user_info_nickname.setText(mUserInfo.getNickname());
            if (mUserInfo.getGender() == 0) {
                user_info_sex.setText(LanguageUtil.getString(activity, R.string.UserInfoActivity_weizhi));
            } else if (mUserInfo.getGender() == 2) {
                user_info_sex.setText(LanguageUtil.getString(activity, R.string.UserInfoActivity_boy));
            } else {
                user_info_sex.setText(LanguageUtil.getString(activity, R.string.UserInfoActivity_gril));
            }
            user_info_uid.setText(mUserInfo.getUid() + "");
            user_info_phone.setText(mUserInfo.getBind_list().size() > 0 ? mUserInfo.getBind_list().get(0).getDisplay() : "");
            user_info_weixin.setText(mUserInfo.getBind_list().size() > 1 ? mUserInfo.getBind_list().get(1).getDisplay() : "");
        }
    }

    Uri mCutUri;

    // 图片裁剪
    private void cropPhoto(Uri uri, boolean fromCapture) {
        Intent intent = new Intent("com.android.camera.action.CROP"); //打开系统自带的裁剪图片的intent


        // 注意一定要添加该项权限，否则会提示无法裁剪
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        intent.setDataAndType(uri, "image/*");
        intent.putExtra("scale", true);

        // 设置裁剪区域的宽高比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // 设置裁剪区域的宽度和高度
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);

        // 取消人脸识别
        intent.putExtra("noFaceDetection", true);
        // 图片输出格式
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        // 若为false则表示不返回数据
        intent.putExtra("return-data", false);

        // 指定裁剪完成以后的图片所保存的位置,pic info显示有延时
        if (fromCapture) {
            // 如果是使用拍照，那么原先的uri和最终目标的uri一致,注意这里的uri必须是Uri.fromFile生成的
            mCutUri = Uri.fromFile(cameraSavePath);
        } else { // 从相册中选择，那么裁剪的图片保存在take_photo中
            String time = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA).format(new Date());
            String fileName = "photo_" + time;
            File mCutFile = new File(Environment.getExternalStorageDirectory() + "/take_photo", fileName + ".jpeg");
            if (!mCutFile.getParentFile().exists()) {
                mCutFile.getParentFile().mkdirs();
            }
            mCutUri = Uri.fromFile(mCutFile);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCutUri);
        // 以广播方式刷新系统相册，以便能够在相册中找到刚刚所拍摄和裁剪的照片
        Intent intentBc = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intentBc.setData(uri);
        this.sendBroadcast(intentBc);

        startActivityForResult(intent, REQUEST_CROP); //设置裁剪参数显示图片至ImageVie
    }
}
