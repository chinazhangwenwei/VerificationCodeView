package com.xol.codelibrary;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wwzhang on 2019/4/10
 */
public class VerificationView extends LinearLayout implements CodeEditText.CodeListener {
    //验证码数量
    private int codeNum = 0;
    //验证码view集合便于后续操作
    private List<CodeEditText> editTextList;

    public void setCodeSuccess(CodeSuccess codeSuccess) {
        this.codeSuccess = codeSuccess;
    }

    private CodeSuccess codeSuccess;


    public VerificationView(Context context) {
        this(context, null);
    }

    public VerificationView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerificationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
//        CodeEditText codeEditText = new CodeEditText(context);
//        codeEditText.setMaxEms(1);
//        codeEditText.setMaxLines(1);
//        codeEditText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0,
//                LayoutParams.WRAP_CONTENT);
//        layoutParams.weight = 1;
//        codeEditText.setGravity(Gravity.CENTER);
//        addView(codeEditText, layoutParams);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (editTextList == null) {
            codeNum = getChildCount();
            if (codeNum > 0) {
                editTextList = new ArrayList<>(codeNum);
                for (int i = 0; i < codeNum; i++) {
                    editTextList.add((CodeEditText) getChildAt(i));
                    editTextList.get(i).setCodeListener(this);
                }
            }
        }
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
