package com.dodelivery;

import android.graphics.Rect;
import android.os.Environment;
import android.util.SparseArray;

import com.google.android.gms.maps.model.Tile;
import com.google.android.gms.maps.model.TileProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by juhee on 4/6/17.
 */

public class CustomMapTileProvider implements TileProvider {
    private static final int TILE_WIDTH = 256;
    private static final int TILE_HEIGHT = 256;
    private static final int BUFFER_SIZE = 16 * 1024;

    private static final SparseArray<Rect> TILE_ZOOMS = new SparseArray<Rect>() {{
        put(8,  new Rect(135,  180,  135,  181 ));
        put(9,  new Rect(270,  361,  271,  363 ));
        put(10, new Rect(541,  723,  543,  726 ));
        put(11, new Rect(1082, 1447, 1086, 1452));
        put(12, new Rect(2165, 2894, 2172, 2905));
        put(13, new Rect(4330, 5789, 4345, 5810));
        put(14, new Rect(8661, 11578, 8691, 11621));
    }};

    public CustomMapTileProvider() {

    }

    @Override
    public Tile getTile(int x, int y, int zoom) {
        y = fixYCoordinate(y, zoom);

        if (hasTile(x, y, zoom)) {
            byte[] image = readTileImage(x, y, zoom);
            return image == null ? null : new Tile(TILE_WIDTH, TILE_HEIGHT, image);
        } else {
            return NO_TILE;
        }
    }

    private boolean hasTile(int x, int y, int zoom) {
        Rect b = TILE_ZOOMS.get(zoom);
        return b == null ? false : (b.left <= x && x <= b.right && b.top <= y && y <= b.bottom);
    }

    private byte[] readTileImage(int x, int y, int zoom) {
        InputStream in = null;
        ByteArrayOutputStream buffer = null;

        try {
            File file = new File(Environment.getExternalStorageDirectory() + "/" + DeliveryBaseApplication.instance().getPackageName() + "/"+getTileFilename(x,y,zoom));
            if(file.exists()) {
                in = new FileInputStream(file);
                buffer = new ByteArrayOutputStream();

                int nRead;
                byte[] data = new byte[BUFFER_SIZE];

                while ((nRead = in.read(data, 0, BUFFER_SIZE)) != -1) {
                    buffer.write(data, 0, nRead);
                }
                buffer.flush();

                return buffer.toByteArray();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        } finally {
            if (in != null) try { in.close(); } catch (Exception ignored) {}
            if (buffer != null) try { buffer.close(); } catch (Exception ignored) {}
        }
        return null;
    }

    public String getTileFilename(int x, int y, int zoom) {
        return "map_" + zoom + '/' + x + '/' + y + ".png";
    }

    private int fixYCoordinate(int y, int zoom) {
        int size = 1 << zoom; // size = 2^zoom
        return size - 1 - y;
    }
}