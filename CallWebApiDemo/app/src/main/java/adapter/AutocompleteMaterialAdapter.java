package adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nguyenbakhiem.callwebapidemo.DownloadImageTask;
import com.example.nguyenbakhiem.callwebapidemo.R;

import java.util.ArrayList;
import java.util.List;

import entity.Material;

public class AutocompleteMaterialAdapter extends ArrayAdapter<Material> {
    private List<Material> materials = new ArrayList<>();
    private List<Material> filteredMaterials = new ArrayList<>();
    Context context;
    public AutocompleteMaterialAdapter(@NonNull Context context, List<Material> materials) {

        super(context,0,materials);
        this.materials = materials;
        this.context = context;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Nullable
    @Override
    public Material getItem(int position) {
        return filteredMaterials.get(position);
    }

    @Override
    public int getCount() {
        return filteredMaterials.size();
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new MaterialFilter(this,materials);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Material material = filteredMaterials.get(position);
        LayoutInflater inflater =   LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.material_layout,parent,false);
        TextView tvName = (TextView) convertView.findViewById(R.id.textViewName);
        ImageView ivIcon = (ImageView) convertView.findViewById(R.id.imageViewPhoto);
        tvName.setText(material.getName());
        new DownloadImageTask(ivIcon)
                .execute(material.getImg());
        return convertView;
    }
    private void loadImageFromUrl(String url,ImageView imageView){

    }
    private class MaterialFilter extends Filter{
        AutocompleteMaterialAdapter adapter;
        List<Material> originList;
        List<Material> filteredList;

        public MaterialFilter(AutocompleteMaterialAdapter adapter, List<Material> originList) {
            super();
            this.adapter = adapter;
            this.originList = originList;
            this.filteredList = new ArrayList<>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if(constraint != null){
                filteredList.clear();
                final FilterResults results = new FilterResults();
                if (constraint == null || constraint.length() == 0){
                    filteredList.addAll(originList);
                }
                else{
                    final String filterPattern = constraint.toString().toLowerCase().trim();
                    for(final Material material : originList){
                        if(material.getName().toLowerCase().contains(filterPattern)){
                            filteredList.add(material);
                        }
                    }
                }
                results.values = filteredList;
                results.count = filteredList.size();

                return  results;
            }
            else {
                return new FilterResults();
            }


        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results != null && results.count > 0){
                adapter.filteredMaterials.clear();
                adapter.filteredMaterials.addAll((List) results.values);
                adapter.notifyDataSetChanged();
            }
            else{
                adapter.filteredMaterials.clear();
                adapter.notifyDataSetChanged();
            }
        }
    }
}
