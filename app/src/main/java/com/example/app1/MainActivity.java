package com.example.app1;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

/**
 * MainActivity：主页面类
 * 继承 AppCompatActivity，相当于说"这是一个安卓页面"
 */
public class MainActivity extends AppCompatActivity {

    // ==================== 界面组件 ====================
    private EditText mInputHeight;
    private EditText mInputWeight;
    private TextView mTextResult;
    private TextView mTextBMI;
    private TextView mTextCategory;
    private Button mBtnCalculate;
    private Button mBtnBack;

    // 记录第一次按返回键的时间
    private long mBackPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 设置布局文件
        setContentView(R.layout.activity_main);

        // 初始化界面组件
        initViews();

        // 设置按钮点击事件
        setupClickListeners();
    }

    /**
     * 通过 id 找到 XML 里的组件
     */
    private void initViews() {
        mInputHeight  = findViewById(R.id.inputHeight);
        mInputWeight  = findViewById(R.id.inputWeight);
        mTextResult   = findViewById(R.id.textResult);
        mTextBMI      = findViewById(R.id.textBMI);
        mTextCategory = findViewById(R.id.textCategory);
        mBtnCalculate = findViewById(R.id.btnCalculate);
        mBtnBack      = findViewById(R.id.btnBack);
    }

    /**
     * 设置所有按钮的点击事件
     */
    private void setupClickListeners() {
        // 计算按钮
        mBtnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateBMI();
            }
        });

        // 返回按钮
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();  // 关闭当前页面
            }
        });
    }

    /**
     * 系统返回键处理
     * 连续按两次返回键才退出应用
     */
    @Override
    public void onBackPressed() {
        // 获取当前时间
        long currentTime = System.currentTimeMillis();

        // 如果两次按键间隔小于 2 秒，则退出
        if (currentTime - mBackPressedTime > 2000) {
            mBackPressedTime = currentTime;
            Toast.makeText(this, "再按一次返回键退出", Toast.LENGTH_SHORT).show();
        } else {
            // 调用父类方法，真正退出
            super.onBackPressed();
        }
    }

    // ==================== 核心方法：计算 BMI ====================

    /**
     * BMI = 体重(kg) ÷ 身高(m)²
     */
    private void calculateBMI() {
        String heightStr = mInputHeight.getText().toString().trim();
        String weightStr = mInputWeight.getText().toString().trim();

        if (heightStr.isEmpty() || weightStr.isEmpty()) {
            Toast.makeText(this, "请输入身高和体重", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int heightCm = Integer.parseInt(heightStr);
            double weightKg = Double.parseDouble(weightStr);

            if (heightCm <= 0 || heightCm > 300) {
                Toast.makeText(this, "请输入合理的身高（1-300cm）", Toast.LENGTH_SHORT).show();
                return;
            }
            if (weightKg <= 0 || weightKg > 500) {
                Toast.makeText(this, "请输入合理的体重（1-500kg）", Toast.LENGTH_SHORT).show();
                return;
            }

            double heightM = heightCm / 100.0;
            double bmi = weightKg / (heightM * heightM);

            displayResult(bmi);

        } catch (NumberFormatException e) {
            Toast.makeText(this, "请输入有效的数字", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 显示计算结果
     */
    private void displayResult(double bmi) {
        String bmiStr = String.format("%.1f", bmi);
        mTextBMI.setText(bmiStr);

        String category;
        int color;

        if (bmi < 18.5) {
            category = "偏瘦";
            color = 0xFFFF9800;
        } else if (bmi < 24) {
            category = "正常";
            color = 0xFF4CAF50;
        } else if (bmi < 28) {
            category = "偏胖";
            color = 0xFFFF5722;
        } else {
            category = "肥胖";
            color = 0xFFF44336;
        }

        mTextCategory.setText(category);
        mTextCategory.setTextColor(color);
        mTextBMI.setTextColor(color);
        mTextResult.setText("你的 BMI 值为：");
    }
}
