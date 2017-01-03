package mountainq.organization.dongguk.hanbokapp.search;

import java.util.List;

/**
 * Created by oneno on 2017-01-04.
 */

public interface OnFinishSearchListener {
    public void onSuccess(List<Item> itemList);
    public void onFail();
}
