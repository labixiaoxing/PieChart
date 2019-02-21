package visa.gezi.com.circleview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import xiaoxing.com.circleview.CircleView;

public class MainActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final CircleView circleView = findViewById(R.id.circleview);

        Button button = findViewById(R.id.button);
        final EditText editText = findViewById(R.id.edittext);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( !TextUtils.isEmpty(editText.getText().toString())){
                    circleView.setRate( Float.parseFloat(editText.getText().toString()));
                }
            }
        });
    }
}
