package it.jaschke.alexandria;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Paul Aranas on 11/4/2015.
 */
public class ErrorMessageDialog extends DialogFragment {

    private TextView content = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.error_message_dialog, container, false);
        content = (TextView) view.findViewById(R.id.textView);
        Bundle bundle = getArguments();
        String message = bundle.getString("Message");
        content.setText(message);


        return view;
    }
}



