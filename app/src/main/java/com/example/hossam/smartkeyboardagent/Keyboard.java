package com.example.hossam.smartkeyboardagent;

import android.content.Context;
import android.content.Intent;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.provider.UserDictionary;
import android.speech.RecognizerIntent;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Hossam on 10/2/2017.
 */

public class Keyboard  extends InputMethodService implements KeyboardView.OnKeyboardActionListener {
    private KeyboardView keyboardView;
    private android.inputmethodservice.Keyboard keyboard;
    InputConnection cn;

    Context context;

    boolean language = false;

    boolean number = false;
    private boolean caps = false;


    @Override
    public View onCreateInputView() {

        keyboardView = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard, null);
        keyboard = new android.inputmethodservice.Keyboard(this, R.xml.keyboard);
        keyboardView.setKeyboard(keyboard);
        keyboardView.setOnKeyboardActionListener(this);
        return keyboardView;
    }

    public void playback(int KeyCode) {
        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        switch (KeyCode) {
            case 32:
                audioManager.playSoundEffect(audioManager.FX_KEYPRESS_SPACEBAR);
                break;
            case 10:
                audioManager.playSoundEffect(audioManager.FX_KEYPRESS_RETURN);
                break;
            case android.inputmethodservice.Keyboard.KEYCODE_DELETE:
                audioManager.playSoundEffect(audioManager.FX_KEYPRESS_DELETE);
                break;
            default:
                audioManager.playSoundEffect(audioManager.FX_KEYPRESS_STANDARD);
        }
    }

    @Override
    public void onPress(int primaryCode) {

    }

    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {

         cn = getCurrentInputConnection();
        playback(primaryCode);
        switch (primaryCode) {

            case -101:
                if (language == false) {
                    keyboard = new android.inputmethodservice.Keyboard(this, R.xml.arabic);
                    keyboardView.setKeyboard(keyboard);
                    language = true;
                } else {
                    keyboard = new android.inputmethodservice.Keyboard(this, R.xml.keyboard);
                    keyboardView.setKeyboard(keyboard);
                    language = false;
                }
                break;

            case android.inputmethodservice.Keyboard.KEYCODE_DELETE:
                cn.deleteSurroundingText(1, 0);
                break;
            case -2:
                if (number == false) {
                    keyboard = new android.inputmethodservice.Keyboard(this, R.xml.symbols);
                    keyboardView.setKeyboard(keyboard);
                    number = true;
                } else {
                    keyboard = new android.inputmethodservice.Keyboard(this, R.xml.keyboard);
                    keyboardView.setKeyboard(keyboard);
                    number = false;
                }
                break;
            case android.inputmethodservice.Keyboard.KEYCODE_SHIFT:
                caps = !caps;
                keyboard.setShifted(caps);
                keyboardView.invalidateAllKeys();
                break;
            case android.inputmethodservice.Keyboard.KEYCODE_DONE:
                cn.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                break;
            default:
                char code = (char) primaryCode;
                if (Character.isLetter(code) && caps) {
                    code = Character.toUpperCase(code);
                }
                cn.commitText(String.valueOf(code), 1);
        }
    }


    @Override
    public void onText(CharSequence text) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }

}
