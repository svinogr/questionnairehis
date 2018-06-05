package info.upump.questionnairehis;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

public class ImageActivity extends AppCompatActivity {
    private static final String IMG = "img";
    private String namePicture;
    private ImageView img;

    public static Intent createIntent(Context context, String namePicture) {
        Intent intent = new Intent(context, ImageActivity.class);
        intent.putExtra(IMG, namePicture);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        img = findViewById(R.id.image_activity_img);
        namePicture = getIntent().getStringExtra(IMG);
        setImage();
    }

    private void setImage() {
        System.out.println(1);
        RequestOptions options = new RequestOptions()
                .transforms(new RoundedCorners(50))
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);
        if (namePicture != null){
            img.setVisibility(View.VISIBLE);
            int ident = getResources().getIdentifier(namePicture, "drawable", getPackageName());
            Glide.with(this).load(ident).apply(options).into(img);
        } else img.setVisibility(View.GONE);

    }

    private void exit() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            exit();
        }
        return super.onOptionsItemSelected(item);
    }
}
