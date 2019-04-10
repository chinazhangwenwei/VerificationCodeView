package com.xol.codelibrary;

import android.view.KeyEvent;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;

/**
 * Created by wwzhang on 2019/4/10
 */
public class CodeInputConnection extends InputConnectionWrapper {

    public void setCodeInputListener(CodeInputListener codeInputListener) {
        this.codeInputListener = codeInputListener;
    }

    private CodeInputListener codeInputListener;

    public CodeInputConnection(InputConnection target, boolean mutable) {
        super(target, mutable);
    }

    /**
     * Commit text to the text box and set the new cursor position.
     */
    @Override
    public boolean commitText(CharSequence text, int newCursorPosition) {
        //文本处理 当为数字才处理，非数字则不做任何处理；
        if (codeInputListener != null) {
            if (text.toString().matches("\\d")) {
                codeInputListener.inputNumber(text);
                return true;
            }
        }
        return super.commitText(text, newCursorPosition);
    }

    /**
     * 当软键盘删除文本之前，会调用这个方法通知输入框，我们可以重写这个方法并判断是否要拦截这个删除事件。
     * 在谷歌输入法上，点击退格键的时候不会调用{@link #sendKeyEvent(KeyEvent event)}，
     * 而是直接回调这个方法，所以也要在这个方法上做拦截；
     */
    @Override
    public boolean deleteSurroundingText(int beforeLength, int afterLength) {
        if (codeInputListener != null) {
            codeInputListener.delete();
            return true;
        }
        return super.deleteSurroundingText(beforeLength, afterLength);
    }


    /**
     * 当在软件盘上点击某些按钮（比如退格键，数字键，回车键等），该方法可能会被触发（取决于输入法的开发者），
     * 所以也可以重写该方法并拦截这些事件，这些事件就不会被分发到输入框了
     */
    @Override
    public boolean sendKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (null != codeInputListener) {
                codeInputListener.delete();
                return true;
            }
        }
        return super.sendKeyEvent(event);
    }

    public interface CodeInputListener {

        void delete();

        void inputNumber(CharSequence numbs);

    }
}
