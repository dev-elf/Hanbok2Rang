package mountainq.organization.dongguk.hanbokapp.search;

import java.util.List;

/**
 * Created by oneno on 2017-01-04.
 */

public interface OnFinishSearchListener {
    void onSuccess(List<Item> itemList);
    void onFail();
}
