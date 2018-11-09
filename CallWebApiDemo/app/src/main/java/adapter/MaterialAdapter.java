package adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nguyenbakhiem.callwebapidemo.DownloadImageTask;
import com.example.nguyenbakhiem.callwebapidemo.MainActivity;
import com.example.nguyenbakhiem.callwebapidemo.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import entity.Material;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class MaterialAdapter extends BaseAdapter {
    private MainActivity mainActivity;
    private List<Material> list;

    public MaterialAdapter(MainActivity mainActivity, List<Material> list) {
        this.mainActivity = mainActivity;
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
    public void update(List<Material> list2){
        list = list2;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        MyItem myItem = null;
        if (view == null) {
            view = mainActivity.getLayoutInflater().inflate(R.layout.material_layout, null);
            myItem = new MyItem();
            myItem.txtName = view.findViewById(R.id.textViewName);
            myItem.imgView = view.findViewById(R.id.imageViewPhoto);

            view.setTag(myItem);

        } else {
            myItem = (MyItem) view.getTag();
        }
        final Material p = list.get(position);
        final int pos = position;
        String name = p.getName();
        myItem.txtName.setText(name);

        //Picasso.with(context).load("http://cdn.journaldev.com/wp-content/uploads/2016/11/android-image-picker-project-structure.png").into(imageView)
        //myItem.imgView.setImageResource(p.getImg());
//        new DownloadImageTask(myItem.imgView)
//                .execute(p.getImage_link());
        Picasso.with(mainActivity).load(p.getImage_link()).transform(new CropCircleTransformation()).into(myItem.imgView);
        return view;
    }

    class MyItem{
        public TextView txtName;
        public ImageView imgView;
    }
}
