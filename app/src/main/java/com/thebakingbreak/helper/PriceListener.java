package com.thebakingbreak.helper;

import com.thebakingbreak.models.CartUpdateModel;

public interface PriceListener {

    void onClickSubtractPriceListener(String price, int position);
    void onClickAddPriceListener(String price, int position);

    void onClickMaxSubtractPriceListener(String price, int position);
    void onClickMaxAddPriceListener(String price, int position);

    void deleteCardItem(CartUpdateModel model, int position);
}
