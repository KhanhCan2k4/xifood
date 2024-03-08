package vn.edu.tdc.quanlynhansu2023.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

import java.util.ArrayList;

import vn.edu.tdc.quanlynhansu2023.R;
import vn.edu.tdc.quanlynhansu2023.databinding.ListItemBinding;
import vn.edu.tdc.quanlynhansu2023.databinding.ListItemInvertBinding;
import vn.edu.tdc.quanlynhansu2023.models.MyPerson;

public class PersonAdapter extends ArrayAdapter<MyPerson> {
    private Activity context;
    private ArrayList<MyPerson> people;
    public PersonAdapter(@NonNull Activity context, int resource, @NonNull ArrayList<MyPerson> objects) {
        super(context, resource, objects);
        this.context = context;
        people = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewBinding binding;
       // Tao moi phan tu viewItem
        if (convertView == null) {
            if (position % 2 == 0) {
                binding = ListItemBinding.inflate(context.getLayoutInflater(), parent, false);
            }
            else {
                binding = ListItemInvertBinding.inflate(context.getLayoutInflater(), parent, false);
            }
            convertView = binding.getRoot();
            convertView.setTag(binding);
        }
        // Tai su dung lai
        else {
            if (position % 2 == 0) {
                binding = (ListItemBinding) convertView.getTag();
            }
            else {
                binding = (ListItemInvertBinding) convertView.getTag();
            }
        }
        // Set data
        MyPerson person = people.get(position);
        if (position % 2 == 0) {
            ((ListItemBinding)binding).lblName.setText(person.getName());
            ((ListItemBinding)binding).lblHobbies.setText(person.getHobbies());
            if (person.getDegree().equalsIgnoreCase(context.getResources().getString(R.string.radCaoDang))) {
                ((ListItemBinding)binding).imgDegree.setImageResource(R.mipmap.college);
            }
            else if (person.getDegree().equalsIgnoreCase(context.getResources().getString(R.string.radDaiHoc))) {
                ((ListItemBinding)binding).imgDegree.setImageResource(R.mipmap.university);
            }
            else if (person.getDegree().equalsIgnoreCase(context.getResources().getString(R.string.radTrungCap))) {
                ((ListItemBinding)binding).imgDegree.setImageResource(R.mipmap.midium);
            }
            else {
                ((ListItemBinding)binding).imgDegree.setImageResource(R.mipmap.none);
            }
        }
        else {
            ((ListItemInvertBinding)binding).lblName.setText(person.getName());
            ((ListItemInvertBinding)binding).lblHobbies.setText(person.getHobbies());
            if (person.getDegree().equalsIgnoreCase(context.getResources().getString(R.string.radCaoDang))) {
                ((ListItemInvertBinding)binding).imgDegree.setImageResource(R.mipmap.college);
            }
            else if (person.getDegree().equalsIgnoreCase(context.getResources().getString(R.string.radDaiHoc))) {
                ((ListItemInvertBinding)binding).imgDegree.setImageResource(R.mipmap.university);
            }
            else if (person.getDegree().equalsIgnoreCase(context.getResources().getString(R.string.radTrungCap))) {
                ((ListItemInvertBinding)binding).imgDegree.setImageResource(R.mipmap.midium);
            }
            else {
                ((ListItemInvertBinding)binding).imgDegree.setImageResource(R.mipmap.none);
            }
        }
        return convertView;
    }
}
