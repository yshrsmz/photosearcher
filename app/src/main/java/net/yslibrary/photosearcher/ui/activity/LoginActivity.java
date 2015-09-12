package net.yslibrary.photosearcher.ui.activity;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import net.yslibrary.photosearcher.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.Bind;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by yshrsmz on 15/08/24.
 */
public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.login_button)
    TwitterLoginButton mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TwitterSession session = Twitter.getSessionManager().getActiveSession();
        if (session != null) {
            startActivity(MainActivity.createIntent(this));
            finish();
        }

        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        mLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Timber.d("Authorization succeeded");
                Timber.d("name: %s", result.data.getUserName());
                mLoginButton.setVisibility(View.GONE);

                startActivity(MainActivity.createIntent(LoginActivity.this));
                finish();
            }

            @Override
            public void failure(TwitterException e) {
                Timber.e(e, e.getMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mLoginButton.onActivityResult(requestCode, resultCode, data);
    }
}
