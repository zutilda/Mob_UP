package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
public class AdapterElement extends BaseAdapter {

    private final Context mContext;
    List<MaskElement> maskList;

    public AdapterElement(Context mContext, List<MaskElement> maskList) {
        this.mContext = mContext;
        this.maskList = maskList;
    }

    @Override
    public int getCount() {
        return maskList.size();
    }

    @Override
    public Object getItem(int i) {
        return maskList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return maskList.get(i).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = View.inflate(mContext, R.layout.list_element, null);

        TextView title = v.findViewById(R.id.Title);
        ImageView Image = v.findViewById(R.id.Images);
        TextView description = v.findViewById(R.id.description);

        MaskElement maskQuote = maskList.get(position);
        title.setText(maskQuote.getTitle());

        if (maskQuote.getImage().equals("null")) {
            Image.setImageResource(R.drawable.absence);
        } else {
            new DownloadImageTask(Image)
                    .execute(maskQuote.getImage());
        }

        description.setText(maskQuote.getDescription());
        return v;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
