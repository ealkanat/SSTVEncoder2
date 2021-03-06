/*
Copyright 2017 Olga Miller <olga.rgb@gmail.com>

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package om.sstvencoder.Modes.ImageFormats;

import android.graphics.Bitmap;

class NV21 extends Yuv {
    NV21(Bitmap bitmap) {
        super(bitmap);
    }

    protected void convertBitmapToYuv(Bitmap bitmap) {
        mYuv = new byte[(3 * mWidth * mHeight) / 2];
        int pos = 0;

        for (int h = 0; h < mHeight; ++h)
            for (int w = 0; w < mWidth; ++w)
                mYuv[pos++] = (byte) YuvConverter.convertToY(bitmap.getPixel(w, h));

        for (int h = 0; h < mHeight; h += 2) {
            for (int w = 0; w < mWidth; w += 2) {
                int v0 = YuvConverter.convertToV(bitmap.getPixel(w, h));
                int v1 = YuvConverter.convertToV(bitmap.getPixel(w + 1, h));
                int v2 = YuvConverter.convertToV(bitmap.getPixel(w, h + 1));
                int v3 = YuvConverter.convertToV(bitmap.getPixel(w + 1, h + 1));
                mYuv[pos++] = (byte) ((v0 + v1 + v2 + v3) / 4);
                int u0 = YuvConverter.convertToU(bitmap.getPixel(w, h));
                int u1 = YuvConverter.convertToU(bitmap.getPixel(w + 1, h));
                int u2 = YuvConverter.convertToU(bitmap.getPixel(w, h + 1));
                int u3 = YuvConverter.convertToU(bitmap.getPixel(w + 1, h + 1));
                mYuv[pos++] = (byte) ((u0 + u1 + u2 + u3) / 4);
            }
        }
    }

    public int getY(int x, int y) {
        return 255 & mYuv[mWidth * y + x];
    }

    public int getU(int x, int y) {
        return 255 & mYuv[mWidth * mHeight + mWidth * (y >> 1) + (x | 1)];
    }

    public int getV(int x, int y) {
        return 255 & mYuv[mWidth * mHeight + mWidth * (y >> 1) + (x & ~1)];
    }
}
