package com.dazhongmianfei.dzmfreader.comic.been;

import android.app.Activity;
import android.graphics.Color;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;


import com.umeng.commonsdk.debug.W;
import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.Task.InstructTask;
import com.dazhongmianfei.dzmfreader.Task.TaskManager;
import com.dazhongmianfei.dzmfreader.comic.view.LargeImageView;
import com.dazhongmianfei.dzmfreader.utils.FileManager;
import com.dazhongmianfei.dzmfreader.utils.MyToash;
import com.dazhongmianfei.dzmfreader.utils.ScreenSizeUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.concurrent.ExecutionException;

import static com.dazhongmianfei.dzmfreader.utils.FileManager.GlideCopy;
import static com.dazhongmianfei.dzmfreader.utils.FileManager.getManhuaSDCardRoot;


/**
 * Created by scb on 2018/12/14.
 */

public class MyGlide {
    static int WIDTH;
    static int HEIGHT;
    //  static Activity activity;

    private MyGlide() {
    }

    private static MyGlide myGlide;

    public static MyGlide getMyGlide(Activity activity, int w, int h) {
        //     activity = Activity;
        WIDTH = w;
        HEIGHT = h;
        if (myGlide == null) {
            synchronized (MyGlide.class) {
                if (myGlide == null)
                    myGlide = new MyGlide();
            }
        }
        return myGlide;
    }


