package paperbaglabs.school_android.service;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by paarthmadan on 2017-09-04.
 */

public class AverageColour extends Thread{

    private ImageView image;
    private LinearLayout sideBar;
    public AverageColour(ImageView image, LinearLayout sideBar){
        this.image = image;
        this.sideBar = sideBar;
    }

    public void run(){
        averageColour();
    }

    public void averageColour(){
        try{
            BitmapDrawable drawable = (BitmapDrawable)(image.getDrawable());
            Bitmap bitmap = drawable.getBitmap();
            int redColors = 0;
            int greenColors = 0;
            int blueColors = 0;
            int pixelCount = 0;

            for (int y = 0; y < bitmap.getHeight(); y++)
            {
                for (int x = 0; x < bitmap.getWidth(); x++)
                {
                    int c = bitmap.getPixel(x, y);
                    pixelCount++;
                    redColors += Color.red(c);
                    greenColors += Color.green(c);
                    blueColors += Color.blue(c);
                }
            }
            // calculate average of bitmap r,g,b values
            int red = (redColors/pixelCount);
            int green = (greenColors/pixelCount);
            int blue = (blueColors/pixelCount);
            Log.d("PAARTH COLOUR TEST", "Red: " + String.valueOf(red) + " Green: " + String.valueOf(green) + " Blue: " + String.valueOf(blue));
            sideBar.setBackgroundColor(Color.rgb(red, green, blue));
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
