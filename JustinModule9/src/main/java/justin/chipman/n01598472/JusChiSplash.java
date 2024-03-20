// justin chipman n01598472
package justin.chipman.n01598472;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class JusChiSplash extends Activity {

    // Duration of wait
    private final int SPLASH_DISPLAY_LENGTH = 3000; // 3000 ms = 3 seconds

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jus_chi_splash);

        // New Handler to start the Menu-Activity and close this Splash-Screen after some seconds.
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                // Create an Intent that will start the main activity.
                Intent mainIntent = new Intent(JusChiSplash.this,MainActivity.class);
                JusChiSplash.this.startActivity(mainIntent);
                JusChiSplash.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
