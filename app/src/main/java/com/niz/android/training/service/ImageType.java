package com.niz.android.training.service;

import android.content.Context;
import com.niz.android.training.R;
import org.apache.commons.lang3.StringUtils;

public enum ImageType {

    RANDOM(R.string.surprise_me, null),
    MURRAY(R.string.fill_murray, "murray"),
    CAGE(R.string.place_cage, "cage"),
    ;

    private int titleResId;
    private String type;

    ImageType(int titleResId, String type) {
        this.titleResId = titleResId;
        this.type = type;
    }

    public String getTitle(Context context) {
        return context.getString(titleResId);
    }

    public String getType() {
        return type;
    }

    public static ImageType fromType(String type) {
        for (ImageType imageType : values()) {
            if (StringUtils.equalsIgnoreCase(imageType.getType(), type)) {
                return imageType;
            }
        }
        throw new IllegalArgumentException("No ImageType with type=" + type);
    }
}

