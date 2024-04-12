package com.example.DR.androiddiabeticretinopathyapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Random;

import Util.ConvolutionMatrix;
import Util.GifImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView chosenImageView, bmpImageVIew, hueImageVIew, grayScaleImageVIew, edgeImageView, engraveimageView, alphaimageView, hue2ImageVIew, testimageview;
    View bar;
    Bitmap bmpgreen;
    Bitmap huebitmmap = null;
    Bitmap grayScalebitmap = null;
    Bitmap edgebitmap = null;
    Bitmap engravebitmap = null;
    Bitmap huebitmmap2 = null;
    Bitmap bmpDummy;

    Bitmap testbitmap1, testbitmap2;

    ProgressDialog progressBar;
    private int progressBarStatus = 0;
    private Handler progressBarHandler = new Handler();
    Intent intent;
    private long fileSize = 0;
    Animation animation;
    private final int[] mColors =
            {Color.BLUE, Color.GREEN, Color.RED, Color.LTGRAY, Color.MAGENTA, Color.CYAN,
                    Color.YELLOW, Color.WHITE};
    int brightnessValue = 0;
    BitmapFactory.Options bmpFactoryOptions;
    int dw;
    int dh;

    final static int KERNAL_WIDTH = 3;
    final static int KERNAL_HEIGHT = 3;

    int[][] kernal = {
            {0, -1, 0},
            {-1, 4, -1},
            {0, -1, 0}
    };

    SeekBar.OnSeekBarChangeListener brightnessBarChangeListener;

    LinearLayout rlbar, rlmain;
    LinearLayout barlinearlayout;
    private GifImageView gifImageView, radascanview, giftestmageView;


    public static String strpath = android.os.Environment.getExternalStorageDirectory().toString();
    public static String dirName = "DIR_NAME";
    File makeDirectory;


    Button scanbtn, buttonshare;
    boolean checkPink = false;

    private static final int START_X = 10;
    private static final int START_Y = 15;
    private static final int WIDTH_PX = 100;
    private static final int HEIGHT_PX = 100;
    String strMessage = null;

    int vibrantpopulation = 0;
    int vibrantLightpopulation = 0;
    int vibrantDarkpopulation = 0;
    int mutedpopulation = 0;
    int mutedLighpopulationt = 0;
    int mutedDarkpopulation = 0;


    int aftervibrantpopulation = 0;
    int aftervibrantLightpopulation = 0;
    int aftervibrantDarkpopulation = 0;
    int aftermutedpopulation = 0;
    int aftermutedLighpopulationt = 0;
    int aftermutedDarkpopulation = 0;

    String mPercentage = null;

    MediaPlayer mPlayer;
    ImageView iv;
    ImageButton closeiv;
    Animation myAnim;

    String mNameStr;

    LinearLayout containerlayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chosenImageView = (ImageView) this.findViewById(R.id.AlteredImageView);
        bmpImageVIew = (ImageView) findViewById(R.id.bmpimageView);
        hueImageVIew = (ImageView) findViewById(R.id.HueimageView);
        grayScaleImageVIew = (ImageView) findViewById(R.id.grayscaleimageView);
        edgeImageView = (ImageView) findViewById(R.id.edgeimageView);
        engraveimageView = (ImageView) findViewById(R.id.engraveimageView);

        hue2ImageVIew = (ImageView) findViewById(R.id.Hue2imageView);
        testimageview = (ImageView) findViewById(R.id.testmageView);
        // fab = (FloatingActionButton) findViewById(R.id.fab);
        rlbar = (LinearLayout) findViewById(R.id.progress_lllayout);
        barlinearlayout = (LinearLayout) findViewById(R.id.barlinearlayout);
        containerlayout = (LinearLayout)findViewById(R.id.containerlayout) ;
       // barlinearlayout1 = (LinearLayout) findViewById(R.id.barlinearlayout1);
        rlmain = (LinearLayout) findViewById(R.id.mainrelativelayout);
         gifImageView = (GifImageView) findViewById(R.id.GifImageView);
         gifImageView.setGifImageResource(R.drawable.radar2);
        radascanview = (GifImageView) findViewById(R.id.radascanview);
        radascanview.setGifImageResource(R.drawable.scan2);
        giftestmageView = (GifImageView) findViewById(R.id.giftestmageView);
        giftestmageView.setGifImageResource(R.drawable.retina4);
        //listview = (ListView) findViewById(R.id.mainlistview);
        scanbtn = (Button) findViewById(R.id.button1);
        buttonshare = (Button) findViewById(R.id.buttonshare);
        scanbtn.setOnClickListener(this);
        bmpImageVIew.setOnClickListener(this);
        hueImageVIew.setOnClickListener(this);
        grayScaleImageVIew.setOnClickListener(this);
        edgeImageView.setOnClickListener(this);
        engraveimageView.setOnClickListener(this);
        hue2ImageVIew.setOnClickListener(this);
        testimageview.setOnClickListener(this);
        buttonshare.setOnClickListener(this);

        containerlayout.setDrawingCacheEnabled(true);
        containerlayout.getDrawingCache();
        makeDirectory = new File(strpath + "/" + dirName);
        makeDirectory.mkdir();

        myAnim = AnimationUtils.loadAnimation(this, R.anim.anim);
        scanbtn.startAnimation(myAnim);
        Display currentDisplay = getWindowManager().getDefaultDisplay();
        dw = currentDisplay.getWidth();
        dh = currentDisplay.getHeight() / 2 - 100;


        try {

            bmpFactoryOptions = new BitmapFactory.Options();
            bmpFactoryOptions.inJustDecodeBounds = true;
            intent = getIntent();
            bmpgreen = (Bitmap) intent.getParcelableExtra("BitmapImage");
            bmpDummy = (Bitmap) intent.getParcelableExtra("BitmapImage");
            mNameStr =  intent.getStringExtra("name");
            bmpDummy = getResizedBitmap(bmpDummy, 500);
            chosenImageView.setImageBitmap(bmpDummy);

        } catch (Exception e) {
            Log.v("ERROR", e.toString());
        }
    }


    public static Bitmap engrave(Bitmap src) {
        ConvolutionMatrix convMatrix = new ConvolutionMatrix(5);
        convMatrix.setAll(0);
        convMatrix.Matrix[0][0] = -2;
        convMatrix.Matrix[1][1] = 2;
        convMatrix.Factor = 1;
        convMatrix.Offset = 95;
        return ConvolutionMatrix.computeConvolution3x3(src, convMatrix);
    }


    public static Bitmap createContrast(Bitmap src, double value) {
        // image size
        int width = src.getWidth();
        int height = src.getHeight();
        // create output bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
        // color information
        int A, R, G, B;
        int pixel;
        // get contrast value
        double contrast = Math.pow((100 + value) / 100, 2);

        // scan through all pixels
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                // get pixel color
                pixel = src.getPixel(x, y);
                A = Color.alpha(pixel);
                // apply filter contrast for every channel R, G, B
                R = Color.red(pixel);
                R = (int) (((((R / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if (R < 0) {
                    R = 0;
                } else if (R > 255) {
                    R = 255;
                }

                G = Color.red(pixel);
                G = (int) (((((G / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if (G < 0) {
                    G = 0;
                } else if (G > 255) {
                    G = 255;
                }

                B = Color.red(pixel);
                B = (int) (((((B / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if (B < 0) {
                    B = 0;
                } else if (B > 255) {
                    B = 255;
                }

                // set new pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }

        // return final image
        return bmOut;
    }


    private Bitmap cropBitmap1(Bitmap bmp2) {
        Bitmap bmOverlay = Bitmap.createBitmap(250, 250, Bitmap.Config.ARGB_8888);

        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp2, 0, 0, null);
        canvas.drawRect(0, 0, 100, 100, paint);

        return bmOverlay;
    }

    private Bitmap processingBitmap(Bitmap src, int[][] knl) {
        Bitmap dest = Bitmap.createBitmap(
                src.getWidth(), src.getHeight(), src.getConfig());

        int bmWidth = src.getWidth();
        int bmHeight = src.getHeight();
        int bmWidth_MINUS_2 = bmWidth - 2;
        int bmHeight_MINUS_2 = bmHeight - 2;

        for (int i = 1; i <= bmWidth_MINUS_2; i++) {
            for (int j = 1; j <= bmHeight_MINUS_2; j++) {

                //get the surround 3*3 pixel of current src[i][j] into a matrix subSrc[][]
                int[][] subSrc = new int[KERNAL_WIDTH][KERNAL_HEIGHT];
                for (int k = 0; k < KERNAL_WIDTH; k++) {
                    for (int l = 0; l < KERNAL_HEIGHT; l++) {
                        subSrc[k][l] = src.getPixel(i - 1 + k, j - 1 + l);
                    }
                }

                //subSum = subSrc[][] * knl[][]
                int subSumA = 0;
                int subSumR = 0;
                int subSumG = 0;
                int subSumB = 0;

                for (int k = 0; k < KERNAL_WIDTH; k++) {
                    for (int l = 0; l < KERNAL_HEIGHT; l++) {
                        subSumR += Color.red(subSrc[k][l]) * knl[k][l];
                        subSumG += Color.green(subSrc[k][l]) * knl[k][l];
                        subSumB += Color.blue(subSrc[k][l]) * knl[k][l];
                    }
                }

                subSumA = Color.alpha(src.getPixel(i, j));

                if (subSumR < 0) {
                    subSumR = 0;
                } else if (subSumR > 255) {
                    subSumR = 255;
                }

                if (subSumG < 0) {
                    subSumG = 0;
                } else if (subSumG > 255) {
                    subSumG = 255;
                }

                if (subSumB < 0) {
                    subSumB = 0;
                } else if (subSumB > 255) {
                    subSumB = 255;
                }

                dest.setPixel(i, j, Color.argb(
                        subSumA,
                        subSumR,
                        subSumG,
                        subSumB));
            }
        }

        return dest;
    }

    public static Bitmap applyHueFilter(Bitmap source, int level) {
        // get image size
        int width = source.getWidth();
        int height = source.getHeight();
        int[] pixels = new int[width * height];
        float[] HSV = new float[3];
        // get pixel array from source
        source.getPixels(pixels, 0, width, 0, 0, width, height);

        int index = 0;
        // iteration through pixels
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                // get current index in 2D-matrix
                index = y * width + x;
                // convert to HSV
                Color.colorToHSV(pixels[index], HSV);
                // increase Saturation level
                HSV[0] *= level;
                HSV[0] = (float) Math.max(0.0, Math.min(HSV[0], 360.0));
                // take color back
                pixels[index] |= Color.HSVToColor(HSV);
            }
        }
        // output bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bmOut.setPixels(pixels, 0, width, 0, 0, width, height);
        return bmOut;
    }

    private Bitmap processingBitmap_Brightness(Bitmap src) {
        Bitmap dest = Bitmap.createBitmap(
                src.getWidth(), src.getHeight(), src.getConfig());

        for (int x = 0; x < src.getWidth(); x++) {
            for (int y = 0; y < src.getHeight(); y++) {
                int pixelColor = src.getPixel(x, y);
                int pixelAlpha = Color.alpha(pixelColor);

                int pixelRed = Color.red(pixelColor) + brightnessValue;
                int pixelGreen = Color.green(pixelColor) + brightnessValue;
                int pixelBlue = Color.blue(pixelColor) + brightnessValue;

                if (pixelRed > 255) {
                    pixelRed = 255;
                } else if (pixelRed < 0) {
                    pixelRed = 0;
                }

                if (pixelGreen > 255) {
                    pixelGreen = 255;
                } else if (pixelGreen < 0) {
                    pixelGreen = 0;
                }

                if (pixelBlue > 255) {
                    pixelBlue = 255;
                } else if (pixelBlue < 0) {
                    pixelBlue = 0;
                }

                int newPixel = Color.argb(
                        pixelAlpha, pixelRed, pixelGreen, pixelBlue);

                dest.setPixel(x, y, newPixel);

            }
        }
        return dest;
    }


    private Bitmap getGrayscale_custom_matrix(Bitmap src) {
        int width = src.getWidth();
        int height = src.getHeight();

        //Custom color matrix
        float[] matrix = new float[]{
                0.3f, 0.59f, 0.11f, 0, 0,
                0.3f, 0.59f, 0.11f, 0, 0,
                0.3f, 0.59f, 0.11f, 0, 0,
                0, 0, 0, 1, 0,};

        Bitmap dest = Bitmap.createBitmap(width, height,
                Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(dest);
        Paint paint = new Paint();
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        paint.setColorFilter(filter);
        canvas.drawBitmap(src, 0, 0, paint);

        return dest;
    }


    public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {

        int targetWidth = 50;
        int targetHeight = 50;
        Bitmap targetBitmap = scaleBitmapImage;

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,
                ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth),
                        ((float) targetHeight)) / 2),
                Path.Direction.CCW);

        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap,
                new Rect(0, 0, sourceBitmap.getWidth(),
                        sourceBitmap.getHeight()),
                new Rect(0, 0, targetWidth,
                        targetHeight), null);
        return targetBitmap;
    }

    public int calculateBrightnessEstimate(Bitmap bitmap, int pixelSpacing) {
        int R = 0;
        int G = 0;
        int B = 0;
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        int n = 0;
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < pixels.length; i += pixelSpacing) {
            int color = pixels[i];
            R += Color.red(color);
            G += Color.green(color);
            B += Color.blue(color);
            n++;
        }
        return (R + B + G) / (n * 3);
    }

    public int calculateBrightness(Bitmap bitmap) {
        calculateBrightnessEstimate(bitmap, 1);
        int value = calculateBrightnessEstimate(bitmap, 1);
        return value;
    }


    public Bitmap replaceColor(Bitmap myBitmap, int color) {
        int[] allpixels = new int[myBitmap.getHeight() * myBitmap.getWidth()];
        myBitmap.getPixels(allpixels, 0, myBitmap.getWidth(), 0, 0, myBitmap.getWidth(), myBitmap.getHeight());
        for (int i = 0; i < allpixels.length; i++) {
            if (allpixels[i] == Color.WHITE) {
                allpixels[i] = color;
                ;
            }
        }
        myBitmap.setPixels(allpixels, 0, myBitmap.getWidth(), 0, 0, myBitmap.getWidth(), myBitmap.getHeight());
        return myBitmap;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }


    public static Bitmap doBrightness(Bitmap src, int value) {
        // image size
        int width = src.getWidth();
        int height = src.getHeight();
        // create output bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
        // color information
        int A, R, G, B;
        int pixel;

        // scan through all pixels
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                // get pixel color
                pixel = src.getPixel(x, y);
                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);

                // increase/decrease each channel
                R += value;
                if (R > 255) {
                    R = 255;
                } else if (R < 0) {
                    R = 0;
                }

                G += value;
                if (G > 255) {
                    G = 255;
                } else if (G < 0) {
                    G = 0;
                }

                B += value;
                if (B > 255) {
                    B = 255;
                } else if (B < 0) {
                    B = 0;
                }

                // apply new pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }

        // return final image
        return bmOut;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void extractProminentColors(Bitmap bitmap, final Bitmap bitmap1) {
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                //work with the palette here
                int defaultValue = 0x000000;
                int vibrant = palette.getVibrantColor(defaultValue);
                int vibrantLight = palette.getLightVibrantColor(defaultValue);
                int vibrantDark = palette.getDarkVibrantColor(defaultValue);
                int muted = palette.getMutedColor(defaultValue);
                int mutedLight = palette.getLightMutedColor(defaultValue);
                int mutedDark = palette.getDarkMutedColor(defaultValue);

                // Log.d("vibrantLightpop",  palette.getLightVibrantSwatch().getPopulation() + "");

                String strvibrant = ("#" + Integer.toHexString(vibrant)) + "";
                String strvibrantlight = ("#" + Integer.toHexString(vibrantLight)) + "";
                String strvibrantDark = ("#" + Integer.toHexString(vibrantDark)) + "";
                String strmuted = ("#" + Integer.toHexString(muted)) + "";
                String strmutedLight = ("#" + Integer.toHexString(mutedLight)) + "";
                String strmutedDark = ("#" + Integer.toHexString(mutedDark)) + "";
                if (strvibrant.contains("f")) {
                    vibrantpopulation = palette.getVibrantSwatch().getPopulation();
                } else if (strvibrantlight.contains("f")) {
                    vibrantLightpopulation = palette.getLightVibrantSwatch().getPopulation();
                } else if (strvibrantDark.contains("f")) {
                    vibrantDarkpopulation = palette.getDarkVibrantSwatch().getPopulation();
                } else if (strmuted.contains("f")) {
                    mutedpopulation = palette.getMutedSwatch().getPopulation();
                } else if (strmutedLight.contains("f")) {
                    mutedLighpopulationt = palette.getLightMutedSwatch().getPopulation();
                } else if (strmutedDark.contains("f")) {
                    mutedDarkpopulation = palette.getDarkMutedSwatch().getPopulation();
                }

                Log.d("vibrantpop", vibrantpopulation + "");
                Log.d("vibrantLightpop", vibrantLightpopulation + "");
                Log.d("vibrantDarkpop", vibrantDarkpopulation + "");
                Log.d("vibmutedpop", mutedpopulation + "");
                Log.d("vibrantpop", mutedLighpopulationt + "");
                Log.d("vibmutedDarkpop", mutedDarkpopulation + "");

                // Integer intColor = -16895234;
                //String hexColor = ("#" + Integer.toHexString(intColor).substring(2));

                String data[] = new String[]{"vibrant", "vibrantLight", "vibrantDark", "vibmuted", "vibmutedLight", "vibmutedDark"};
                int value[] = new int[]{vibrant, vibrantLight, vibrantDark, muted, mutedLight, mutedDark};

                ColourAdapter adapter = new ColourAdapter(getApplicationContext(), data, value);
                //listview.setAdapter(adapter);

                createPopUP(bitmap1);

            }

            private void createPopUP(Bitmap bitmap1) {

                Palette.from(bitmap1).generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        //work with the palette here
                        int defaultValue = 0x000000;
                        int vibrant = palette.getVibrantColor(defaultValue);
                        int vibrantLight = palette.getLightVibrantColor(defaultValue);
                        int vibrantDark = palette.getDarkVibrantColor(defaultValue);
                        int muted = palette.getMutedColor(defaultValue);
                        int mutedLight = palette.getLightMutedColor(defaultValue);
                        int mutedDark = palette.getDarkMutedColor(defaultValue);

                        // Log.d("vibrantLightpop",  palette.getLightVibrantSwatch().getPopulation() + "");

                        String strvibrant = ("#" + Integer.toHexString(vibrant)) + "";
                        String strvibrantlight = ("#" + Integer.toHexString(vibrantLight)) + "";
                        String strvibrantDark = ("#" + Integer.toHexString(vibrantDark)) + "";
                        String strmuted = ("#" + Integer.toHexString(muted)) + "";
                        String strmutedLight = ("#" + Integer.toHexString(mutedLight)) + "";
                        String strmutedDark = ("#" + Integer.toHexString(mutedDark)) + "";
                        if (strvibrant.contains("f")) {
                            aftervibrantpopulation = palette.getVibrantSwatch().getPopulation();
                        } else if (strvibrantlight.contains("f")) {
                            aftervibrantLightpopulation = palette.getLightVibrantSwatch().getPopulation();
                        } else if (strvibrantDark.contains("f")) {
                            aftervibrantDarkpopulation = palette.getDarkVibrantSwatch().getPopulation();
                        } else if (strmuted.contains("f")) {
                            aftermutedpopulation = palette.getMutedSwatch().getPopulation();
                        } else if (strmutedLight.contains("f")) {
                            aftermutedLighpopulationt = palette.getLightMutedSwatch().getPopulation();
                        } else if (strmutedDark.contains("f")) {
                            aftermutedDarkpopulation = palette.getDarkMutedSwatch().getPopulation();
                        }

                        Log.d("aftervibrantpop", aftervibrantpopulation + "");
                        Log.d("aftervibrantLightpop", aftervibrantLightpopulation + "");
                        Log.d("aftervibrantDarkpop", aftervibrantDarkpopulation + "");
                        Log.d("aftervibmutedpop", aftermutedpopulation + "");
                        Log.d("aftervibrantpop", aftermutedLighpopulationt + "");
                        Log.d("aftervibmutedDarkpop", aftermutedDarkpopulation + "");

                        Log.d("vibmainpop", vibrantpopulation + "");

                        int maximum = 10;
                        int minimum = 1;
                        Random rn = new Random();
                        int range = maximum - minimum + 1;
                        int randomNum = 0;

                        if (aftervibrantpopulation > 0 || aftervibrantLightpopulation > 0) {
                            if (aftervibrantpopulation > 190 || aftervibrantLightpopulation > 190) {
                                strMessage = "Severe";
                                mPercentage = aftervibrantpopulation+aftervibrantLightpopulation+"";

                            } else if (aftervibrantpopulation > 0 || aftervibrantLightpopulation > 0 && aftervibrantpopulation < 190 || aftervibrantLightpopulation < 190) {
                                strMessage = "moderate";
                                mPercentage = aftervibrantpopulation+aftervibrantLightpopulation+"";

                            } else {
                                randomNum =  rn.nextInt(range) + minimum;
                                mPercentage = randomNum+"";
                                strMessage = "mild";
                            }
                        } else {
                            mPercentage = aftervibrantpopulation+aftervibrantLightpopulation+"";
                            randomNum =  rn.nextInt(range) + minimum;
                            strMessage = "mild";
                            mPercentage = randomNum+"";

                        }

                        Log.d("percentage",mPercentage);
/*

                        if( aftermutedpopulation > 400){

                            if( aftermutedpopulation > 570){
                                strMessage = "Severe";
                            }
                            else if(aftermutedpopulation > 400 && aftermutedpopulation < 569){

                                strMessage = "medium";
                            }

                        }
                        else{
                            strMessage = "mild";
                        }
*/
                        showChangeLangDialog(strMessage);


                    }
                });


            }
        });


    }

    public void showChangeLangDialog(String strMessage) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("RESULT");
        dialogBuilder.setMessage("DIabetes level is " + strMessage);
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //do something with edt.getText().toString();

                myAnim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.anim);
                buttonshare.startAnimation(myAnim);
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    private Bitmap getCircleBitmap(Bitmap bitmap) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int radius = Math.min(h / 6, w / 6);
        Bitmap output = Bitmap.createBitmap(w + 8, h + 8, Bitmap.Config.ARGB_8888);

        Paint p = new Paint();
        p.setAntiAlias(true);

        Canvas c = new Canvas(output);
        c.drawARGB(0, 0, 0, 0);
        p.setStyle(Paint.Style.FILL);

        c.drawCircle((w / 2) + 4, (h / 2) + 4, radius, p);

        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        c.drawBitmap(bitmap, 4, 4, p);
        p.setXfermode(null);
        p.setStyle(Paint.Style.STROKE);
        // p.setColor(Color.WHITE);
        p.setStrokeWidth(3);
        c.drawCircle((w / 2) + 4, (h / 2) + 4, radius, p);

        return output;
    }

    public void onDestroy() {
       /* if(mPlayer .isPlaying()) {
            mPlayer.stop();
        }*/
        super.onDestroy();

    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.button1) {
            new AsyncTask<String, String, String>() {


                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    mPlayer = MediaPlayer.create(MainActivity.this, R.raw.databasescan1);
                    mPlayer.start();
                    rlbar.setVisibility(View.VISIBLE);
                    // rlmain.setVisibility(View.GONE);
                    rlbar.setBackgroundColor(getResources().getColor(R.color.animcolour));
                    barlinearlayout.setVisibility(View.GONE);
                   // barlinearlayout1.setVisibility(View.GONE);
                    scanbtn.setEnabled(false);
                    giftestmageView.setVisibility(View.VISIBLE);

                    radascanview.setVisibility(View.VISIBLE);
                    testimageview.setVisibility(View.GONE);
                    buttonshare.setVisibility(View.GONE);
                }

                @Override
                protected String doInBackground(String... params) {

                    bmpgreen = (Bitmap) intent.getParcelableExtra("BitmapImage");
                    bmpgreen = getResizedBitmap(bmpgreen, 500);
                    if (bmpgreen != null) {

                        int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight
                                / (float) dh);
                        int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth
                                / (float) dw);

                        if (heightRatio > 1 && widthRatio > 1) {
                            if (heightRatio > widthRatio) {
                                bmpFactoryOptions.inSampleSize = heightRatio;
                            } else {
                                bmpFactoryOptions.inSampleSize = widthRatio;
                            }
                        }
                        bmpFactoryOptions.inJustDecodeBounds = false;
                        bmpgreen = doBrightness(bmpgreen, 15);
                        Canvas canvas = new Canvas(bmpgreen);
                        Paint paint = new Paint();
                        ColorMatrix cm = new ColorMatrix();
                        // Increase Contrast, Slightly Reduce Brightness
                        float contrast = 3;
                        float brightness = -3;
                        cm.set(new float[]{contrast, 0, 0, 0, brightness, 0,
                                contrast, 0, 0, brightness, 0, 0, contrast, 0,
                                brightness, 0, 0, 0, 1, 0});

                        paint.setColorFilter(new ColorMatrixColorFilter(cm));
                        Matrix matrix = new Matrix();
                        canvas.drawBitmap(bmpgreen, matrix, paint);
                        int value = calculateBrightness(bmpgreen);
                        Log.d("brifgtnessvalue", value + "");
                        // bmp = createTransparentBitmapFromBitmap(bmp,Color.BLUE);
                        matrix.postScale(0.5f, 0.5f);
                        //bmp = getRoundedShape(bmp);
                        //bmp = scaleCenterCrop(bmp,bmp.getHeight()/2,bmp.getWidth()/2);



                        //bmpgreen = cropBitmap1(bmpgreen);
                        // bmpgreen = processingBitmap_Brightness(bmpgreen);
                       // bmpgreen = getResizedBitmap(bmpgreen, 500);
                        //bmpgreen = createContrast(bmpgreen, 100);
                        bmpgreen = replaceColor(bmpgreen, getResources().getColor(R.color.colorAccent));


                        huebitmmap = (Bitmap) intent.getParcelableExtra("BitmapImage");
                        huebitmmap = getResizedBitmap(huebitmmap, 500);
                        huebitmmap = applyHueFilter(huebitmmap, 9);


                        grayScalebitmap = (Bitmap) intent.getParcelableExtra("BitmapImage");
                        grayScalebitmap = getResizedBitmap(grayScalebitmap, 500);
                        grayScalebitmap = getGrayscale_custom_matrix(grayScalebitmap);
                        grayScalebitmap = replaceColor(grayScalebitmap, getResources().getColor(R.color.GREENPALLTE));
                        grayScalebitmap = doBrightness(grayScalebitmap, 15);


                        edgebitmap = (Bitmap) intent.getParcelableExtra("BitmapImage");
                        edgebitmap = getResizedBitmap(edgebitmap, 500);
                        edgebitmap = createContrast(edgebitmap, 100);
                        edgebitmap = replaceColor(edgebitmap, getResources().getColor(R.color.GREENPALLTE));
                        //checkPinkColour(edgebitmap);
                        //edgebitmap = engrave(edgebitmap);
                        //extractProminentColors(edgebitmap);


                        engravebitmap = (Bitmap) intent.getParcelableExtra("BitmapImage");
                        engravebitmap = getResizedBitmap(engravebitmap, 500);
                        engravebitmap = engrave(engravebitmap);


                        huebitmmap2 = (Bitmap) intent.getParcelableExtra("BitmapImage");
                        huebitmmap2 = getResizedBitmap(huebitmmap2, 500);
                        huebitmmap2 = applyHueFilter(huebitmmap2, 3);

                        //replaceColor(huebitmmap2,getResources().getColor(R.color.colorAccent));
                        //edgebitmap = engrave(edgebitmap);
                        // replaceColor(edgebitmap,getResources().getColor(R.color.colorAccent));




                        /*    Bitmap alphabitmap  = null;
                            alphabitmap = (Bitmap) intent.getParcelableExtra("BitmapImage");
                            alphabitmap = getResizedBitmap(alphabitmap, 500);
                            alphabitmap = engrave(engravebitmap);
                            //edgebitmap = engrave(edgebitmap);
                            //replaceColor(edgebitmap,getResources().getColor(R.color.colorAccent));
                            alphaimageView.setImageBitmap(alphabitmap);*/
                        //extractProminentColors(bmp);


                    }
                    return null;
                }


                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    mPlayer.stop();
                    mPlayer = MediaPlayer.create(MainActivity.this, R.raw.databasescan2);
                    mPlayer.start();
                    // barlinearlayout.setVisibility(View.VISIBLE);
                    // barlinearlayout1.setVisibility(View.VISIBLE);
                    scanbtn.setEnabled(true);
                    giftestmageView.setVisibility(View.GONE);
                    // radascanview.setVisibility(View.GONE);
                    //rlbar.setVisibility(View.GONE);
                    //  rlmain.setVisibility(View.VISIBLE);
                    testimageview.setVisibility(View.VISIBLE);
                    barlinearlayout.setVisibility(View.VISIBLE);
                   // barlinearlayout1.setVisibility(View.VISIBLE);
                    rlbar.setVisibility(View.GONE);

                    radascanview.setVisibility(View.GONE);
                    bmpImageVIew.setImageBitmap(bmpgreen);
                    hueImageVIew.setImageBitmap(huebitmmap);
                    grayScaleImageVIew.setImageBitmap(grayScalebitmap);
                    edgeImageView.setImageBitmap(edgebitmap);
                    engraveimageView.setImageBitmap(engravebitmap);
                    hue2ImageVIew.setImageBitmap(huebitmmap2);
                    testbitmap1 = edgebitmap;
                    edgebitmap = getCircleBitmap(edgebitmap);
                    testbitmap2 = edgebitmap;
                    testimageview.setImageBitmap(edgebitmap);

                    extractProminentColors(testbitmap1, testbitmap2);
                    buttonshare.setVisibility(View.VISIBLE);

                }
            }.execute();

        } else if (v.getId() == R.id.bmpimageView) {

            showDialog(bmpgreen);

        } else if (v.getId() == R.id.HueimageView) {

            showDialog(huebitmmap);

        } else if (v.getId() == R.id.grayscaleimageView) {

            showDialog(grayScalebitmap);

        } else if (v.getId() == R.id.edgeimageView) {

            showDialog(testbitmap1);

        } else if (v.getId() == R.id.engraveimageView) {

            showDialog(engravebitmap);

        } else if (v.getId() == R.id.Hue2imageView) {

            showDialog(huebitmmap2);

        } else if (v.getId() == R.id.testmageView) {

            showDialog(testbitmap2);

        } else if (v.getId() == R.id.buttonshare) {

            Bitmap bitmap = Bitmap.createBitmap(containerlayout.getWidth(), containerlayout.getHeight(),
                    Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            containerlayout.draw(canvas);
            bitmap = getResizedBitmap(bitmap, 500);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bytes = stream.toByteArray();
            Intent intent = new Intent(this, SecondActivity.class);
            intent.putExtra("name",mNameStr);
            intent.putExtra("perecntage",mPercentage);
            intent.putExtra("severity",strMessage);
            intent.putExtra("BMP", bytes);
            startActivity(intent);

        }


    }

    private void showDialog(Bitmap bitmap) {

        final Dialog dialog = new Dialog(MainActivity.this, android.R.style.Theme_Translucent);
        dialog.setContentView(R.layout.customdialog);
        iv = (ImageView) dialog.findViewById(R.id.imageViewbig);
        iv.setImageBitmap(bitmap);
        closeiv = (ImageButton) dialog.findViewById(R.id.close_button);
        closeiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


}
