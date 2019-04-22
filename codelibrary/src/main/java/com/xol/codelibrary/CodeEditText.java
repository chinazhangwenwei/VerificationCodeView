package com.xol.codelibrary;

import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;

/**
 * Created by wwzhang on 2019/4/10
 */
public class CodeEditText extends AppCompatEditText implements CodeInputConnection.CodeInputListener {

    public void setCodeListener(CodeListener codeListener) {
        this.codeListener = codeListener;
    }

    private CodeListener codeListener;
    private CodeInputConnection inputConnection;

    public CodeEditText(Context context) {
        this(context, null);
    }

    public CodeEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CodeEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        inputConnection = new CodeInputConnection(null, true);
        inputConnection.setCodeInputListener(this);

    }

    //设置EditText只在最后位置
    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        if (selStart == selEnd) {//防止不能多选
            setSelection(getText().length());
        }
    }

    //拦截粘贴事件
    @Override
    public boolean onTextContextMenuItem(int id) {
        boolean consumed = super.onTextContextMenuItem(id);
        if (consumed) {
            switch (id) {
                case android.R.id.paste:
                    if (codeListener != null) {
                        ClipboardManager clipboardManager = (ClipboardManager)
                                getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                        codeListener.onCopy(clipboardManager.getPrimaryClip().
                                getItemAt(0).getText().toString());
                    }
                    break;
            }
        }
        return consumed;
    }

    //代理inputConection
    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        inputConnection.setTarget(super.onCreateInputConnection(outAttrs));
        return inputConnection;
    }

    @Override
    public void delete() {
        if (null != codeListener) {
            codeListener.onDelete(this);
        }

    }

    @Override
    public void inputNumber(CharSequence numbs) {
        if (null != codeListener) {
            String number = numbs.toString();
            if (number.length() > 1) {
                codeListener.onCopy(number);
            } else {
                codeListener.onInputNumber(number, this);
            }
        }

    }

    //code 监听
    public interface CodeListener {

        void onCopy(String codes);

        void onInputNumber(String code, EditText view);

        void onDelete(EditText view);
    }
}
