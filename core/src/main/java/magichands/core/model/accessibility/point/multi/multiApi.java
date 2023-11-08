package magichands.core.model.accessibility.point.multi;

import android.os.Build;

import androidx.annotation.RequiresApi;

public class multiApi implements multi {
    private init acEventApi;
    public multiApi(init oO80o) {
        this.acEventApi = oO80o;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean touchDown(int i, int i2, int i3) {
        return this.acEventApi.LLI1lI1(i, i2, 1);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean touchMove(int i, int i2, int i3) {
        return this.acEventApi.m1930Lil1l(i, i2, i3);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean touchUp(int i, int i2, int i3) {
        return this.acEventApi.m3923lILLLIL(i, i2, i3);

    }


}
