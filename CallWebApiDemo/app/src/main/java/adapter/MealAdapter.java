package adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nguyenbakhiem.callwebapidemo.FavoriteActivity;
import com.example.nguyenbakhiem.callwebapidemo.R;
import com.example.nguyenbakhiem.callwebapidemo.DownloadImageTask;
import com.example.nguyenbakhiem.callwebapidemo.MealActivity;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.List;

import entity.Material;
import entity.Meal;

public class MealAdapter extends BaseAdapter {
    private Context mealActivity;
    private List<Meal> list;
    private Meal meal;

    public static MealAdapter mealinstance;
    public static MealAdapter favinstance;
    public static MealAdapter getInstanceMeal(Context mealActivity, List<Meal> list){
        if(mealinstance == null){
            mealinstance = new MealAdapter(mealActivity,list);
            
        }
        return mealinstance;
    }
    public static MealAdapter getInstanceFavorite(Context favActivity, List<Meal> list){
        if(favinstance == null){
            favinstance = new MealAdapter(favActivity,list);

        }
        return favinstance;
    }
    public MealAdapter(Context mealActivity, List<Meal> list)
    {
        this.mealActivity = mealActivity;
        this.list = list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public void update(List<Meal> list2){
        list = list2;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        Myholder myholder = new Myholder();
        if(view == null)
        {
            LayoutInflater inflater = (LayoutInflater) mealActivity.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
             view = inflater.inflate( R.layout.meal_layout, null );

            //view = mealActivity.getLayoutInflater().inflate(R.layout.meal_layout, null);
            myholder = new Myholder();
            myholder.txtName = view.findViewById(R.id.textViewMeal);
            myholder.imageView = view.findViewById(R.id.imageViewMeal);
            view.setTag(myholder);
        }
        else
        {
            myholder = (Myholder) view.getTag();
        }
        Meal meal = list.get(position);
        myholder.txtName.setText(meal.getName());
//        new DownloadImageTask(myholder.imageView)
//                .execute(meal.getImgLink());
        Picasso.with(mealActivity).load(meal.getImage_link()).into(myholder.imageView);
        return view;
    }



    public class Myholder
    {
        public TextView txtName;
        public ImageView imageView;
    }
}
