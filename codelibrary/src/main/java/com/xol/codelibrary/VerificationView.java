package com.xol.codelibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wwzhang on 2019/4/10
 */
public class VerificationView extends LinearLayout implements CodeEditText.CodeListener {
    //验证码数量
    private int codeNum = 4;
    //验证码间距
    private int contentMargin = 16;
    //验证码字体大小
    private int codeTextSize = 16;
    //验证码style
    private int styleResource = 0;
    //验证码view集合便于后续操作
    private List<EditText> editTextList;

    private CodeSuccess codeSuccess;


    public VerificationView(Context context) {
        this(context, null);
    }

    public VerificationView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerificationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
    }

    private void initAttr(Context context, AttributeSet attrs) {
        setOrientation(HORIZONTAL);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.VerificationView);
        codeNum = typedArray.getInt(R.styleable.VerificationView_code_num, codeNum);
        contentMargin = typedArray.getDimensionPixelSize(R.styleable.VerificationView_code_margin, contentMargin);
        codeTextSize = typedArray.getDimensionPixelSize(R.styleable.VerificationView_code_text_size, codeTextSize);
        typedArray.recycle();
        editTextList = new ArrayList<>(codeNum);
        for (int i = 0; i < codeNum; i++) {
            CodeEditText codeEditText = new CodeEditText(context);

            codeEditText.setCodeListener(this);
            //添加子view
            addView(codeEditText);
            editTextList.add(codeEditText);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onCopy(String codes) {
        char[] chars = codes.toCharArray();
        int len = Math.min(chars.length, codeNum);
        for (int i = 0; i < len; i++) {
            editTextList.get(i).setText(String.valueOf(chars[i]));
        }
        editTextList.get(len - 1).requestFocus();
        computerSuccess();

    }

    @Override
    public void onInputNumber(String code, EditText view) {
        int position = editTextList.indexOf(view);
        if (position == codeNum - 1) {
            //最后一位 且没有数据
            if (view.getText().toString().length() < 1) {
                view.setText(code);
            }
        } else {
            //非最后一位，下一位设置数据
            if (view.getText().toString().length() > 0) {
                editTextList.get(position + 1).setText(code);
            } else {
                view.setText(code);
            }
            editTextList.get(position + 1).requestFocus();
        }
        computerSuccess();
    }

    @Override
    public void onDelete(EditText view) {
        int position = editTextList.indexOf(view);
        if (position == 0) {
            view.setText("");
        } else {
            position = position - 1;
            if (view.getText().toString().length() > 0) {
                view.setText("");
            }
            editTextList.get(position).requestFocus();
        }
    }

    /**
     * 计算是否输入完毕
     */
    private void computerSuccess() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < codeNum; i++) {
            EditText editText = editTextList.get(i);
            String code = editText.getText().toString();
            if (code.length() < 1) {
                return;
            }
            sb.append(code);
        }
        if (null != codeSuccess) {
            codeSuccess.codeSuccess(sb.toString());
        }

    }

    public interface CodeSuccess {
        void codeSuccess(String codes);

    }

}
