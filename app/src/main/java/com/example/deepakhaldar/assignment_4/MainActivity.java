package com.example.deepakhaldar.assignment_4;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileReader;

import weka.classifiers.Evaluation;
import weka.classifiers.functions.LibSVM;
import weka.core.Debug;
import weka.core.Instances;

public class MainActivity extends AppCompatActivity  {

    double accuracy;
    EditText Degree, Gamma, Coef0, Cost, NU, Epsilon,CacheSize, Tolerance, Accuracy;
    String algo, kernel;
    Spinner svm_type, kernel_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Motion Analyzer");
        setSupportActionBar(toolbar);

        Degree=(EditText)findViewById(R.id.degree_field);
        Gamma= (EditText)findViewById(R.id.gamma_field);
        Coef0= (EditText)findViewById(R.id.coef0_field);
        Cost= (EditText)findViewById(R.id.cost_field);
        NU= (EditText)findViewById(R.id.nu_field);
        Epsilon= (EditText) findViewById(R.id.epsilon_field);
        CacheSize=(EditText) findViewById(R.id.cache_field);
        Tolerance=(EditText)findViewById(R.id.tolerance_field);


        //Setting the default values on UI
        Degree.setText(""+3);
        Gamma.setText(""+(1/150));
        Coef0.setText(""+0);
        Cost.setText(""+1);
        NU.setText(""+0.5);
        Epsilon.setText(""+0.1);
        CacheSize.setText(""+100);
        Tolerance.setText("" + 0.01);



        //Setting the dropdown menu for SVM TYPE
        svm_type = (Spinner)findViewById(R.id.spinner1);
        String[] items = new String[]{"0 : C-SVC", "1 : nu-SVC"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        svm_type.setAdapter(adapter1);

        //Setting the dropdown menu for SVM TYPE
        kernel_type = (Spinner)findViewById(R.id.spinner2);
        items = new String[]{"0 : LINEAR", "1 : Polynomial", "2 : Radial basis", "3 : sigmoid"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        kernel_type.setAdapter(adapter2);


        //Listener for the  SVM TYPE dropdown
        svm_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                algo = svm_type.getSelectedItem().toString();
                if (algo.equals("1 : nu-SVC")) {
                    //Disable Cost
                    Cost.setEnabled(false);
                    NU.setEnabled(true);
                }
                if (algo.equals("0 : C-SVC")) {
                    //Disable NU
                    NU.setEnabled(false);
                    Cost.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




        //Listener for the Kernel Type dropdown
        kernel_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                kernel= kernel_type.getSelectedItem().toString();
                if(kernel.equals("0 : LINEAR")){
                    //Disable Gamma
                    Gamma.setEnabled(false);
                }
                else{
                    Gamma.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    //Method for Computing Accuracy
    public void onCompute(View v) {
        try {

            BufferedReader br = new BufferedReader(new FileReader(Environment.getExternalStorageDirectory() + "/Motion.arff"));
            Instances dataset = new Instances(br);
            dataset.setClassIndex(dataset.numAttributes() - 1);
            br.close();
            LibSVM classifier = new LibSVM();
            //Setting Default values for the parameters (Backend)
            String typeSVM = "0",
                    typeKernel = "0",
                    degree = "3",
                    gamma = "0",
                    coef0 = "0",
                    cost = "1",
                    nu = "0.5",
                    epsilon = "0.1",
                    cachesize = "100",
                    tolerance = "0.001";
            //For more info go to this link: https://www.csie.ntu.edu.tw/~cjlin/libsvm/


            //Getting values from the user.
            if (!TextUtils.isEmpty(Degree.getText()))
                degree = Degree.getText().toString();
            if (!TextUtils.isEmpty(Gamma.getText()))
                gamma = Gamma.getText().toString();
            if (!TextUtils.isEmpty(Coef0.getText()))
                coef0 = Coef0.getText().toString();
            if (!TextUtils.isEmpty(Cost.getText()))
                cost = Cost.getText().toString();
            if (!TextUtils.isEmpty(NU.getText()))
                nu = NU.getText().toString();
            if (!TextUtils.isEmpty(Epsilon.getText()))
                epsilon = Epsilon.getText().toString();
            if (!TextUtils.isEmpty(CacheSize.getText()))
                cachesize = CacheSize.getText().toString();
            if (!TextUtils.isEmpty(Tolerance.getText()))
                tolerance = Tolerance.getText().toString();

            typeSVM = svm_type.getSelectedItem().toString().substring(0, 1);
            typeKernel = kernel_type.getSelectedItem().toString().substring(0, 1);

            String options = ("-S " + typeSVM + " -K " + typeKernel + " -D " + degree + " -G " + gamma + " -R "
                    + coef0 + " -N " + nu + " -M " + cachesize + " -C " + cost + " -E " + tolerance + " -P " + epsilon);
            String[] optionsArray = options.split(" ");
            classifier.setOptions(optionsArray);
            classifier.buildClassifier(dataset);
            Evaluation eval = new Evaluation(dataset);
            eval.crossValidateModel(classifier, dataset, 5, new Debug.Random(1));//k=4
            accuracy = (eval.truePositiveRate(1) + eval.trueNegativeRate(1)) / (eval.truePositiveRate(1) + eval.trueNegativeRate(1) + eval.falseNegativeRate(1) + eval.falsePositiveRate(1));
            Accuracy = (EditText) findViewById(R.id.accuracy_field);
            accuracy = Math.round(accuracy * 100.0) / 100.0;
            //progressBar.setVisibility(View.INVISIBLE);
            Accuracy.setText("" + accuracy);
            Toast.makeText(this, "Accuracy is :"+accuracy, Toast.LENGTH_LONG).show();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

}
