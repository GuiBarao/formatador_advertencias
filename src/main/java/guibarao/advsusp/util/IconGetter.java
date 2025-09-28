package guibarao.advsusp.util;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class IconGetter {

    private static Map<String, Image> iconsCache = new HashMap<>();

    private IconGetter() {}

    public static ImageView getIconImageView(String fileName) {

        Image iconImage = iconsCache.get(fileName);

        if (iconImage == null) {
            iconImage = new Image(Objects.requireNonNull(IconGetter.class.getResourceAsStream("/icons/".concat(fileName))));
            iconsCache.put(fileName, iconImage);
        }

        return new ImageView(iconImage);
    }

}
