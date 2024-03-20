// justin chipman n01598472
package justin.chipman.n01598472;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Jus1tin extends Fragment {
    private final String sharedPrefKey = "shared preferences";
    private final String courseKey = "courses";

    private EditText courseNameEdt, courseDescEdt;
    private Button addBtn, saveBtn, deleteBtn;
    private RecyclerView courseRV;

    private CourseAdapter adapter; // Make sure to create an adapter class for RecyclerView
    private ArrayList<CourseModal> courseModalArrayList;

    public Jus1tin() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_jus1tin, container, false);

        courseNameEdt = view.findViewById(R.id.jusidEdtCourseName);
        courseDescEdt = view.findViewById(R.id.jusidEdtCourseDescription);
        addBtn = view.findViewById(R.id.jusidBtnAdd);
        saveBtn = view.findViewById(R.id.jusidBtnSave);
        deleteBtn = view.findViewById(R.id.jusidBtnDelete);
        courseRV = view.findViewById(R.id.jusidRVCourses);

        loadData();
        buildRecyclerView();

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courseModalArrayList.add(new CourseModal(courseNameEdt.getText().toString(), courseDescEdt.getText().toString()));
                adapter.notifyItemInserted(courseModalArrayList.size());
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the list is empty
                if (!courseModalArrayList.isEmpty()) {
                    // Clear the list
                    courseModalArrayList.clear();
                    // Notify the adapter to update the RecyclerView
                    adapter.notifyDataSetChanged();
                    // Save the empty list to SharedPreferences
                    saveData();
                } else {
                    // Show a toast message if there's nothing to delete
                    Toast.makeText(getContext(), getString(R.string.no_data_to_delete), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void buildRecyclerView() {
        adapter = new CourseAdapter(courseModalArrayList, getContext());
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        courseRV.setHasFixedSize(true);
        courseRV.setLayoutManager(manager);
        courseRV.setAdapter(adapter);
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(sharedPrefKey, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(courseKey, null);
        Type type = new TypeToken<ArrayList<CourseModal>>() {}.getType();
        courseModalArrayList = gson.fromJson(json, type);

        if (courseModalArrayList == null) {
            courseModalArrayList = new ArrayList<>();
        }
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(sharedPrefKey, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(courseModalArrayList);
        editor.putString(courseKey, json);
        editor.apply();
        Toast.makeText(getContext(), getString(R.string.saved_array_list_to_shared_preferences), Toast.LENGTH_SHORT).show();
    }
}