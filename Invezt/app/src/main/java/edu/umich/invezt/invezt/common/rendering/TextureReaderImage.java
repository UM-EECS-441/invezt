package edu.umich.invezt.invezt.common.rendering;

import java.nio.ByteBuffer;

/** Image Buffer Class. */
public class TextureReaderImage {
    /** The id corresponding to RGBA8888. */
    public static final int IMAGE_FORMAT_RGBA = 0;

    /** The id corresponding to grayscale. */
    public static final int IMAGE_FORMAT_I8 = 1;

    /** The width of the image, in pixels. */
    public int width;

    /** The height of the image, in pixels. */
    public int height;

    /** The image buffer. */
    public ByteBuffer buffer;

    /** Pixel format. Can be either IMAGE_FORMAT_RGBA or IMAGE_FORMAT_I8. */
    public int format;

    /** Default constructor. */
    public TextureReaderImage() {
        width = 1;
        height = 1;
        format = IMAGE_FORMAT_RGBA;
        buffer = ByteBuffer.allocateDirect(4);
    }

    /**
     * Constructor.
     *
     * @param imgWidth the width of the image, in pixels.
     * @param imgHeight the height of the image, in pixels.
     * @param imgFormat the format of the image.
     * @param imgBuffer the buffer of the image pixels.
     */
    public TextureReaderImage(int imgWidth, int imgHeight, int imgFormat, ByteBuffer imgBuffer) {
        if (imgWidth == 0 || imgHeight == 0) {
            throw new RuntimeException("Invalid image size.");
        }

        if (imgFormat != IMAGE_FORMAT_RGBA && imgFormat != IMAGE_FORMAT_I8) {
            throw new RuntimeException("Invalid image format.");
        }

        if (imgBuffer == null) {
            throw new RuntimeException("Pixel buffer cannot be null.");
        }

        width = imgWidth;
        height = imgHeight;
        format = imgFormat;
        buffer = imgBuffer;
    }
}
