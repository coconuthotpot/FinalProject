package algonquin.cst2335.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AviationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aviation);

        Button showToastButton = findViewById(R.id.button);

        showToastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AviationActivity.this, "This is a Toast notification", Toast.LENGTH_SHORT).show();
            }
        });


        Button secondPageButton = findViewById(R.id.button4);
        secondPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 创建Intent对象，将当前Activity作为上下文参数，目标Activity作为参数传递给Intent构造函数
                Intent intent = new Intent(AviationActivity.this, AviationActivity2.class);
                startActivity(intent); // 启动目标Activity
            }
        });

    }
}