    public void GlideImage(Activity activity, String url, ImageView imageView, int width, int height) {
        try {
            if (url == null || url.length() == 0) {
                return;
            } else {
                //  MyToash.Log("ComicChapterItem  3",url);
                RequestOptions options = new RequestOptions()
                        .placeholder(R.mipmap.icon_comic_def)        //加载成功之前占位图
                        .error(R.mipmap.icon_comic_def)        //加载错误之后的错误图
                        //指定图片的尺寸
                        .override(WIDTH, WIDTH * height / width)
                        .centerCrop()
                        .skipMemoryCache(true)        //
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE);    //缓存所有版本的图像
                try {
                    Glide.with(activity).load(url).apply(options).into(imageView);
                } catch (Exception e) {
                } catch (Error e) {
                }
            }
        } catch (Exception e) {
        } catch (Error e) {
        }
    }

    private static TaskManager mTaskManager;

    public static void GlideImageSubsamplingScaleImageView(Activity activity, BaseComicImage baseComicImage, LargeImageView imageView) {
        try {
            MyToash.Log("baseComicIBB", "1");
            File localPathFile;
            String ImgName = "";
            String localPath = getManhuaSDCardRoot().concat(baseComicImage.comic_id + "/").concat(baseComicImage.chapter_id + "/");
            if (baseComicImage.image == null) {
                return;
            }
            MyToash.Log("baseComicIBB", "2");
            if (baseComicImage.image.contains(".jpg")) {
                ImgName = baseComicImage.image_id + ".jpg";
            } else if (baseComicImage.image.contains(".jpeg")) {
                ImgName = baseComicImage.image_id + ".jpeg";
            } else if (baseComicImage.image.contains(".png")) {
                ImgName = baseComicImage.image_id + ".png";
            } else {
                return;
            }
            MyToash.Log("baseComicIBB", "3");
            localPathFile = new File(localPath.concat(ImgName));
            if (localPathFile.exists()) {
                MyToash.Log("baseComicIBB", localPathFile.getAbsolutePath());
                // imageView.setImage(ImageSource.uri(localPathFile.getAbsolutePath()));
                try {
                    imageView.setInputStream(new FileInputStream(localPathFile));
                } catch (Exception e) {
                } catch (Error e) {
                }
            } else {

                localPathFile.mkdirs();
                MyToash.Log("baseComicIBB", localPathFile.getAbsolutePath());
                if (mTaskManager == null) {
                    mTaskManager = new TaskManager();
                }
                mTaskManager.addQueueTask(new InstructTask<String, String>(null) {

                    @Override
                    public String doRun(String s) {
                        try {
                            File filee = Glide.with(activity)
                                    .load(baseComicImage.image)
                                    .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                    .get();
                            GlideCopy(filee, localPathFile);
                            MyToash.Log("baseComicIBBA", filee.getAbsolutePath() + "  " + localPathFile.getAbsolutePath());
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // imageView.setImage(ImageSource.uri(localPathFile.getAbsolutePath()));
                                    try {
                                        imageView.setInputStream(new FileInputStream(localPathFile));
                                    } catch (Exception e) {
                                    } catch (Error e) {
                                    }
                                }
                            });
                        } catch (Exception e) {
                        } catch (Error e) {
                        }


                        return null;
                    }
                });
            }
        } catch (Exception e) {
        } catch (Error e) {
        }
    }

    public static void GlideImage(Activity activity, BaseComicImage baseComicImage, ImageView imageView, int width, int height) {
        try {
            MyToash.Log("baseComicImagea", baseComicImage.toString());
            File localPathFile = FileManager.getManhuaSDCardRootImg(baseComicImage);

            if (localPathFile != null) {
                MyToash.Log("baseComicImage--", localPathFile.getAbsolutePath());
                RequestOptions options = new RequestOptions()
                        .placeholder(R.mipmap.icon_comic_def)        //加载成功之前占位图
                        .error(R.mipmap.icon_comic_def)        //加载错误之后的错误图
                        //指定图片的尺寸 WIDTH * height / width
                        .override(WIDTH, height)
                        .centerCrop()
                        .skipMemoryCache(false)
                        .diskCacheStrategy(DiskCacheStrategy.NONE);    //bu缓存
                try {
                    Glide.with(activity).load(localPathFile).apply(options).into(imageView);
                } catch (Exception e) {
                }

            } else {
                String url = baseComicImage.image;
                if (url == null || url.length() == 0) {
                    return;
                } else {
                    //  MyToash.Log("ComicChapterItem  3",url);
                    RequestOptions options = new RequestOptions()
                            .placeholder(R.mipmap.icon_comic_def)        //加载成功之前占位图
                            .error(R.mipmap.icon_comic_def)        //加载错误之后的错误图
                            //指定图片的尺寸
                            .override(WIDTH, height)
                            .centerCrop()
                            .skipMemoryCache(false)        //
                            .diskCacheStrategy(DiskCacheStrategy.NONE);    //缓存所有版本的图像
                    try {
                        Glide.with(activity).load(url).apply(options).into(imageView);
                    } catch (Exception e) {
                    } catch (Error e) {
                    }
                }
            }
        } catch (Exception e) {
        } catch (Error e) {
        }
    }


    public static void GlideImagePalette(Activity activity, String url, ImageView imageView, int width, int height) {
        if (url == null || url.length() == 0) {
            return;
        } else {
            //  MyToash.Log("ComicChapterItem  3",url);
            RequestOptions options = new RequestOptions()
                    .placeholder(R.mipmap.icon_comic_def)        //加载成功之前占位图
                    .error(R.mipmap.icon_comic_def)        //加载错误之后的错误图
                    //指定图片的尺寸
                    .override(WIDTH, WIDTH * height / width)
                    .centerCrop()
                    .skipMemoryCache(true)        //
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE);    //缓存所有版本的图像
            try {
                Glide.with(activity).load(url)
                        /*      .listener(GlidePalette.with(url).use(GlidePalette.Profile.VIBRANT_DARK).intoCallBack(new BitmapPalette.CallBack() {
                                  @Override
                                  public void onPaletteLoaded(@Nullable Palette palette) {
                                      Palette.Swatch swatch = palette.getDominantSwatch();
                                      if (swatch != null) {
                                          int rgb = swatch.getRgb();
                                          int[] colors = {0xB3FFFFFF, rgb};
                                          GradientDrawable g = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, colors);

                                          v.setBackground(g);
                                      }
                                  }
                              }))*/
                        .apply(options)
                        .into(imageView);
            } catch (Exception e) {
            }
        }
    }


    public static String toHexEncoding(int color) {
        String R, G, B;
        StringBuffer sb = new StringBuffer();
        R = Integer.toHexString(Color.red(color));
        G = Integer.toHexString(Color.green(color));
        B = Integer.toHexString(Color.blue(color));
        //判断获取到的R,G,B值的长度 如果长度等于1 给R,G,B值的前边添0
        R = R.length() == 1 ? "0" + R : R;
        G = G.length() == 1 ? "0" + G : G;
        B = B.length() == 1 ? "0" + B : B;
        sb.append("0x");
        sb.append(R);
        sb.append(G);
        sb.append(B);
        return sb.toString();
    }
}





