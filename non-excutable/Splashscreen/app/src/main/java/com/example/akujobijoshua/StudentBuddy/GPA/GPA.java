package com.example.akujobijoshua.StudentBuddy.GPA;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.akujobijoshua.StudentBuddy.R;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class GPA extends Fragment {

    public static int GRADE_POSITION=1;

    private ArrayAdapter<String> arrayAdapter;
    private  View v;
    public GPA() {
        // Required empty public constructor
    }


   private MaterialBetterSpinner materialBetterSpinner;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_gpa, container, false);

        ArrayList<String> mGPADropdown = new ArrayList<>();
mGPADropdown.add("4");
        mGPADropdown.add("9");
        materialBetterSpinner=(MaterialBetterSpinner) v.findViewById(R.id.spinPoints);
        arrayAdapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_dropdown_item_1line,mGPADropdown);
        materialBetterSpinner.setAdapter(arrayAdapter);

        Button cal=(Button) v.findViewById(R.id.calculate_button);
        cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(materialBetterSpinner.getText().toString().equals("4") ){
                    sendToCalculate();
                }else if (materialBetterSpinner.getText().toString().equals("9")){
                    sendToCalculate(1);
                }else { Toast.makeText(getContext(), "Please select a grade point", Toast.LENGTH_LONG).show();
                }
            }
        });
        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                revealRow();
            }
        });

        // Inflate the layout for this fragment
        return v;
    }

    public void sendToCalculate() {
        FourPointScaleCalculator calculator = new FourPointScaleCalculator();
        CompileGrades compiler = new CompileGrades();
        Double calc = calculator.calculate(compiler.getValues(v));
        DecimalFormat df = new DecimalFormat("###.##");
        String calculation = df.format(calc);
        // grab TextVew ID and display GPA

        AlertDialog.Builder dlg = new AlertDialog.Builder(getActivity());
        dlg.setTitle("Calculation Result");
        dlg.setMessage("Your GPA is " + calculation);
        dlg.show();
    }
    public void sendToCalculate(int i) {
        NinePointScaleCalculator calculator = new NinePointScaleCalculator();
        CompileGrades compiler = new CompileGrades();
        Double calc = calculator.calculate(compiler.getValues(v));
        DecimalFormat df = new DecimalFormat("###.##");
        String calculation = df.format(calc);
        // grab TextVew ID and display GPA

        AlertDialog.Builder dlg = new AlertDialog.Builder(getActivity());
        dlg.setTitle("Calculation Result");
        dlg.setMessage("Your GPA is " + calculation);
        dlg.show();
    }



    /**
     *
     * Reveals extra rows to add Grades for calculation on the press of button.
     */
    public void revealRow() {
        View view = (View) v.findViewById(R.id.four_point_table);
        GRADE_POSITION += 1;
        if (GRADE_POSITION == 2) {
            TableRow row = (TableRow) view.findViewById(R.id.grade2);
            row.setVisibility(View.VISIBLE);
        }
        else if (GRADE_POSITION == 3) {
            TableRow row = (TableRow) view.findViewById(R.id.grade3);
            row.setVisibility(View.VISIBLE);
        }
        else if (GRADE_POSITION == 4) {
            TableRow row = (TableRow) view.findViewById(R.id.grade4);
            row.setVisibility(View.VISIBLE);
        }
        else if (GRADE_POSITION == 5) {
            TableRow row = (TableRow) view.findViewById(R.id.grade5);
            row.setVisibility(View.VISIBLE);
        }
        else if (GRADE_POSITION == 6) {
            TableRow row = (TableRow) view.findViewById(R.id.grade6);
            row.setVisibility(View.VISIBLE);
        }
        else if (GRADE_POSITION == 7) {
            TableRow row = (TableRow) view.findViewById(R.id.grade7);
            row.setVisibility(View.VISIBLE);
        }
        else if (GRADE_POSITION == 8) {
            TableRow row = (TableRow) view.findViewById(R.id.grade8);
            row.setVisibility(View.VISIBLE);
        }

        else if (GRADE_POSITION == 9) {
            TableRow row = (TableRow) view.findViewById(R.id.grade9);
            row.setVisibility(View.VISIBLE);
        }

        else if (GRADE_POSITION == 10) {
            TableRow row = (TableRow) view.findViewById(R.id.grade10);
            row.setVisibility(View.VISIBLE);
        }

    }

}
